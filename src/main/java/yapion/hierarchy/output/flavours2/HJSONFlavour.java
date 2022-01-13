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
import yapion.path.YAPIONPath;

import java.util.function.Supplier;

public class HJSONFlavour extends JSON5Flavour {

    @Override
    public PrettifyBehaviour getPrettifyBehaviour() {
        return PrettifyBehaviour.ALWAYS;
    }

    @Override
    public void beginElement(HierarchyTypes hierarchyTypes, AbstractOutput output, String name, Supplier<YAPIONPath> yapionPathSupplier) {
        output.consume(name);
        output.consume(": ");
    }

    @Override
    public <T> void elementValue(HierarchyTypes hierarchyTypes, AbstractOutput output, ValueData<T> valueData) {
        String s = valueData.getWrappedValue().getValueHandler().output(valueData.getValue(), YAPIONType.VALUE);
        if (s.startsWith(" ") || s.startsWith("{") || s.startsWith("[") || s.startsWith("]") || s.startsWith("}") || s.startsWith(",") || s.startsWith(":") || s.contains("\n") || s.contains("\r")) {
            output.consume("\"");
            output.consume(s);
            output.consume("\"");
        } else {
            output.consume(s);
        }
    }
}
