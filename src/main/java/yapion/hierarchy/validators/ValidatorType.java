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

package yapion.hierarchy.validators;

import yapion.annotations.api.DeprecationInfo;
import yapion.hierarchy.types.YAPIONType;

@Deprecated
@DeprecationInfo(since = "0.25.3")
public enum ValidatorType {

    ANY(YAPIONType.ANY),
    OBJECT(YAPIONType.OBJECT),
    ARRAY(YAPIONType.ARRAY),
    MAP(YAPIONType.MAP),
    POINTER(YAPIONType.POINTER),
    VALUE(YAPIONType.VALUE);

    private YAPIONType type;

    ValidatorType(YAPIONType type) {
        this.type = type;
    }

    YAPIONType getType() {
        return type;
    }

    static ValidatorType getByYAPIONType(YAPIONType yapionType) {
        switch (yapionType) {
            case ANY:
                return ANY;
            case ARRAY:
                return ARRAY;
            case MAP:
                return MAP;
            case OBJECT:
                return OBJECT;
            case POINTER:
                return POINTER;
            case VALUE:
                return VALUE;
        }
        return null;
    }

}
