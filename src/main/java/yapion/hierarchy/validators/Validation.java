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

import yapion.hierarchy.types.YAPIONValue;

@FunctionalInterface
public interface Validation {

    /**
     * This method should return true if the inputted
     * {@link YAPIONValue} value is considered to be valid.
     *
     * @param o the value of the {@link YAPIONValue}
     * @return {@code true} if valid, {@code false} otherwise
     */
    boolean validate(Object o);

}
