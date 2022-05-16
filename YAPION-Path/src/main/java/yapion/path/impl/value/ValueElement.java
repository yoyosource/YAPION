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

package yapion.path.impl.value;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.path.PathElement;

public interface ValueElement extends PathElement {

    default boolean check(Object object) {
        return true;
    }

    @Override
    default boolean check(YAPIONAnyType yapionAnyType) {
        if (!(yapionAnyType instanceof YAPIONValue yapionValue)) {
            return false;
        }
        return check(yapionValue.get());
    }
}
