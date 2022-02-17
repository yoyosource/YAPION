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

package yapion.hierarchy.output.flavours;

import yapion.hierarchy.output.AbstractOutput;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ThunderFileFlavour implements Flavour {

    private static Set<HierarchyTypes> unsupportedTypesSet = new HashSet<>();

    static {
        unsupportedTypesSet.add(HierarchyTypes.MAP);
        unsupportedTypesSet.add(HierarchyTypes.POINTER);
        unsupportedTypesSet = Collections.unmodifiableSet(unsupportedTypesSet);
    }

    @Override
    public Set<HierarchyTypes> unsupportedTypes() {
        return unsupportedTypesSet;
    }

    @Override
    public PrettifyBehaviour getPrettifyBehaviour() {
        return PrettifyBehaviour.ALWAYS;
    }

    @Override
    public boolean removeRootObject() {
        return true;
    }

    @Override
    public void begin(HierarchyTypes hierarchyTypes, AbstractOutput output) {
        switch (hierarchyTypes) {
            case OBJECT:
                output.consume("{");
                break;
            case ARRAY:
                output.consume("[");
                break;
            case COMMENT:
                output.consume("# ");
                break;
        }
    }

    @Override
    public void end(HierarchyTypes hierarchyTypes, AbstractOutput output) {
        switch (hierarchyTypes) {
            case OBJECT:
                output.consume("}");
                break;
            case ARRAY:
                output.consume("]");
                break;
        }
    }

    @Override
    public void beginElement(HierarchyTypes hierarchyTypes, AbstractOutput output, ElementData elementData) {
        switch (hierarchyTypes) {
            case OBJECT:
                output.consume(elementData.getName()).consume(" = ");
                break;
            case ARRAY:
                output.consume("- ");
                break;
        }
    }

    @Override
    public <T> void elementValue(HierarchyTypes hierarchyTypes, AbstractOutput output, ValueData<T> valueData) {
        if (valueData.getValue() == null) {
            output.consume("null");
        } else {
            output.consume(valueData.getValue().toString());
        }
    }
}
