package yapion.serializing.serializer.other;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

import java.io.File;

public class FileSerializer implements Serializer<File> {

    @Override
    public String type() {
        return "java.io.File";
    }

    @Override
    public YAPIONAny serialize(File object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("@type", new YAPIONValue<>("@file")));
        yapionObject.add(new YAPIONVariable("@name", new YAPIONValue<>("@file")));
        yapionObject.add(new YAPIONVariable("absolutePath", new YAPIONValue<>(object.getAbsolutePath())));
        yapionObject.add(new YAPIONVariable("name", new YAPIONValue<>(object.getName())));
        yapionObject.add(new YAPIONVariable("parent", new YAPIONValue<>(object.getParent())));
        yapionObject.add(new YAPIONVariable("path", new YAPIONValue<>(object.getPath())));
        return yapionObject;
    }
}
