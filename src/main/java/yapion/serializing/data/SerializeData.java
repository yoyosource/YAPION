// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.data;

import lombok.RequiredArgsConstructor;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.serializing.YAPIONDataLossException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.YAPIONSerializerFlagDefault;
import yapion.serializing.YAPIONSerializerFlagDefault.YAPIONSerializerFlagKey;
import yapion.serializing.YAPIONSerializerFlags;

import java.lang.reflect.Field;

@RequiredArgsConstructor
@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SerializeData<T> {

    public final T object;
    public final String context;
    private final YAPIONSerializer yapionSerializer;

    public <R> SerializeData<R> clone(R object) {
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

    public YAPIONSerializerFlags getYAPIONSerializerFlags() {
        return yapionSerializer.getYAPIONSerializerFlags();
    }

    public void isSet(YAPIONSerializerFlagKey key, Runnable allowed) {
        isSet(key, allowed, () -> {});
    }

    public void isSet(YAPIONSerializerFlagKey key, Runnable allowed, Runnable disallowed) {
        boolean b = yapionSerializer.getYAPIONSerializerFlags().isSet(key);
        if (b) {
            allowed.run();
        } else {
            disallowed.run();
        }
    }

    public void signalDataLoss() {
        isSet(YAPIONSerializerFlagDefault.DATA_LOSS_EXCEPTION, () -> {
            throw new YAPIONDataLossException("Some data would be discarded by serialization");
        });
    }

}