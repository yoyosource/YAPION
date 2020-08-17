package test;

import yapion.annotations.YAPIONData;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONData
public class TestData {

    private final int i = 1;
    private final String hugo = "Hugo";

    private final transient String test = "Test";
    private static final String test2 = "Test2";

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestData());
        System.out.println(yapionObject);
        Object object = YAPIONDeserializer.deserialize(yapionObject);
        System.out.println(object);
    }

}
