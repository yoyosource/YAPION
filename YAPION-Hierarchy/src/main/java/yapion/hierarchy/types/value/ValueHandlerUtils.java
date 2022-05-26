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
import yapion.annotations.api.InternalAPI;

import java.util.*;

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

    static {
        internalAdd(new BooleanHandler());
        internalAdd(new CharacterHandler());
        internalAdd(new FractionNumberHandler.FloatHandler());
        internalAdd(new FractionNumberHandler.DoubleHandler());
        internalAdd(new FractionNumberHandler.BigDecimalHandler());
        internalAdd(new HexNumberHandler.ByteHexHandler());
        internalAdd(new HexNumberHandler.ShortHexHandler());
        internalAdd(new HexNumberHandler.IntegerHexHandler());
        internalAdd(new HexNumberHandler.LongHexHandler());
        internalAdd(new NullHandler());
        internalAdd(new StringHandler());
        internalAdd(new WholeNumberHandler.ByteHandler());
        internalAdd(new WholeNumberHandler.ShortHandler());
        internalAdd(new WholeNumberHandler.IntegerHandler());
        internalAdd(new WholeNumberHandler.LongHandler());
        internalAdd(new WholeNumberHandler.BigIntegerHandler());
        valueHandlerList.sort(Comparator.comparing(ValueHandler::index));
    }

    private static void internalAdd(ValueHandler<?> valueHandler) {
        allowedTypes.add(valueHandler.type());
        if (valueHandler.typeIdentifier() != null) {
            typeIdentifier.put(valueHandler.type(), valueHandler.typeIdentifier());
        }
        valueHandlerList.add(valueHandler);
        if (valueHandler.type() != null && valueHandler.type().isEmpty()) return;
        stringValueHandlerMap.put(valueHandler.type(), valueHandler);
    }
}
