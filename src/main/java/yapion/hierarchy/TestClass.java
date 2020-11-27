package yapion.hierarchy;

import test.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.output.StringPrettifiedOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

public class TestClass {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new Test());
        System.out.println(yapionObject.toJSON(new StringPrettifiedOutput()).getResult());
    }

}
