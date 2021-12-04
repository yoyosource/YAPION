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

import lombok.Getter;
import yapion.hierarchy.types.YAPIONObject;

import java.util.function.BiFunction;

class SerializerFuture {

    private BiFunction<Integer, Integer, Class<?>> classLoader;
    private int start;
    private int length;

    @Getter
    private String type;

    @Getter
    private String classType;

    @Getter
    private String interfaceType;

    @Getter
    private String primitiveType;

    @Getter
    private boolean directLoad;

    private Class<?> clazz = null;

    public SerializerFuture(YAPIONObject data, BiFunction<Integer, Integer, Class<?>> classLoader) {
        this.classLoader = classLoader;

        this.start = data.getPlainValue("start");
        this.length = data.getPlainValue("length");
        this.type = data.getPlainValueOrDefault("t", null);
        this.primitiveType = data.getPlainValueOrDefault("pt", null);
        this.interfaceType = data.getPlainValueOrDefault("it", null);
        this.classType = data.getPlainValueOrDefault("ct", null);
        this.directLoad = data.getPlainValueOrDefault("dl", false);
    }

    public Class<?> get() {
        if (clazz == null) {
            clazz = classLoader.apply(start, length);
        }
        return clazz;
    }
}
