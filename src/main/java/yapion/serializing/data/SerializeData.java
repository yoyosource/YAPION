// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.data;

import lombok.RequiredArgsConstructor;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.serializing.YAPIONDataLossException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.serializing.YAPIONSerializer;

import java.lang.reflect.Field;

@RequiredArgsConstructor
@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SerializeData<T> {

    public final T object;
    public final String context;
    private final YAPIONSerializer yapionSerializer;

    public <R extends Object> SerializeData<R> clone(R object) {
        return new SerializeData<>(object, context, yapionSerializer);
    }

    @SuppressWarnings({"java:S3011"})
    public final YAPIONAnyType serializeField(String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return serialize(field.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new YAPIONSerializerException();
        }
    }

    public final YAPIONAnyType serialize(Object o) {
        return yapionSerializer.parse(o);
    }

    public boolean isStrictSerialization() {
        return yapionSerializer.isStrict();
    }

    public void signalDataLoss() {
        signalDataLoss("");
    }

    public void signalDataLoss(String message) {
        if (yapionSerializer.isStrict()) {
            throw new YAPIONDataLossException(message);
        }
    }

}