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

package yapion.path.elements;

import lombok.AllArgsConstructor;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.path.PathElement;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Element implements PathElement {

    private String identifier;
    private Integer intIdentifier;

    public Element(String identifier) {
        this.identifier = identifier;
        try {
            intIdentifier = Integer.parseInt(identifier);
        } catch (NumberFormatException e) {
            // Ignored
        }
    }

    @Override
    public boolean check(YAPIONAnyType element) {
        if (element instanceof YAPIONObject yapionObject) {
            return yapionObject.containsKey(identifier);
        } else if (element instanceof YAPIONArray yapionArray && intIdentifier != null) {
            return yapionArray.containsKey(intIdentifier);
        }
        return false;
    }

    @Override
    public List<YAPIONAnyType> apply(List<YAPIONAnyType> current, PathElement possibleNext) {
        List<YAPIONAnyType> result = new ArrayList<>();
        for (YAPIONAnyType element : current) {
            if (element instanceof YAPIONObject yapionObject) {
                yapionObject.getAny(identifier, result::add, () -> {});
            } else if (element instanceof YAPIONArray yapionArray && intIdentifier != null) {
                yapionArray.getAny(intIdentifier, result::add, () -> {});
            }
        }
        return result;
    }
}
