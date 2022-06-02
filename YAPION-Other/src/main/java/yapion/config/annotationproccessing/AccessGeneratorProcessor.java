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

package yapion.config.annotationproccessing;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import yapion.annotations.api.ProcessorImplementation;
import yapion.annotations.config.YAPIONAccessGenerator;
import yapion.config.annotationproccessing.generator.*;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.parser.YAPIONParser;
import yapion.parser.options.FileOptions;
import yapion.parser.options.ParseOptions;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@ProcessorImplementation
@SupportedAnnotationTypes("yapion.annotations.config.YAPIONAccessGenerator")
public class AccessGeneratorProcessor extends AbstractProcessor {

    private Messager messager;
    private Element currentElement;
    private final AtomicInteger index = new AtomicInteger(0);
    private final AtomicBoolean toString = new AtomicBoolean(false);
    private final AtomicBoolean setter = new AtomicBoolean(false);
    private final AtomicBoolean lombokExtensionMethod = new AtomicBoolean(false);

    private boolean checkValueNeeded = false;
    private boolean checkObjectNeeded = false;
    private boolean checkArrayNeeded = false;
    private boolean checkMapNeeded = false;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @SneakyThrows
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(YAPIONAccessGenerator.class);
        for (Element element : elements) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Processing " + element.getSimpleName());
            currentElement = element;
            if (element.getKind() != ElementKind.FIELD) {
                error("Element needs to be field");
                continue;
            }
            if (!element.getModifiers().contains(Modifier.FINAL)) {
                error("Element needs to be final");
                continue;
            }

            messager.printMessage(Diagnostic.Kind.NOTE, "Accessing YAPIONAccessGenerator on " + element.getSimpleName());
            YAPIONAccessGenerator yapionAccessGenerator = element.getAnnotation(YAPIONAccessGenerator.class);
            toString.set(yapionAccessGenerator.generateToString());
            setter.set(yapionAccessGenerator.setter());
            lombokExtensionMethod.set(yapionAccessGenerator.lombokExtensionMethods());
            index.set(0);

            checkValueNeeded = false;
            checkObjectNeeded = false;
            checkArrayNeeded = false;
            checkMapNeeded = false;

            TypeElement clazz = (TypeElement) element.getEnclosingElement();
            String packageName = clazz.getQualifiedName().toString();
            packageName = packageName.substring(0, packageName.lastIndexOf('.'));

