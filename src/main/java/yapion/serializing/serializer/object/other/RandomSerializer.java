// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class RandomSerializer implements InternalSerializer<Random> {

    @Override
    public String type() {
        return "java.util.Random";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Random> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("seed", serializeData.serializeField("seed"));
        return yapionObject;
    }

    @Override
    public Random deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        AtomicLong atomicLong = (AtomicLong) deserializeData.deserialize(((YAPIONObject) deserializeData.object).getObject("seed"));
        return new Random(atomicLong.get());
    }
}