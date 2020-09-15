package yapion.parser;

import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.*;
import yapion.utils.ReflectionsUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class JSONMapper {

    public static final String POINTER_IDENTIFIER = "@pointer";
    public static final String MAP_IDENTIFIER = "@mapping";

    public static final String BYTE_IDENTIFIER = "@byte";
    public static final String SHORT_IDENTIFIER = "@short";
    public static final String CHAR_IDENTIFIER = "@char";
    public static final String INT_IDENTIFIER = "@int";
    public static final String FLOAT_IDENTIFIER = "@float";
    public static final String LONG_IDENTIFIER = "@long";
    public static final String DOUBLE_IDENTIFIER = "@double";
    public static final String BIG_INTEGER_IDENTIFIER = "@bint";
    public static final String BIG_DECIMAL_IDENTIFIER = "@bdecimal";

    private YAPIONObject json;
    private YAPIONObject result = null;

    private List<YAPIONObject> yapionObjectList = new ArrayList<>();
    private List<YAPIONPointer> yapionPointerList = new ArrayList<>();

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
            yapionObjectMap.put(new YAPIONPointer(yapionObject).getPointerID(), yapionObject);
        }
        for (YAPIONPointer yapionPointer : yapionPointerList) {
            long id = yapionPointer.getPointerID();
            YAPIONObject yapionObject = yapionObjectMap.get(id);
            if (yapionObject == null) continue;
            ReflectionsUtils.invokeMethod("setYAPIONObject", yapionPointer, yapionObject);
        }
    }

    private YAPIONObject mapObject(YAPIONObject json) {
        yapionObjectList.add(json);
        List<YAPIONVariable> toChange = new ArrayList<>(json.size());
        for (String s : json.getKeys()) {
            YAPIONVariable variable = json.getVariable(s);
            toChange.add(new YAPIONVariable(variable.getName(), map(variable.getValue())));
        }

        for (YAPIONVariable variable : toChange) {
            json.add(variable);
        }

        return json;
    }

    private YAPIONAnyType map(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType instanceof YAPIONArray) {
            return mapArray((YAPIONArray) yapionAnyType);
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
            json.set(i, map(json.get(i)));
        }
        return json;
    }

    private YAPIONPointer mapPointer(YAPIONObject yapionObject) {
        return new YAPIONPointer(yapionObject.getValue(POINTER_IDENTIFIER, "").get());
    }

    private YAPIONAnyType mapMap(YAPIONObject yapionObject) {
        YAPIONArray mapping = yapionObject.getArray(MAP_IDENTIFIER);
        if (yapionObject.size() != mapping.length() * 2 + 1) {
            return mapObject(yapionObject);
        }
        YAPIONMap yapionMap = new YAPIONMap();
        for (int i = 0; i < mapping.length(); i++) {
            String[] mapped = ((YAPIONValue<String>) mapping.get(i)).get().split(":");
            YAPIONAnyType key = map(yapionObject.getVariable("#" + mapped[0]).getValue());
            YAPIONAnyType value = map(yapionObject.getVariable("#" + mapped[1]).getValue());

            yapionMap.add(key, value);
        }
        return yapionMap;
    }

    private YAPIONValue<Byte> mapByteValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((byte)(int)yapionObject.getValue(BYTE_IDENTIFIER).get()));
    }

    private YAPIONValue<Short> mapShortValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((short)(int)yapionObject.getValue(SHORT_IDENTIFIER).get()));
    }

    private YAPIONValue<Character> mapCharValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(yapionObject.getValue(CHAR_IDENTIFIER, "").get().charAt(0));
    }

    private YAPIONValue<Integer> mapIntValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((int)yapionObject.getValue(INT_IDENTIFIER).get()));
    }

    private YAPIONValue<Float> mapFloatValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((float)(double)yapionObject.getValue(FLOAT_IDENTIFIER).get()));
    }

    private YAPIONValue<Long> mapLongValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((long)(int)yapionObject.getValue(LONG_IDENTIFIER).get()));
    }

    private YAPIONValue<Double> mapDoubleValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((double)yapionObject.getValue(DOUBLE_IDENTIFIER).get()));
    }

    private YAPIONValue<BigInteger> mapBigIntegerValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((BigInteger)yapionObject.getValue(BIG_INTEGER_IDENTIFIER).get()));
    }

    private YAPIONValue<BigDecimal> mapBigDecimalValue(YAPIONObject yapionObject) {
        return new YAPIONValue<>(((BigDecimal)yapionObject.getValue(BIG_DECIMAL_IDENTIFIER).get()));
    }

}
