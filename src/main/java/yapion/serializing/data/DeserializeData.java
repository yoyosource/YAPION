// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.data;

import lombok.RequiredArgsConstructor;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.serializing.YAPIONDeserializer;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Field;

@RequiredArgsConstructor
@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class DeserializeData<T extends YAPIONAnyType> {

    public final T object;
    public final String context;
    private final YAPIONDeserializer yapionDeserializer;

    public <R extends YAPIONAnyType> DeserializeData<R> clone(R object) {
        return new DeserializeData<>(object, context, yapionDeserializer);
    }

    public boolean deserialize(String fieldName, Object object, YAPIONAnyType yapionAnyType) {
        Field field = ReflectionsUtils.getField(object.getClass(), fieldName);
        return setField(field, deserialize(yapionAnyType));
    }

    public Object deserialize(YAPIONAnyType yapionAnyType) {
        return yapionDeserializer.parse(yapionAnyType);
    }

    public boolean setField(String fieldName, Object object, Object objectToSet) {
        Field field = ReflectionsUtils.getField(object.getClass(), fieldName);
        return setField(field, objectToSet);
    }

    @SuppressWarnings({"java:S3011"})
    private boolean setField(Field field, Object objectToSet) {
        if (field == null) return false;
        try {
            field.setAccessible(true);
            field.set(object, objectToSet);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

}