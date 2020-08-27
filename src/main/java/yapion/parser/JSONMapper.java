package yapion.parser;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.*;

import java.util.ArrayList;
import java.util.List;

public class JSONMapper {

    public static final String POINTER = "@pointer";
    public static final String MAP = "@mapping";

    private JSONMapper() {
        throw new IllegalStateException();
    }

    static YAPIONObject mapObject(YAPIONObject json) {
        List<YAPIONVariable> toChange = new ArrayList<>(json.variables());
        for (String s : json.getKeys()) {
            YAPIONVariable variable = json.getVariable(s);
            toChange.add(new YAPIONVariable(variable.getName(), map(variable.getValue())));
        }

        for (YAPIONVariable variable : toChange) {
            json.add(variable);
        }

        return json;
    }

    private static YAPIONAny map(YAPIONAny yapionAny) {
        if (yapionAny instanceof YAPIONArray) {
            return mapArray((YAPIONArray) yapionAny);
        }
        if (!(yapionAny instanceof YAPIONObject)) {
            return yapionAny;
        }

        YAPIONObject yapionObject = (YAPIONObject) yapionAny;
        if (yapionObject.getValue(POINTER, "") != null) {
            return mapPointer(yapionObject);
        }
        if (yapionObject.getArray(MAP) != null) {
            return mapMap(yapionObject);
        }
        return mapObject(yapionObject);
    }

    private static YAPIONArray mapArray(YAPIONArray json) {
        for (int i = 0; i < json.length(); i++) {
            json.set(i, map(json.get(i)));
        }
        return json;
    }

    private static YAPIONPointer mapPointer(YAPIONObject yapionObject) {
        return new YAPIONPointer(yapionObject.getValue(POINTER, "").get());
    }

    private static YAPIONMap mapMap(YAPIONObject yapionObject) {
        YAPIONArray mapping = yapionObject.getArray(MAP);
        YAPIONMap yapionMap = new YAPIONMap();
        for (int i = 0; i < mapping.length(); i++) {
            String[] mapped = ((YAPIONValue<String>) mapping.get(i)).get().split(":");
            YAPIONAny key = map(yapionObject.getVariable("#" + mapped[0]).getValue());
            YAPIONAny value = map(yapionObject.getVariable("#" + mapped[1]).getValue());

            yapionMap.add(key, value);
        }
        return yapionMap;
    }

}
