// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.number;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class FloatSerializer implements Serializer<Float> {

    @Override
    public String type() {
        return "java.lang.Float";
    }

    @Override
    public String primitiveType() {
        return "float";
    }

    @Override
    public YAPIONAny serialize(Float object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public Float deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return ((YAPIONValue<Float>) yapionAny).get();
    }
}