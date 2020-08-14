// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONArray;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.HashSet;
import java.util.Iterator;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SetSerializerHash implements Serializer<HashSet> {

    @Override
    public String type() {
        return "java.util.HashSet";
    }

    @Override
    public YAPIONAny serialize(HashSet object, YAPIONSerializer yapionSerializer) {
        Iterator iterator = object.iterator();
        YAPIONArray yapionArray = new YAPIONArray();
        while (iterator.hasNext()) {
            yapionArray.add(yapionSerializer.parse(iterator.next(), yapionSerializer));
        }
        return yapionArray;
    }

    @Override
    public HashSet deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        return null;
    }

}