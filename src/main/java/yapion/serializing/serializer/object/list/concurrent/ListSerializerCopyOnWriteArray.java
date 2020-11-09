// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.list.concurrent;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONVariable;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class ListSerializerCopyOnWriteArray implements InternalSerializer<CopyOnWriteArrayList<?>> {

    @Override
    public String type() {
        return "java.util.concurrent.CopyOnWriteArrayList";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<CopyOnWriteArrayList<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("values", yapionArray));
        Iterator<?> iterator = serializeData.object.iterator();
        while (iterator.hasNext()) {
            yapionArray.add(serializeData.serialize(iterator.next()));
        }
        return yapionObject;
    }

    @Override
    public CopyOnWriteArrayList<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
        CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            list.add(deserializeData.deserialize(yapionArray.get(i)));
        }
        return list;
    }
}