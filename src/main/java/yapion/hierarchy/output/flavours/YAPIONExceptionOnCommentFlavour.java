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
import yapion.hierarchy.types.YAPIONType;
import yapion.hierarchy.types.value.ValueUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class YAPIONExceptionOnCommentFlavour implements Flavour {

    private static Set<HierarchyTypes> unsupportedTypesSet = new HashSet<>();

    static {
        unsupportedTypesSet.add(HierarchyTypes.COMMENT);
        unsupportedTypesSet = Collections.unmodifiableSet(unsupportedTypesSet);
    }

    @Override
    public Set<HierarchyTypes> unsupportedTypes() {
        return unsupportedTypesSet;
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
            case MAP:
                output.consume("<");
                break;
            case VALUE:
                output.consume("(");
                break;
            case POINTER:
                output.consume("->");
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
            case MAP:
                output.consume(">");
                break;
            case VALUE:
                output.consume(")");
                break;
        }
    }

    @Override
    public void beginElement(HierarchyTypes hierarchyTypes, AbstractOutput output, ElementData elementData) {
        if (hierarchyTypes == HierarchyTypes.OBJECT) {
            if (elementData.getName().startsWith(" ") || elementData.getName().startsWith(",")) {
                output.consume("\\" + ValueUtils.stringToUTFEscapedString(elementData.getName(), ValueUtils.EscapeCharacters.KEY));
            } else {
                output.consume(ValueUtils.stringToUTFEscapedString(elementData.getName(), ValueUtils.EscapeCharacters.KEY));
            }
        }
    }

    @Override
    public void elementSeparator(HierarchyTypes hierarchyTypes, AbstractOutput output, boolean afterLast) {
        switch (hierarchyTypes) {
            case ARRAY:
                if (afterLast) {
                    output.consumePrettified(",");
                } else {
                    output.consume(",");
                }
                break;
            case MAP:
                output.consume(":");
                break;
        }
    }

    @Override
    public <T> void elementValue(HierarchyTypes hierarchyTypes, AbstractOutput output, ValueData<T> valueData) {
        switch (hierarchyTypes) {
            case POINTER:
                output.consume(valueData.getValue().toString());
                break;
            case VALUE:
                output.consume(valueData.getWrappedValue().getValueHandler().output(valueData.getValue(), YAPIONType.VALUE));
                break;
            case ARRAY:
                String string = valueData.getWrappedValue().getValueHandler().output(valueData.getValue(), YAPIONType.ARRAY);
                if (string.startsWith(" ") || string.startsWith("-")) {
                    string = "\\" + string;
                }
                if (ValueUtils.startsWith(string, ValueUtils.EscapeCharacters.KEY) || string.isEmpty()) {
                    begin(HierarchyTypes.VALUE, output);
                    output.consume(string);
                    end(HierarchyTypes.VALUE, output);
                } else {
                    output.consume(string);
                }
                break;
        }
    }
}
