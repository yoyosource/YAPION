/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.config.annotationproccessing.generator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import yapion.hierarchy.output.Indentator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public class ClassGenerator {

    private List<String> annotations = new ArrayList<>();

    @Setter
    private ModifierGenerator modifierGenerator = new ModifierGenerator(ModifierType.PUBLIC);
    private final String packageName;
    private final String className;

    private Set<String> imports = new LinkedHashSet<>();
    private String extendsString = null;
    private List<String> interfaces = new ArrayList<>();
    private List<FieldGenerator> fields = new ArrayList<>();
    private List<FunctionGenerator> functions = new ArrayList<>();
    private List<ClassGenerator> internalClasses = new ArrayList<>();

    public ClassGenerator(ModifierGenerator modifierGenerator, String packageName, String className) {
        this.modifierGenerator = modifierGenerator;
        this.packageName = packageName;
        this.className = className;
    }

    public ClassGenerator addImport(String importName) {
        imports.add(importName);
        return this;
    }

    public ClassGenerator setExtendsString(String extendsString) {
        this.extendsString = extendsString;
        return this;
    }

    public ClassGenerator addAnnotation(Class<? extends Annotation> type) {
        annotations.add("@" + type.getTypeName());
        return this;
    }

    public ClassGenerator addAnnotation(String type) {
        if (type.startsWith("@")) {
            annotations.add(type);
        } else {
            annotations.add("@" + type);
        }
        return this;
    }

    public ClassGenerator addInterface(String type) {
        interfaces.add(type);
        return this;
    }

    public ClassGenerator add(FieldGenerator fieldGenerator) {
        if (fieldGenerator == null) return this;
        fields.add(fieldGenerator);
        return this;
    }

    public ClassGenerator add(FieldGenerator fieldGenerator, boolean getter, boolean setter, boolean builder) {
        fields.add(fieldGenerator);

        if (getter) {
            FunctionGenerator getterFunction = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "get" + capitalize(fieldGenerator.getName()), fieldGenerator.getType());
            getterFunction.add("return " + fieldGenerator.getName() + ";");
            functions.add(getterFunction);
        }
        if (setter) {
            FunctionGenerator setterFunction = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "set" + capitalize(fieldGenerator.getName()), builder ? className : void.class.getTypeName(), new ParameterGenerator(fieldGenerator.getType(), fieldGenerator.getName()));
            setterFunction.add("this." + fieldGenerator.getName() + " = " + fieldGenerator.getName() + ";");
            if (builder) {
                setterFunction.add("return this;");
            }
            functions.add(setterFunction);
        }
        return this;
    }

    private String capitalize(String name) {
        if (name.length() == 0) return name;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public ClassGenerator add(FunctionGenerator functionGenerator) {
        functions.add(functionGenerator);
        return this;
    }

    public ClassGenerator add(ClassGenerator classGenerator) {
        internalClasses.add(classGenerator);
        return this;
    }

    public List<String> output(int indent) {
        List<String> strings = new ArrayList<>();
        if (indent == 0) {
            strings.add("package " + packageName + ";");
            strings.add("");
            importOutput(this, strings);
            if (!imports.isEmpty()) {
                strings.add("");
            }
        }
        annotations.forEach(s -> {
            strings.add(Indentator.QUAD_SPACE.indent(indent) + s);
        });
        String s = String.join(", ", interfaces);
        strings.add(Indentator.QUAD_SPACE.indent(indent) + modifierGenerator.output() + "class " + className + (extendsString == null ? "" : " extends " + extendsString) + (s.isEmpty() ? "" : " implements " + s) + " {");
        fields.forEach(fieldGenerator -> {
            List<String> current = fieldGenerator.output(indent + 1);
            if (current.size() != 1) {
                strings.add("");
            }
            strings.addAll(current);
        });
        functions.forEach(functionGenerator -> {
            strings.add("");
            strings.addAll(functionGenerator.output(indent + 1));
        });
        internalClasses.forEach(classGenerator -> {
            strings.add("");
            strings.addAll(classGenerator.output(indent + 1));
        });
        strings.add(Indentator.QUAD_SPACE.indent(indent) + "}");
        return strings;
    }

    private void importOutput(ClassGenerator classGenerator, List<String> strings) {
        classGenerator.imports.forEach(s -> {
            strings.add("import " + s + ";");
        });
        classGenerator.internalClasses.forEach(current -> {
            importOutput(current, strings);
        });
    }
}
