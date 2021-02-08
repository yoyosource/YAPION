// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.list;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Iterator;
import java.util.LinkedList;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SuppressWarnings({"java:S3740"})
@SerializerImplementation(since = "0.3.0")
public class LinkedListSerializer implements InternalSerializer<LinkedList> {

    @Override
    public String type() {
        return "java.util.LinkedList";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<LinkedList> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("values", yapionArray);
        Iterator iterator = serializeData.object.iterator();
        while (iterator.hasNext()) {
            yapionArray.add(serializeData.serialize(iterator.next()));
        }
        return yapionObject;
    }

    @Override
    public LinkedList deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONArray yapionArray = ((YAPIONObject) deserializeData.object).getArray("values");
        LinkedList<Object> list = new LinkedList<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            list.add(deserializeData.deserialize(yapionArray.getYAPIONAnyType(i)));
        }
        return list;
    }

}