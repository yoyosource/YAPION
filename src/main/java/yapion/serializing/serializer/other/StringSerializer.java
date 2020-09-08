// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializeLoader;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializeLoader
public class StringSerializer implements InternalSerializer<String> {

    @Override
    public String type() {
        return "java.lang.String";
    }

    @Override
    public YAPIONAny serialize(String object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public String deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return ((YAPIONValue<String>) yapionAny).get();
    }
}