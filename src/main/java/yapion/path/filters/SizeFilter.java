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

package yapion.path.filters;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;

import java.util.function.Predicate;

public abstract class SizeFilter implements Predicate<YAPIONAnyType> {

    private Predicate<Integer> sizeCheck;

    public SizeFilter(Predicate<Integer> sizeCheck) {
        this.sizeCheck = sizeCheck;
    }

    @Override
    public boolean test(YAPIONAnyType element) {
        if (element instanceof YAPIONObject yapionObject) {
            return sizeCheck.test(yapionObject.getKeys().size());
        } else if (element instanceof YAPIONArray yapionArray) {
            return sizeCheck.test(yapionArray.length());
        }
        return false;
    }
}
