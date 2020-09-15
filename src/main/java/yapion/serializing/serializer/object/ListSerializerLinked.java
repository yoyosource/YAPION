// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.Iterator;
import java.util.LinkedList;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SuppressWarnings({"java:S3740"})
@SerializerImplementation
public class ListSerializerLinked implements InternalSerializer<LinkedList> {

    @Override
    public String type() {
        return "java.util.LinkedList";
    }

    @Override
    public YAPIONAnyType serialize(LinkedList object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("values", yapionArray));
        Iterator iterator = object.iterator();
        while (iterator.hasNext()) {
            yapionArray.add(yapionSerializer.parse(iterator.next()));
        }
        return yapionObject;
    }

    @Override
    public LinkedList deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONArray yapionArray = ((YAPIONObject) yapionAnyType).getArray("values");
        LinkedList<Object> list = new LinkedList<>();
        for (int i = 0; i < yapionArray.length(); i++) {
            list.add(yapionDeserializer.parse(yapionArray.get(i)));
        }
        return list;
    }

}