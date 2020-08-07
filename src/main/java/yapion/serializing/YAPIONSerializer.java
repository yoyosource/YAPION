package yapion.serializing;

import test.Test;
import yapion.hierarchy.types.YAPIONObject;

public class YAPIONSerializer {

    private Object object;
    private StateManager stateManager;

    public static void main(String[] args) {
        serialize(new Test());
    }

    public static YAPIONObject serialize(Object object) {
        return serialize(object, "");
    }

    public static YAPIONObject serialize(Object object, String state) {
        return new YAPIONSerializer(object, state).parse().getYAPIONObject();
    }

    public YAPIONSerializer(Object object, String state) {
        stateManager = new StateManager(state);
        this.object = object;
    }

    public YAPIONSerializer parse() {

        return this;
    }

    public YAPIONObject getYAPIONObject() {

        return null;
    }

}
