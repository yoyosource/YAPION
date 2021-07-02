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

package yapion.serializing.annotationproccessing.generator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yapion.hierarchy.output.Indentator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class FunctionGenerator {
    private List<String> annotations = new ArrayList<>();
    private ModifierGenerator modifierGenerator = new ModifierGenerator();
    private final String name;
    private final String type;
    private List<ParameterGenerator> parameterGenerators = new ArrayList<>();
    private List<String> lines = new ArrayList<>();

    public FunctionGenerator(ModifierGenerator modifierGenerator, String name, Class<?> type, ParameterGenerator... parameterGenerators) {
        this.modifierGenerator = modifierGenerator;
        this.name = name;
        this.type = type.getTypeName();
        this.parameterGenerators = new ArrayList<>(Arrays.asList(parameterGenerators));
    }

    public FunctionGenerator(ModifierGenerator modifierGenerator, String name, String type, ParameterGenerator... parameterGenerators) {
        this.modifierGenerator = modifierGenerator;
        this.name = name;
        this.type = type;
        this.parameterGenerators = new ArrayList<>(Arrays.asList(parameterGenerators));
    }

    public FunctionGenerator(String name, String type, ParameterGenerator... parameterGenerators) {
        this.name = name;
        this.type = type;
        this.parameterGenerators = new ArrayList<>(Arrays.asList(parameterGenerators));
    }

    public FunctionGenerator addAnnotation(Class<? extends Annotation> type) {
        annotations.add("@" + type.getTypeName());
        return this;
    }

    public FunctionGenerator add(String line) {
        lines.add(line);
        return this;
    }

    public List<String> output(int indent) {
        List<String> strings = new ArrayList<>();
        annotations.forEach(s -> {
            strings.add(Indentator.QUAD_SPACE.indent(indent) + s);
        });
        strings.add(Indentator.QUAD_SPACE.indent(indent) + modifierGenerator.output() + type + (name != null ? " " + name : "") + "(" + parameterGenerators.stream().map(ParameterGenerator::output).collect(Collectors.joining(", ")) + ") {");
        lines.forEach(s -> strings.add(Indentator.QUAD_SPACE.indent(indent + 1) + s));
        strings.add(Indentator.QUAD_SPACE.indent(indent) + "}");
        return strings;
    }
}
