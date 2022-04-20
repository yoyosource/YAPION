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

package yapion.parser;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.types.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static yapion.utils.IdentifierUtils.*;

public class JSONMapper {

    private final YAPIONObject json;
    private YAPIONObject result = null;

    private final List<YAPIONObject> yapionObjectList = new ArrayList<>();
    private final List<YAPIONPointer> yapionPointerList = new ArrayList<>();

    private JSONMapper(YAPIONObject json) {
        this.json = json;
    }

    static YAPIONObject map(YAPIONObject json) {
        return new JSONMapper(json).parse().result();
    }

    public JSONMapper parse() {
        if (json == null) return this;
        result = mapObject(json);
        parseFinish();
        return this;
    }

    public YAPIONObject result() {
        return result;
    }

    private void parseFinish() {
        Map<Long, YAPIONObject> yapionObjectMap = new HashMap<>();
        for (YAPIONObject yapionObject : yapionObjectList) {
            yapionObjectMap.put(yapionObject.referenceValue(), yapionObject);
        }
        for (YAPIONPointer yapionPointer : yapionPointerList) {
            long id = yapionPointer.getPointerID();
            YAPIONObject yapionObject = yapionObjectMap.get(id);
            if (yapionObject == null) continue;
            yapionPointer.set(yapionObject);
        }
    }

    private YAPIONObject mapObject(YAPIONObject json) {
        yapionObjectList.add(json);
        for (String s : json.getKeys()) {
            YAPIONAnyType yapionAnyType = json.getAnyType(s);
            if (yapionAnyType == null) continue;
            json.add(s, map(yapionAnyType));
        }
        return json;
    }

    private YAPIONAnyType map(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType instanceof YAPIONArray yapionArray) {
            return mapArray(yapionArray);
        }
        if (!(yapionAnyType instanceof YAPIONObject)) {
            return yapionAnyType;
        }

        YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
        if (yapionObject.getArray(MAP_IDENTIFIER) != null) {
            return mapMap(yapionObject);
        }
        if (yapionObject.size() != 1) {
            return mapObject(yapionObject);
        }
        if (yapionObject.getValue(POINTER_IDENTIFIER, "") != null) {
            YAPIONPointer yapionPointer = mapPointer(yapionObject);
            yapionPointerList.add(yapionPointer);
            return yapionPointer;
        }

        if (yapionObject.getValue(BYTE_IDENTIFIER) != null) {
            return mapByteValue(yapionObject);
        }
        if (yapionObject.getValue(SHORT_IDENTIFIER) != null) {
            return mapShortValue(yapionObject);
        }
        if (yapionObject.getValue(CHAR_IDENTIFIER) != null) {
            return mapCharValue(yapionObject);
        }
        if (yapionObject.getValue(INT_IDENTIFIER) != null) {
            return mapIntValue(yapionObject);
        }
        if (yapionObject.getValue(FLOAT_IDENTIFIER) != null) {
            return mapFloatValue(yapionObject);
        }
        if (yapionObject.getValue(LONG_IDENTIFIER) != null) {
            return mapLongValue(yapionObject);
        }
        if (yapionObject.getValue(DOUBLE_IDENTIFIER) != null) {
            return mapDoubleValue(yapionObject);
        }
        if (yapionObject.getValue(BIG_INTEGER_IDENTIFIER) != null) {
            return mapBigIntegerValue(yapionObject);
        }
        if (yapionObject.getValue(BIG_DECIMAL_IDENTIFIER) != null) {
            return mapBigDecimalValue(yapionObject);
        }

        return mapObject(yapionObject);
    }

    private YAPIONArray mapArray(YAPIONArray json) {
        for (int i = 0; i < json.length(); i++) {
            json.set(i, map(json.getAnyType(i)));
        }
        return json;
    }

    private YAPIONPointer mapPointer(YAPIONObject yapionObject) {
        return new YAPIONPointer(yapionObject.getValue(POINTER_IDENTIFIER, "").get());
    }

    private YAPIONDataType<? extends YAPIONDataType<?,?>, ? extends Object> mapMap(YAPIONObject yapionObject) {
        YAPIONArray mapping = yapionObject.getArray(MAP_IDENTIFIER);
        if (yapionObject.size() != mapping.length() * 2 + 1) {
            return mapObject(yapionObject);
        }
        YAPIONMap yapionMap = new YAPIONMap();
        for (int i = 0; i < mapping.length(); i++) {
            String[] mapped = mapping.getValue(i, String.class).get().split(":");
            YAPIONAnyType key = map(yapionObject.getAnyType("#" + mapped[0]));
            YAPIONAnyType value = map(yapionObject.getAnyType("#" + mapped[1]));

            yapionMap.add(key, value);
        }
        return yapionMap;
    }

    private YAPIONValue<Byte> mapByteValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((byte) (int) yapionObject.getValue(BYTE_IDENTIFIER).get()));
    }

    private YAPIONValue<Short> mapShortValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((short) (int) yapionObject.getValue(SHORT_IDENTIFIER).get()));
    }

    private YAPIONValue<Character> mapCharValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(yapionObject.getValue(CHAR_IDENTIFIER, "").get().charAt(0));
    }

    private YAPIONValue<Integer> mapIntValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((int) yapionObject.getValue(INT_IDENTIFIER).get()));
    }

    private YAPIONValue<Float> mapFloatValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((float) (double) yapionObject.getValue(FLOAT_IDENTIFIER).get()));
    }

    private YAPIONValue<Long> mapLongValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(Long.parseLong(yapionObject.getValue(LONG_IDENTIFIER).get().toString()));
    }

    private YAPIONValue<Double> mapDoubleValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((double) yapionObject.getValue(DOUBLE_IDENTIFIER).get()));
    }

    private YAPIONValue<BigInteger> mapBigIntegerValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(new BigInteger((String) yapionObject.getValue(BIG_INTEGER_IDENTIFIER).get()));
    }

    private YAPIONValue<BigDecimal> mapBigDecimalValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(new BigDecimal((String) yapionObject.getValue(BIG_DECIMAL_IDENTIFIER).get()));
    }
}
