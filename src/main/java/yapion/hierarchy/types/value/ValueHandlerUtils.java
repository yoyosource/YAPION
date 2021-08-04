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

    private static final Set<String> allowedTypes = new HashSet<>();
    private static final Map<String, String> typeIdentifier = new HashMap<>();

    @InternalAPI
    public static List<ValueHandler<?>> allValueHandlers() {
        return valueHandlerList;
    }

    @InternalAPI
    public static ValueHandler<?> get(String type) {
        return stringValueHandlerMap.get(type);
    }

    @InternalAPI
    public static boolean allowedType(String type) {
        return allowedTypes.contains(type);
    }

    @InternalAPI
    public static String[] allowedTypesArray() {
        return allowedTypes.toArray(new String[0]);
    }

    @InternalAPI
    public static boolean containsTypeIdentifier(String type) {
        return typeIdentifier.containsKey(type);
    }

    @InternalAPI
    public static String getTypeIdentifier(String type) {
        return typeIdentifier.get(type);
    }

    static {
        InputStream inputStream = ValueHandlerUtils.class.getResourceAsStream("valueHandler.zar.gz");
        if (inputStream == null) {
            log.error("No ValueHandler was loaded. Please inspect.");
            throw new YAPIONException("No ValueHandler was loaded. Please inspect.");
        }

        try {
            Unpacker.unpack(inputStream, "yapion.hierarchy.types.value.", ValueHandlerUtils::internalAdd);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        if (valueHandlerList.isEmpty()) {
            log.error("No ValueHandler was loaded. Please inspect.");
        }
        valueHandlerList.sort(Comparator.comparing(ValueHandler::index));
    }

    private static void internalAdd(Class<?> clazz) {
        if (clazz.isInterface()) return;
        if (clazz.getInterfaces().length != 1) return;
        Object o = ReflectionsUtils.constructObjectObjenesis(clazz);
        if (o == null) return;
        if (o instanceof ValueHandler<?> valueHandler) {
            allowedTypes.add(valueHandler.type());
            if (valueHandler.typeIdentifier() != null) {
                typeIdentifier.put(valueHandler.type(), valueHandler.typeIdentifier());
            }
            valueHandlerList.add(valueHandler);
            if (valueHandler.type() != null && valueHandler.type().isEmpty()) return;
            stringValueHandlerMap.put(valueHandler.type(), valueHandler);
        }
    }

}
