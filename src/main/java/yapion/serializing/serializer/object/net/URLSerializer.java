// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.net;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.net.MalformedURLException;
import java.net.URL;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class URLSerializer implements InternalSerializer<URL> {

    @Override
    public String type() {
        return "java.net.URL";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<URL> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        try {
            yapionObject.add("protocol", serializeData.object.getProtocol());
            yapionObject.add("host", serializeData.object.getHost());
            yapionObject.add("port", serializeData.object.getPort());
            yapionObject.add("file", serializeData.object.getFile());
        } catch (Exception e) {

        }
        return yapionObject;
    }

    @Override
    public URL deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
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