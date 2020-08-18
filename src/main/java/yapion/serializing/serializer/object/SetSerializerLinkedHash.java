// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashSet;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SetSerializerLinkedHash implements Serializer<LinkedHashSet> {

    @Override
    public String type() {
        return "java.util.LinkedHashSet";
    }

    @Override
    public YAPIONAny serialize(LinkedHashSet object, YAPIONSerializer yapionSerializer) {
        Iterator iterator = object.iterator();
        YAPIONArray yapionArray = new YAPIONArray();
        while (iterator.hasNext()) {
            yapionArray.add(yapionSerializer.parse(iterator.next(), yapionSerializer));
        }
        return yapionArray;
    }

    @Override
    public LinkedHashSet deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer, Field field) {
        YAPIONArray yapionArray = (YAPIONArray)yapionAny;
        LinkedHashSet<Object> set = new LinkedHashSet<>(yapionArray.length());
        for (int i = 0; i < yapionArray.length(); i++) {
            set.add(yapionDeserializer.parse(yapionArray.get(i), yapionDeserializer, field));
        }
        return set;
    }

}