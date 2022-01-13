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

package yapion.hierarchy.output.flavours2;

import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.types.YAPIONType;
import yapion.hierarchy.types.value.ValueUtils;
import yapion.path.YAPIONPath;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class JSONFlavour implements Flavour {

    private static Set<HierarchyTypes> unsupportedTypes = new HashSet<>();

    static {
        unsupportedTypes.add(HierarchyTypes.COMMENT);
        unsupportedTypes.add(HierarchyTypes.POINTER);
        unsupportedTypes.add(HierarchyTypes.MAP);
        unsupportedTypes = Collections.unmodifiableSet(unsupportedTypes);
    }

    @Override
    public Set<HierarchyTypes> unsupportedTypes() {
        return unsupportedTypes;
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
    public void beginElement(HierarchyTypes hierarchyTypes, AbstractOutput output, String name, Supplier<YAPIONPath> yapionPathSupplier) {
        output.consume("\"" + name + "\":");
        output.consumePrettified(" ");
    }

    @Override
    public void elementSeparator(HierarchyTypes hierarchyTypes, AbstractOutput output, boolean last) {
        if (!last) {
            output.consume(",");
        }
    }

    @Override
    public <T> void elementValue(HierarchyTypes hierarchyTypes, AbstractOutput output, ValueData<T> valueData) {
        String s = valueData.getWrappedValue().getValueHandler().output(valueData.getValue(), YAPIONType.VALUE);
        if (valueData.getValue() instanceof String || valueData.getValue() instanceof Character) {
            s = "\"" + valueData.getValue().toString() + "\"";
        }
        output.consume(ValueUtils.stringToUTFEscapedString(s, ValueUtils.EscapeCharacters.JSON));
    }
}
