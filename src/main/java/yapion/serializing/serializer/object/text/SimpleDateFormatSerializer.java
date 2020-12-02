// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.text;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.text.SimpleDateFormat;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class SimpleDateFormatSerializer implements InternalSerializer<SimpleDateFormat> {

    @Override
    public String type() {
        return "java.text.SimpleDateFormat";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<SimpleDateFormat> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("pattern", serializeData.object.toPattern());
        return yapionObject;
    }

    @Override
    public SimpleDateFormat deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        return new SimpleDateFormat(((YAPIONObject) deserializeData.object).getValue("pattern", "").get());
    }
}