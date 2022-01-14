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

import lombok.AllArgsConstructor;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.types.YAPIONElementPath;

import java.util.Set;
import java.util.function.Supplier;

@AllArgsConstructor
public class CheckingFlavour implements Flavour {

    private final Flavour flavour;

    @Override
    public Set<HierarchyTypes> unsupportedTypes() {
        return flavour.unsupportedTypes();
    }

    @Override
    public void checkType(HierarchyTypes hierarchyTypes) {
        flavour.checkType(hierarchyTypes);
    }

    @Override
    public PrettifyBehaviour getPrettifyBehaviour() {
        return flavour.getPrettifyBehaviour();
    }

    @Override
    public boolean removeRootObject() {
        return flavour.removeRootObject();
    }

    @Override
    public void header(AbstractOutput output) {
        flavour.header(output);
    }

    @Override
    public void footer(AbstractOutput output) {
        flavour.footer(output);
    }

    @Override
    public void begin(HierarchyTypes hierarchyTypes, AbstractOutput output) {
        checkType(hierarchyTypes);
        flavour.begin(hierarchyTypes, output);
    }

    @Override
    public void end(HierarchyTypes hierarchyTypes, AbstractOutput output) {
        checkType(hierarchyTypes);
        flavour.end(hierarchyTypes, output);
    }

    @Override
    public void beginElement(HierarchyTypes hierarchyTypes, AbstractOutput output, ElementData elementData) {
        checkType(hierarchyTypes);
        flavour.beginElement(hierarchyTypes, output, elementData);
    }

    @Override
    public void endElement(HierarchyTypes hierarchyTypes, AbstractOutput output, ElementData elementData) {
        checkType(hierarchyTypes);
        flavour.endElement(hierarchyTypes, output, elementData);
    }

    @Override
    public void elementSeparator(HierarchyTypes hierarchyTypes, AbstractOutput output, boolean afterLast) {
        checkType(hierarchyTypes);
        flavour.elementSeparator(hierarchyTypes, output, afterLast);
    }

    @Override
    public <T> void elementValue(HierarchyTypes hierarchyTypes, AbstractOutput output, ValueData<T> valueData) {
        checkType(hierarchyTypes);
        flavour.elementValue(hierarchyTypes, output, valueData);
    }
}
