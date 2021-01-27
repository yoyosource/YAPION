// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.net;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.net.URI;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation(since = "0.12.0")
public class URISerializer implements InternalSerializer<URI> {

    @Override
    public String type() {
        return "java.net.URI";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<URI> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        try {
            yapionObject.add("uri", (String) URI.class.getDeclaredField("string").get(serializeData.object));
        } catch (Exception e) {
            yapionObject.add("uri", "");
        }
        return yapionObject;
    }

    @Override
    public URI deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String s = yapionObject.getValue("uri", "").get();
        return URI.create(s);
    }
}