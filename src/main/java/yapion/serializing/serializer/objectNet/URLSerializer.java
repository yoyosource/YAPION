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

import java.net.MalformedURLException;
import java.net.URL;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class URLSerializer implements InternalSerializer<URL> {

    @Override
    public String type() {
        return "java.net.URL";
    }

    @Override
    public YAPIONAnyType serialize(URL object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type()));
        try {
            yapionObject.add("protocol", new YAPIONValue<>(object.getProtocol()));
            yapionObject.add("host", new YAPIONValue<>(object.getHost()));
            yapionObject.add("port", new YAPIONValue<>(object.getPort()));
            yapionObject.add("file", new YAPIONValue<>(object.getFile()));
        } catch (Exception e) {

        }
        return yapionObject;
    }

    @Override
    public URL deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
        String protocol = yapionObject.getValue("protocol", "").get();
        String host = yapionObject.getValue("host", "").get();
        int port = yapionObject.getValue("port", 0).get();
        String file = yapionObject.getValue("file", "").get();
        try {
            return new URL(protocol, host, port, file);
        } catch (MalformedURLException e) {}
        try {
            return new URL(protocol, host, file);
        } catch (MalformedURLException e) {}
        try {
            return new URL(host);
        } catch (MalformedURLException e) {}
        return null;
    }
}