            VariableElement variableElement = (VariableElement) element;
            YAPIONObject yapionObject;
            if (yapionAccessGenerator.inline()) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Inline parsing: " + variableElement.getConstantValue().toString());
                yapionObject = YAPIONParser.parse(variableElement.getConstantValue().toString(), new ParseOptions().commentParsing(yapionAccessGenerator.commentParsing()).lazy(yapionAccessGenerator.lazy()));
            } else {
                messager.printMessage(Diagnostic.Kind.NOTE, "File parsing: ./" + variableElement.getConstantValue().toString());
                File file = new File("./" + variableElement.getConstantValue().toString());
                try {
                    yapionObject = YAPIONParser.parse(file, new FileOptions().commentParsing(yapionAccessGenerator.commentParsing()).lazy(yapionAccessGenerator.lazy()));
                } catch (IOException e) {
                    error("Parsing of file '" + file.getAbsolutePath() + "' failed. Please specify a valid path relative from '" + new File(".").getAbsolutePath() + "'");
                    continue;
                }
            }
            if (!yapionObject.containsKey("@name", String.class)) {
                error("Root Element needs to have a proper name. Use '@name' to specify one in the YAPIONObject");
                continue;
            }

            messager.printMessage(Diagnostic.Kind.NOTE, "Generating ObjectContainer for " + element.getSimpleName());
            ObjectContainer objectContainer;
            try {
                objectContainer = new ObjectContainer(yapionObject.getPlainValue("@name"), yapionObject);
            } catch (Exception e) {
                e.printStackTrace();
                error(e.getMessage());
                continue;
            }

            messager.printMessage(Diagnostic.Kind.NOTE, "Generating " + element.getSimpleName());
            ClassGenerator classGenerator = new ClassGenerator(packageName, objectContainer.getClassName());
            classGenerator.addImport(YAPIONParser.class.getTypeName());
            yapionObject.getArrayOrDefault("@imports", new YAPIONArray()).forEach(yapionAnyType -> {
                classGenerator.addImport(((YAPIONValue<String>) yapionAnyType).get());
            });

            if (checkValueNeeded) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Generating checkValue method for " + element.getSimpleName());
                classGenerator.addImport("static yapion.config.annotationproccessing.ParsingUtils.checkValue");
            }

            if (checkObjectNeeded) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Generating checkObject method for " + element.getSimpleName());
                classGenerator.addImport("static yapion.config.annotationproccessing.ParsingUtils.checkObject");
            }

            if (checkArrayNeeded) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Generating checkArray method for " + element.getSimpleName());
                classGenerator.addImport("static yapion.config.annotationproccessing.ParsingUtils.checkArray");
            }

            if (checkMapNeeded) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Generating checkMap method for " + element.getSimpleName());
                classGenerator.addImport("static yapion.config.annotationproccessing.ParsingUtils.checkMap");
            }

            if (lombokExtensionMethod.get()) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Generating extension annotation for " + element.getSimpleName());
                List<String> strings = new ArrayList<>();
                strings.add("yapion.config.annotationproccessing.ConstraintUtils.class");
                yapionObject.getArrayOrDefault("@extensions", new YAPIONArray()).forEach(yapionAnyType -> {
                    String current = ((YAPIONValue<String>) yapionAnyType).get();
                    if (current.endsWith(".class")) {
                        strings.add(current);
                    } else {
                        strings.add(current + ".class");
                    }
                });
                classGenerator.addAnnotation("@lombok.experimental.ExtensionMethod({" + String.join(", ", strings) + "})");
            }
            classGenerator.toString(toString.get());
            messager.printMessage(Diagnostic.Kind.NOTE, "Generating import for 'static yapion.config.annotationproccessing.ConstraintUtils.*'");
            classGenerator.addImport("static yapion.config.annotationproccessing.ConstraintUtils.*");
            try {
                messager.printMessage(Diagnostic.Kind.NOTE, "Writing generated hierarchy to ClassGenerator");
                objectContainer.outputRoot(classGenerator);
            } catch (Exception e) {
                e.printStackTrace();
                error(e.getMessage());
                continue;
            }

            messager.printMessage(Diagnostic.Kind.NOTE, "Writing ClassGenerator to file");
            JavaFileObject f = processingEnv.getFiler().createSourceFile(classGenerator.getPackageName() + "." + classGenerator.getClassName());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Creating " + f.toUri());
            PrintWriter pw = new PrintWriter(f.openWriter());
            classGenerator.output(0).forEach(pw::println);
            pw.close();
        }

        return true;
    }

    private void error(String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                currentElement);
    }

    @ToString
    @Getter
    private abstract class ContainerElement {
        private final String name;

        private boolean nonNull = false;

        public ContainerElement(String name) {
            if (name != null && name.endsWith("!")) {
                nonNull = true;
                name = name.substring(0, name.length() - 1);
            }
            this.name = name;
        }

        public abstract String constructorCall();

        public String constructorCallMap() {
            return constructorCall();
        }

        public abstract void output(ClassGenerator classGenerator);

        public boolean isDefaultPresent() {
            return false;
        }

        public String type() {
            return "";
        }

        public void setHidden() {
            // Ignored
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class ObjectContainer extends ContainerElement {

        private final List<ContainerElement> containerElementList = new ArrayList<>();
        private final String className;
        private boolean hidden;
        private YAPIONObject defaultValue = null;

        public ObjectContainer(String name, YAPIONObject yapionObject) {
            super(name);
            if (yapionObject.containsKey("@name", String.class)) {
                className = yapionObject.getPlainValue("@name");
            } else {
                className = "_" + index.incrementAndGet();
            }
            hidden = yapionObject.getPlainValueOrDefault("@hidden", false);
            checkElements(yapionObject, containerElementList::add);
            if (yapionObject.containsKey("@default", YAPIONType.OBJECT)) {
                defaultValue = yapionObject.getObject("@default");
            }
        }

        private String constructor(boolean toOutput) {
            if (defaultValue == null) {
                return "checkObject(yapionObject.getObject(\"" + getName() + "\"), " + isNonNull() + ", " + getClassName() + "::new, " + (toOutput ? "output::set" : "value -> this." + getName() + " = value") + ");";
            } else {
                return "checkObject(yapionObject.getObjectOrDefault(\"" + getName() + "\", " + createParseStatement(defaultValue.toYAPION()) + "), " + isNonNull() + ", " + getClassName() + "::new, " + (toOutput ? "output::set" : "value -> this." + getName() + " = value") + ");";
            }
        }

        @Override
        public String constructorCall() {
            containerElementList.forEach(containerElement -> {
                if (containerElement.isNonNull() && isDefaultPresent()) {
                    if (!defaultValue.containsKey(containerElement.name)) {
                        error("Default of '" + className + "' does not define value for '" + containerElement.name + "'");
                    }
                }
            });

            if (hidden) {
                return "";
            }
            return constructor(false);
        }

        @Override
        public String constructorCallMap() {
            if (defaultValue != null) {
                return constructor(true).replace("yapionObject.getObjectOrDefault(\"" + getName() + "\", " + createParseStatement(defaultValue.toYAPION()) + ")", YAPIONObject.class.getTypeName() + ".class.isInstance(any) ? (" + YAPIONObject.class.getTypeName() + ") any : " + createParseStatement(defaultValue.toYAPION())).replace("this.", "output");
            } else {
                return constructor(true).replace("yapionObject.getObject(\"" + getName() + "\")", "(" + YAPIONObject.class.getTypeName() + ") any").replace("this.", "output");
            }
        }

        public void outputRoot(ClassGenerator classGenerator) {
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "yapionObject"));
            classGenerator.add(functionGenerator);
            ClassGenerator finalClassGenerator = classGenerator;
            containerElementList.forEach(containerElement -> {
                String constructorCall = containerElement.constructorCall();
                if (!constructorCall.isEmpty()) {
                    for (String s : constructorCall.split("\n")) {
                        functionGenerator.add(s);
                    }
                }
                containerElement.output(finalClassGenerator);
            });
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            if (!hidden) {
                methodsAndField(classGenerator, getName(), type(), isNonNull() || isDefaultPresent());
            }

            ClassGenerator currentGenerator = new ClassGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName());
            classGenerator.toString(toString.get());
            classGenerator.add(currentGenerator);
            classGenerator = currentGenerator;
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "yapionObject"));
            classGenerator.add(functionGenerator);
            ClassGenerator finalClassGenerator = classGenerator;
            containerElementList.forEach(containerElement -> {
                String constructorCall = containerElement.constructorCall();
                if (!constructorCall.isEmpty()) {
                    for (String s : constructorCall.split("\n")) {
                        functionGenerator.add(s);
                    }
                }
                containerElement.output(finalClassGenerator);
            });
        }

        @Override
        public boolean isDefaultPresent() {
            return defaultValue != null;
        }

        @Override
        public String type() {
            return className;
        }

        @Override
        public void setHidden() {
            hidden = true;
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class ObjectReferenceContainer extends ContainerElement {

        private final List<ContainerElement> containerElementList = new ArrayList<>();
        private String className;
        private boolean hidden;
        private final String reference;
        private YAPIONObject defaultValue = null;

        public ObjectReferenceContainer(String name, YAPIONObject yapionObject) {
            super(name);
            if (yapionObject.containsKey("@name", String.class)) {
                className = yapionObject.getPlainValue("@name");
            } else {
                className = "_" + index.incrementAndGet();
            }
            hidden = yapionObject.getPlainValueOrDefault("@hidden", false);
            reference = yapionObject.getPlainValue("@reference");
            checkElements(yapionObject, containerElementList::add);
            if (yapionObject.containsKey("@default", YAPIONType.OBJECT)) {
                defaultValue = yapionObject.getObject("@default");
            }
        }

        public ObjectReferenceContainer(String name, YAPIONValue<String> yapionValue) {
            super(name);
            reference = yapionValue.get().substring(1);
        }

        private String constructor(boolean toOutput) {
            if (containerElementList.isEmpty()) {
                if (defaultValue != null) {
                    return "checkObject(yapionObject.getObjectOrDefault(\"" + getName() + "\", " + createParseStatement(defaultValue.toYAPION()) + "), " + isNonNull() + ", " + reference + "::new, " + (toOutput ? "output::set" : "value -> this." + getName() + " = value") + ");";
                } else {
                    return "checkObject(yapionObject.getObject(\"" + getName() + "\"), " + isNonNull() + ", " + reference + "::new, " + (toOutput ? "output::set" : "value -> this." + getName() + " = value") + ");";
                }
            }
            if (defaultValue != null) {
                return "checkObject(yapionObject.getObjectOrDefault(\"" + getName() + "\", " + createParseStatement(defaultValue.toYAPION()) + "), " + isNonNull() + ", " + getClassName() + "::new, " + (toOutput ? "output::set" : "value -> this." + getName() + " = value") + ");";
            } else {
                return "checkObject(yapionObject.getObject(\"" + getName() + "\"), " + isNonNull() + ", " + getClassName() + "::new, " + (toOutput ? "output::set" : "value -> this." + getName() + " = value") + ");";
            }
        }

        @Override
        public String constructorCall() {
            containerElementList.forEach(containerElement -> {
                if (containerElement.isNonNull() && !containerElement.isDefaultPresent()) {
                    if (!defaultValue.containsKey(containerElement.name)) {
                        error("Default of '" + className + "' does not define value for '" + containerElement.name + "'");
                    }
                }
            });
            if (hidden) {
                return "";
            }
            return constructor(false);
        }

        @Override
        public String constructorCallMap() {
            if (defaultValue != null) {
                return constructor(true).replace("yapionObject.getObjectOrDefault(\"" + getName() + "\", " + createParseStatement(defaultValue.toYAPION()) + ")", YAPIONObject.class.getTypeName() + ".class.isInstance(any) ? (" + YAPIONObject.class.getTypeName() + ") any : " + createParseStatement(defaultValue.toYAPION()));
            } else {
                return constructor(true).replace("yapionObject.getObject(\"" + getName() + "\")", "(" + YAPIONObject.class.getTypeName() + ") any");
            }
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            if (!hidden) {
                methodsAndField(classGenerator, getName(), type(), isNonNull() || isDefaultPresent());
            }
            if (containerElementList.isEmpty()) {
                return;
            }

            ClassGenerator currentGenerator = new ClassGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName());
            classGenerator.toString(toString.get());
            currentGenerator.setExtendsString(reference);
            classGenerator.add(currentGenerator);
            classGenerator = currentGenerator;
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "yapionObject"));
            functionGenerator.add("super(yapionObject);");
            classGenerator.add(functionGenerator);
            ClassGenerator finalClassGenerator = classGenerator;
            containerElementList.forEach(containerElement -> {
                String constructorCall = containerElement.constructorCall();
                if (!constructorCall.isEmpty()) {
                    for (String s : constructorCall.split("\n")) {
                        functionGenerator.add(s);
                    }
                }
                containerElement.output(finalClassGenerator);
            });
        }

        @Override
        public boolean isDefaultPresent() {
            return defaultValue != null;
        }

        @Override
        public String type() {
            if (containerElementList.isEmpty()) {
                return reference;
            } else {
                return className;
            }
        }

        @Override
        public void setHidden() {
            hidden = true;
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class ArrayReferenceContainer extends ContainerElement {

        private final List<ContainerElement> containerElementList = new ArrayList<>();
        private String className;
        private boolean hidden;
        private final String reference;

        public ArrayReferenceContainer(String name, YAPIONObject yapionObject) {
            super(name);
            if (yapionObject.containsKey("@name", String.class)) {
                className = yapionObject.getPlainValue("@name");
            } else {
                className = "_" + index.incrementAndGet();
            }
            hidden = yapionObject.getPlainValueOrDefault("@hidden", false);
            reference = yapionObject.getPlainValue("@arrayReference");
            checkElements(yapionObject, containerElementList::add);
        }

        public ArrayReferenceContainer(String name, YAPIONValue<String> yapionValue) {
            super(name);
            reference = yapionValue.get().substring(1, yapionValue.get().length() - 2);
        }

        @Override
        public String constructorCall() {
            if (hidden) {
                return "";
            }
            if (containerElementList.isEmpty()) {
                return "checkArray(yapionObject.getArray(\"" + getName() + "\"), " + isNonNull() + ", " + YAPIONObject.class.getTypeName() + ".class, " + reference + ".class, " + reference + "::new, null, value -> this." + getName() + " = value);";
            }
            return "checkArray(yapionObject.getArray(\"" + getName() + "\"), " + isNonNull() + ", " + YAPIONObject.class.getTypeName() + ".class, " + className + ".class, " + className + "::new, null, value -> this." + getName() + " = value);";
        }

        @Override
        public String constructorCallMap() {
            if (containerElementList.isEmpty()) {
                return "checkArray((" + YAPIONArray.class.getTypeName() + ") any, " + isNonNull() + ", " + YAPIONObject.class.getTypeName() + ".class, " + reference + ".class, " + reference + "::new, null, output::set);";
            }
            return "checkArray((" + YAPIONArray.class.getTypeName() + ") any, " + isNonNull() + ", " + YAPIONObject.class.getTypeName() + ".class, " + className + ".class, " + className + "::new, null, output::set);";
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            if (!hidden) {
                methodsAndField(classGenerator, getName(), type(), isNonNull());
            }
            if (containerElementList.isEmpty()) {
                return;
            }

            ClassGenerator currentGenerator = new ClassGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName());
            classGenerator.toString(toString.get());
            currentGenerator.setExtendsString(reference);
            classGenerator.add(currentGenerator);
            classGenerator = currentGenerator;
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "yapionObject"));
            functionGenerator.add("super(yapionObject);");
            classGenerator.add(functionGenerator);
            ClassGenerator finalClassGenerator = classGenerator;
            containerElementList.forEach(containerElement -> {
                String constructorCall = containerElement.constructorCall();
                if (!constructorCall.isEmpty()) {
                    for (String s : constructorCall.split("\n")) {
                        functionGenerator.add(s);
                    }
                }
                containerElement.output(finalClassGenerator);
            });
        }

        @Override
        public String type() {
            if (containerElementList.isEmpty()) {
                return "java.util.List<" + reference + ">";
            } else {
                return "java.util.List<" + className + ">";
            }
        }

        @Override
        public void setHidden() {
            hidden = true;
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class ArrayContainer extends ContainerElement {

        private boolean hidden;
        private final String type;
        private String constraints;

        public ArrayContainer(String name, YAPIONObject yapionObject) {
            super(name);
            hidden = yapionObject.getPlainValueOrDefault("@hidden", false);
            type = yapionObject.getPlainValueOrDefault("@arrayType", "OBJECT");
            constraints = yapionObject.getPlainValueOrDefault("constraints", "ignored -> true");
            constraints = Arrays.stream(constraints.split("\n")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.joining("\n"));
        }

        public ArrayContainer(String name, YAPIONValue<String> yapionValue) {
            super(name);
            type = yapionValue.get().substring(0, yapionValue.get().length() - 2);
            constraints = "ignored -> true";
        }

        @Override
        public String constructorCall() {
            if (hidden) {
                return "";
            }
            return "checkArray(yapionObject.getArray(\"" + getName() + "\"), " + isNonNull() + ", " + YAPIONValue.class.getTypeName() + ".class, " + toClass(type).getTypeName() + ".class, value -> (" + toClass(type).getTypeName() + ") value.get(), " + constraints + ", value -> this." + getName() + " = value);";
        }

        @Override
        public String constructorCallMap() {
            return "checkArray((" + YAPIONArray.class.getTypeName() + ") any, " + isNonNull() + ", " + YAPIONValue.class.getTypeName() + ".class, " + toClass(type).getTypeName() + ".class, value -> (" + toClass(type).getTypeName() + ") value.get(), " + constraints + ", output::set);";
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            if (!hidden) {
                methodsAndField(classGenerator, getName(), type(), isNonNull());
            }
        }

        @Override
        public String type() {
            return "java.util.List<" + toClass(type).getTypeName() + ">";
        }

        @Override
        public void setHidden() {
            hidden = true;
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class MapContainer extends ContainerElement {

        private final String keyConstraints;
        private ContainerElement containerElement;

        public MapContainer(String name, YAPIONObject yapionObject) {
            super(name);
            keyConstraints = yapionObject.getPlainValueOrDefault("@keys", "ignored -> true");
            checkElement("", yapionObject.getAnyType("@value"), containerElement -> {
                MapContainer.this.containerElement = containerElement;
            });
            if (containerElement instanceof MapContainer) {
                error("No Map directly inside another map");
            }
            containerElement.setHidden();
        }

        @Override
        public String constructorCall() {
            return "checkMap(yapionObject, " + keyConstraints + ", " + isNonNull() + ", " + containerElement.type() + ".class, any -> {\n    java.util.concurrent.atomic.AtomicReference<" + containerElement.type() + "> output = new java.util.concurrent.atomic.AtomicReference<>(null);\n    " + containerElement.constructorCallMap() + "\n    return output.get();\n}, value -> this." + getName() + " = value);";
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            methodsAndField(classGenerator, getName(), type(), isNonNull());
            if (!(containerElement instanceof ValueContainer)) {
                containerElement.output(classGenerator);
            }
        }

        @Override
        public String type() {
            return "java.util.Map<java.lang.String, " + containerElement.type() + ">";
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class ValueContainer extends ContainerElement {

        private final String type;
        private String constraints;
        private Optional<String> defaultValue = Optional.empty();

        public ValueContainer(String name, YAPIONObject yapionObject) {
            super(name);
            type = yapionObject.getPlainValueOrDefault("@type", "OBJECT");
            constraints = yapionObject.getPlainValueOrDefault("constraints", "ignored -> true");
            constraints = Arrays.stream(constraints.split("\n")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.joining("\n"));

            if (yapionObject.containsKey("default", YAPIONType.VALUE)) {
                Object object = yapionObject.getValue("default").get();
                if (object.getClass() != toClass(type)) {
                    error("The type of the default value did not match the given variable type");
                    return;
                }
                if (object instanceof String s) {
                    defaultValue = Optional.of("\"" + s + "\"");
                } else if (object instanceof Byte b) {
                    defaultValue = Optional.of("(byte) " + b);
                } else if (object instanceof Short s) {
                    defaultValue = Optional.of("(short) " + s);
                } else if (object instanceof Integer i) {
                    defaultValue = Optional.of("" + i);
                } else if (object instanceof Long l) {
                    defaultValue = Optional.of("" + l + "L");
                } else if (object instanceof BigInteger bi) {
                    defaultValue = Optional.of("new " + BigInteger.class.getTypeName() + "(" + bi + ")");
                } else if (object instanceof Float f) {
                    defaultValue = Optional.of("" + f + "F");
                } else if (object instanceof Double d) {
                    defaultValue = Optional.of("" + d + "D");
                } else if (object instanceof BigDecimal bd) {
                    defaultValue = Optional.of("new " + BigDecimal.class.getTypeName() + "(" + bd + ")");
                } else if (object instanceof Character c) {
                    defaultValue = Optional.of("'" + c + "'");
                } else if (object instanceof Boolean b) {
                    defaultValue = Optional.of("" + b);
                } else {
                    defaultValue = Optional.of("null");
                }
            }
        }

        public ValueContainer(String name, YAPIONValue<String> yapionValue) {
            super(name);
            type = yapionValue.get();
            constraints = "ignored -> true";
        }

        @Override
        public String constructorCall() {
            return "checkValue(yapionObject.getValue(\"" + getName() + "\"), " + isNonNull() + ", " + type() + ".class, " + (defaultValue.map(s -> "() -> " + s).orElse("null")) + ", value -> this." + getName() + " = value, " + constraints + ");";
        }

        @Override
        public String constructorCallMap() {
            return "checkValue(" + YAPIONValue.class.getTypeName() + ".class.isInstance(any) ? (" + YAPIONValue.class.getTypeName() + "<?>) any : null, " + isNonNull() + ", " + type() + ".class, " + (defaultValue.map(s -> "() -> " + s).orElse("null")) + ", output::set, " + constraints + ");";
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            methodsAndField(classGenerator, getName(), toClass(type).getTypeName(), isNonNull() || isDefaultPresent());
        }

        @Override
        public boolean isDefaultPresent() {
            return defaultValue.isPresent();
        }

        @Override
        public String type() {
            return toClass(type).getTypeName();
        }
    }

    public void checkElements(YAPIONObject yapionObject, Consumer<ContainerElement> resultConsumer) {
        yapionObject.forEach((s, yapionAnyType) -> {
            if (s.startsWith("@")) {
                return;
            }
            checkElement(s, yapionAnyType, resultConsumer);
        });
    }

    public void checkElement(String s, YAPIONAnyType yapionAnyType, Consumer<ContainerElement> resultConsumer) {
        if (yapionAnyType instanceof YAPIONObject object) {
            if (object.containsKey("@type", String.class)) {
                resultConsumer.accept(new ValueContainer(s, object));
                checkValueNeeded = true;
            } else if (object.containsKey("@arrayType", String.class)) {
                resultConsumer.accept(new ArrayContainer(s, object));
                checkArrayNeeded = true;
            } else if (object.containsKey("@reference", String.class)) {
                resultConsumer.accept(new ObjectReferenceContainer(s, object));
                checkObjectNeeded = true;
            } else if (object.containsKey("@arrayReference", String.class)) {
                resultConsumer.accept(new ArrayReferenceContainer(s, object));
                checkArrayNeeded = true;
            } else if (object.containsKey("@value")) {
                resultConsumer.accept(new MapContainer(s, object));
                checkMapNeeded = true;
            } else {
                resultConsumer.accept(new ObjectContainer(s, object));
                checkObjectNeeded = true;
            }
        } else if (yapionAnyType instanceof YAPIONValue value) {
            String v = value.get().toString();
            if (v.endsWith("[]")) {
                if (v.startsWith("@")) {
                    resultConsumer.accept(new ArrayReferenceContainer(s, value));
                    checkArrayNeeded = true;
                } else {
                    resultConsumer.accept(new ArrayContainer(s, value));
                    checkArrayNeeded = true;
                }
            } else {
                if (v.startsWith("@")) {
                    resultConsumer.accept(new ObjectReferenceContainer(s, value));
                    checkObjectNeeded = true;
                } else {
                    resultConsumer.accept(new ValueContainer(s, value));
                    checkValueNeeded = true;
                }
            }
        } else {
            error("Only YAPIONObjects are allowed to specify types: {}", yapionAnyType);
        }
    }

    public static String toCamelCase(String in) {
        if (in.isEmpty()) return in;
        return "" + Character.toTitleCase(in.charAt(0)) + in.substring(1);
    }

    public static Class<?> toClass(String type) {
        return switch (type.toLowerCase()) {
            case "string" -> String.class;
            case "byte" -> Byte.class;
            case "short" -> Short.class;
            case "int", "integer" -> Integer.class;
            case "long" -> Long.class;
            case "biginteger" -> BigInteger.class;
            case "float" -> Float.class;
            case "double" -> Double.class;
            case "bigdecimal" -> BigDecimal.class;
            case "char", "character" -> Character.class;
            case "bool", "boolean" -> Boolean.class;
            default -> Object.class;
        };
    }

    public void methodsAndField(ClassGenerator classGenerator, String name, String type, boolean nonNull) {
        classGenerator.add(new FieldGenerator(new ModifierGenerator(ModifierType.PRIVATE), type, name, null));

        if (!nonNull) {
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "is" + toCamelCase(name) + "Present", "boolean");
            functionGenerator.add("return " + name + " != null;");
            classGenerator.add(functionGenerator);
        }

        FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "get" + toCamelCase(name), type);
        functionGenerator.add("return " + name + ";");
        classGenerator.add(functionGenerator);

        if (setter.get()) {
            functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "set" + toCamelCase(name), void.class, new ParameterGenerator(type, name));
            functionGenerator.add("this." + name + " = " + name + ";");
            classGenerator.add(functionGenerator);
        }
    }

    public String createParseStatement(String s) {
        return "YAPIONParser.parse(\"" + s.replace("\"", "\\\"") + "\")";
    }
}
