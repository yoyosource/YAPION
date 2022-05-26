/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.serializing.data;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.serializing.YAPIONDataLossException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONFlag;
import yapion.serializing.YAPIONFlags;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.views.View;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Field;

@RequiredArgsConstructor
@ToString
public class SerializeData<T> {

    public final String fieldName;
    public final Object outerObject;
    public final T object;
    public final Class<? extends View> context;

    @ToString.Exclude
    private final YAPIONSerializer yapionSerializer;

    public <R> SerializeData<R> copy(R object) {
        return new SerializeData<>(null, this.object, object, context, yapionSerializer);
    }

    public final YAPIONAnyType serializeField(Field field) {
        return yapionSerializer.parse(field.getName(), object, getField(field));
    }

    public final YAPIONAnyType serializeField(String fieldName) {
        return serialize(getField(fieldName));
    }

    public final YAPIONAnyType serialize(Object o) {
        return yapionSerializer.parse(null, object, o);
    }

    public final void serialize(YAPIONObject yapionObject, Field field) {
        yapionObject.add(field.getName(), serializeField(field));
    }

    @SuppressWarnings({"java:S3011"})
    public final Object getField(String fieldName) {
        Field field = ReflectionsUtils.getField(object.getClass(), fieldName);
        return getField(field);
    }

    public final Object getField(Field field) {
        if (!SerializeManager.getReflectionStrategy().checkGet(field, object)) {
            throw new YAPIONSerializerException("Field getting denied by ReflectionStrategy");
        }
        return SerializeManager.getReflectionStrategy().get(field, object);
    }

    public final boolean checkFieldGet(Field field) {
        return SerializeManager.getReflectionStrategy().checkGet(field, object);
    }

    public YAPIONFlags getYAPIONFlags() {
        return yapionSerializer.getYAPIONFlags();
    }

    public void isSet(YAPIONFlag key, Runnable allowed) {
        isSet(key, allowed, () -> {});
    }

    public void isSet(YAPIONFlag key, Runnable allowed, Runnable disallowed) {
        if (yapionSerializer.getYAPIONFlags().isSet(key)) {
            allowed.run();
        } else {
            disallowed.run();
        }
    }

    public void signalDataLoss() {
        isSet(YAPIONFlag.DATA_LOSS_EXCEPTION, () -> {
            throw new YAPIONDataLossException("Some data would be discarded by serialization");
        });
    }

    @InternalAPI
    public YAPIONSerializer getSerializer() {
        return yapionSerializer;
    }
}
