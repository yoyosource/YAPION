package test;

import yapion.annotations.YAPIONData;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@YAPIONData
public class TestData {

    private final int i = 1;
    private final String hugo = "Hugo";
    private final List<String> strings = new ArrayList<>();
    {
        strings.add("Hugo");
        strings.add("Hugo2");
    }
    private final Map<String, String> stringMap = new HashMap<>();
    {
        stringMap.put("Hugo", "Test");
        stringMap.put("Hugo2", "Test2");
        stringMap.put("Hugo3", "Test3");
        stringMap.put("Hugo4", "Test4");
    }

    private final transient String test = "Test";
    private static final String test2 = "Test2";

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestData(""));
        System.out.println(yapionObject);
        Object object = YAPIONDeserializer.deserialize(yapionObject);
        System.out.println(object);
    }

    private TestData(String s) {

    }

    @Override
    public String toString() {
        return "TestData{" +
                "i=" + i +
                ", hugo='" + hugo + '\'' +
                ", strings=" + strings +
                ", stringMap=" + stringMap +
                ", test='" + test + '\'' +
                '}';
    }
}
