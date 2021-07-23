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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.YAPIONException;
import yapion.serializing.SerializeManager;
import yapion.utils.ReflectionsUtils;
import yapion.utils.Unpacker;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@UtilityClass
@InternalAPI
public class ValueHandlerUtils {

    private static final List<ValueHandler<?>> valueHandlerList = new ArrayList<>();
    private static final Map<String, ValueHandler<?>> stringValueHandlerMap = new HashMap<>();

    @InternalAPI
    public static List<ValueHandler<?>> allValueHandlers() {
        return valueHandlerList;
    }

    @InternalAPI
    public static ValueHandler<?> get(String type) {
        return stringValueHandlerMap.get(type);
    }

    static {
        InputStream inputStream = ValueHandlerUtils.class.getResourceAsStream("valueHandler.zar.gz");
        if (inputStream == null) {
            log.error("No ValueHandler was loaded. Please inspect.");
            throw new YAPIONException("No ValueHandler was loaded. Please inspect.");
        }

        try {
            Unpacker.unpack(inputStream, "yapion.hierarchy.types.value.", (className, depth) -> {
                if (className.endsWith("NumberSuffix.class")) {
                    return depth - 1;
                }
                return depth;
            }, ValueHandlerUtils::internalAdd);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        if (valueHandlerList.isEmpty()) {
            log.error("No ValueHandler was loaded. Please inspect.");
        }
        valueHandlerList.sort(Comparator.comparing(valueHandler -> valueHandler.index()));
    }

    private static void internalAdd(Class<?> clazz) {
        if (clazz.isInterface()) return;
        if (clazz.getInterfaces().length != 1) return;
        Object o = ReflectionsUtils.constructObjectObjenesis(clazz);
        if (o == null) return;
        if (o instanceof ValueHandler valueHandler) {
            valueHandlerList.add(valueHandler);
            stringValueHandlerMap.put(valueHandler.type(), valueHandler);
        }
    }

}
