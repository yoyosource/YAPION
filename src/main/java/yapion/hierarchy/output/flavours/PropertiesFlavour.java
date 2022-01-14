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
import yapion.hierarchy.types.YAPIONElementPath;
import yapion.hierarchy.types.YAPIONType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PropertiesFlavour implements Flavour {

    private static Set<HierarchyTypes> unsupportedTypes = new HashSet<>();

    static {
        unsupportedTypes.add(HierarchyTypes.MAP);
        unsupportedTypes.add(HierarchyTypes.POINTER);
        unsupportedTypes = Collections.unmodifiableSet(unsupportedTypes);
    }

    @Override
    public Set<HierarchyTypes> unsupportedTypes() {
        return unsupportedTypes;
    }

    @Override
    public PrettifyBehaviour getPrettifyBehaviour() {
        return PrettifyBehaviour.NEVER;
    }

    @Override
    public boolean removeRootObject() {
        return true;
    }

    @Override
    public void begin(HierarchyTypes hierarchyTypes, AbstractOutput output) {
        if (hierarchyTypes == HierarchyTypes.COMMENT) {
            output.consume("\n");
            output.consume("# ");
        }
    }

    @Override
    public void beginElement(HierarchyTypes hierarchyTypes, AbstractOutput output, ElementData elementData) {
        if (elementData.getFollowingYAPIONType() != YAPIONType.VALUE) {
            return;
        }
        output.consume("\n");
        YAPIONElementPath yapionElementPath = elementData.getYapionPathSupplier().get();
        output.consume(yapionElementPath.join("_"));
        if (yapionElementPath.depth() != 0) {
            output.consume("_");
        }
        output.consume(elementData.getName());
        output.consume(" = ");
    }

    @Override
    public <T> void elementValue(HierarchyTypes hierarchyTypes, AbstractOutput output, ValueData<T> valueData) {
        output.consume(valueData.getValue().toString());
    }
}
