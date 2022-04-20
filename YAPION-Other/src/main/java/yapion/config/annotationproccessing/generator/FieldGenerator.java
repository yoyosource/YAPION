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
import yapion.hierarchy.output.Indentator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class FieldGenerator {
    private List<String> annotations = new ArrayList<>();
    private ModifierGenerator modifierGenerator = new ModifierGenerator(ModifierType.PRIVATE);
    private final String type;
    private final String name;
    private final String defaultValue;

    public FieldGenerator(Class<?> type, String name, String defaultValue) {
        this.type = type.getTypeName();
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public FieldGenerator(ModifierGenerator modifierGenerator, Class<?> type, String name, String defaultValue) {
        this.modifierGenerator = modifierGenerator;
        this.type = type.getTypeName();
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public FieldGenerator(ModifierGenerator modifierGenerator, String type, String name, String defaultValue) {
        this.modifierGenerator = modifierGenerator;
        this.type = type;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public FieldGenerator addAnnotation(Class<? extends Annotation> type) {
        annotations.add("@" + type.getTypeName());
        return this;
    }

    public List<String> output(int indent) {
        List<String> strings = new ArrayList<>();
        annotations.forEach(s -> {
            strings.add(Indentator.QUAD_SPACE.indent(indent) + s);
        });
        strings.add(Indentator.QUAD_SPACE.indent(indent) + modifierGenerator.output() + type + " " + name + (defaultValue != null ? " = " + defaultValue : "") + ";");
        return strings;
    }
}
