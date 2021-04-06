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

package yapion.hierarchy.types.value;

import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

public interface ValueHandler<T> {

    boolean allowed(char c, int length);

    String output(T t);

    MethodReturnValue<T> preParse(String s);

    MethodReturnValue<T> parse(String s);

    long referenceValue(ReferenceFunction referenceFunction);

}
