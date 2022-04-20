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

package yapion.utils;

import lombok.RequiredArgsConstructor;
import yapion.annotations.api.InternalAPI;

import java.util.function.ToLongFunction;

/**
 * You could use your own {@code ReferenceFunction} but it is not recommended as you need
 * to use the ReferenceFunction everywhere {@link yapion.hierarchy.types.YAPIONPointer}'s
 * are encountered. So you need to use your own implementation while parsing in
 * {@link yapion.parser.YAPIONParser} as well as {@link yapion.serializing.YAPIONSerializer}.
 */
@RequiredArgsConstructor
@InternalAPI
public class ReferenceFunction {

    private final ToLongFunction<String> reference;

    public long stringToReferenceValue(String s) {
        return reference.applyAsLong(s);
    }
}
