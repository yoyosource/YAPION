package yapion.serializing.serializer.objectNet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.net.URI;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class URISerializer implements InternalSerializer<URI> {

    @Override
    public String type() {
        return "java.net.URI";
    }

    @Override
    public YAPIONAnyType serialize(URI object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type()));
        try {
            yapionObject.add("uri", new YAPIONValue<>((String) URI.class.getDeclaredField("string").get(object)));
        } catch (Exception e) {
            yapionObject.add("uri", new YAPIONValue<>(""));
        }
        return yapionObject;
    }

    @Override
    public URI deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
        String s = yapionObject.getValue("uri", "").get();
        return URI.create(s);
    }
}
