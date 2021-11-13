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

package yapion.parser.callbacks;

import yapion.hierarchy.types.*;

public class CallbackType<T> {
    public static final CallbackType<YAPIONObject> OBJECT = new CallbackType<>(YAPIONObject.class);
    public static final CallbackType<YAPIONArray> ARRAY = new CallbackType<>(YAPIONArray.class);
    public static final CallbackType<YAPIONMap> MAP = new CallbackType<>(YAPIONMap.class);
    public static final CallbackType<YAPIONValue> VALUE = new CallbackType<>(YAPIONValue.class);
    public static final CallbackType<YAPIONPointer> POINTER = new CallbackType<>(YAPIONPointer.class);
    public static final CallbackType<String> COMMENT = new CallbackType<>(String.class);

    private Class<T> clazz;

    private CallbackType(Class<T> clazz) {
        this.clazz = clazz;
    }
}
