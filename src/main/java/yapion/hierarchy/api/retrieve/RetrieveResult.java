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

package yapion.hierarchy.api.retrieve;

import lombok.NonNull;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.*;

public class RetrieveResult {

    private YAPIONAnyType[] yapionAnyTypes;

    RetrieveResult(YAPIONAnyType... yapionAnyTypes) {
        this.yapionAnyTypes = yapionAnyTypes;
    }

    public int size() {
        return yapionAnyTypes.length;
    }

    public int length() {
        return yapionAnyTypes.length;
    }

    public YAPIONType getType(int index) {
        return yapionAnyTypes[index].getType();
    }

    public YAPIONAnyType getRaw(int index) {
        return yapionAnyTypes[index];
    }

    @SuppressWarnings({"unchecked"})
    public <T extends YAPIONAnyType> T get(int index) {
        return (T) yapionAnyTypes[index];
    }

    public YAPIONObject getObject(int index) {
        return get(index);
    }

    public YAPIONArray getArray(int index) {
        return get(index);
    }

    public YAPIONMap getMap(int index) {
        return get(index);
    }

    public YAPIONPointer getPointer(int index) {
        return get(index);
    }

    public <T> YAPIONValue<T> getValue(int index) {
        return get(index);
    }

}
