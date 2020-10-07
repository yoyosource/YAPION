// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.objectConcurrent;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class SetSerializerCopyOnWriteArray implements InternalSerializer<CopyOnWriteArraySet<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.CopyOnWriteArraySet";
    }

    @Override
    public YAPIONAnyType serialize(CopyOnWriteArraySet<?> object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("values", yapionArray));
        Iterator<?> iterator = object.iterator();
        while (iterator.hasNext()) {
            yapionArray.add(yapionSerializer.parse(iterator.next()));
        }
        return yapionObject;
    }

    @Override
    public CopyOnWriteArraySet<?> deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONArray yapionArray = ((YAPIONObject) yapionAnyType).getArray("values");
        CopyOnWriteArraySet<Object> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            set.add(yapionDeserializer.parse(yapionArray.get(i)));
        }
        return set;
    }
}