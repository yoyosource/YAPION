package yapion.parser;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.*;
import yapion.utils.ReflectionsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONMapper {

    public static final String POINTER = "@pointer";
    public static final String MAP = "@mapping";

    private YAPIONObject json;
    private YAPIONObject result;

    private List<YAPIONObject> yapionObjectList = new ArrayList<>();
    private List<YAPIONPointer> yapionPointerList = new ArrayList<>();

    private JSONMapper(YAPIONObject json) {
        this.json = json;
    }

    static YAPIONObject map(YAPIONObject json) {
        return new JSONMapper(json).parse().result();
    }

    public JSONMapper parse() {
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

    private YAPIONAny map(YAPIONAny yapionAny) {
        if (yapionAny instanceof YAPIONArray) {
            return mapArray((YAPIONArray) yapionAny);
        }
        if (!(yapionAny instanceof YAPIONObject)) {
            return yapionAny;
        }

        YAPIONObject yapionObject = (YAPIONObject) yapionAny;
        if (yapionObject.getValue(POINTER, "") != null) {
            YAPIONPointer yapionPointer = mapPointer(yapionObject);
            yapionPointerList.add(yapionPointer);
            return yapionPointer;
        }
        if (yapionObject.getArray(MAP) != null) {
            return mapMap(yapionObject);
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
        return new YAPIONPointer(yapionObject.getValue(POINTER, "").get());
    }

    private YAPIONMap mapMap(YAPIONObject yapionObject) {
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