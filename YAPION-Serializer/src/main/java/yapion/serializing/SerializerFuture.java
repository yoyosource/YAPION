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

package yapion.serializing;

import lombok.*;

import java.util.function.BiFunction;

@NoArgsConstructor
@ToString
class SerializerFuture {

    @Setter(AccessLevel.PACKAGE)
    private BiFunction<Integer, Integer, Class<?>> classLoader;

    @Setter(AccessLevel.PACKAGE)
    private int start;

    @Setter(AccessLevel.PACKAGE)
    private int length;

    @Setter(AccessLevel.PACKAGE)
    @Getter
    private String name;

    private Class<?> clazz = null;

    public Class<?> get() {
        if (clazz == null) {
            clazz = classLoader.apply(start, length);
        }
        return clazz;
    }
}
