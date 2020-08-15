// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;

import java.lang.reflect.Field;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public interface Serializer<T> {

    String type();
    default String primitiveType() {
        return "";
    }
    YAPIONAny serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer, Field field);

}