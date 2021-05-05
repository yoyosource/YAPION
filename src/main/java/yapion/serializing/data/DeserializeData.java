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
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class DeserializeData<T extends YAPIONAnyType> {

    public final T object;
    public final String context;
    private final YAPIONDeserializer yapionDeserializer;

    public <R extends YAPIONAnyType> DeserializeData<R> clone(R object) {
        return new DeserializeData<>(object, context, yapionDeserializer);
    }

    public boolean deserialize(String fieldName, Object object, YAPIONAnyType yapionAnyType) {
        Field field = ReflectionsUtils.getField(object.getClass(), fieldName);
        return setField(field, object, deserialize(yapionAnyType));
    }

    public Object deserialize(YAPIONAnyType yapionAnyType) {
        return yapionDeserializer.parse(yapionAnyType);
    }

    public boolean hasFactory(Class<?> clazz) {
        return SerializeManager.hasFactory(clazz);
    }

    public Object getGenericInstance(Class<?> clazz) throws ClassNotFoundException {
        return SerializeManager.getGenericObjectInstance(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) throws ClassNotFoundException {
        return (T) SerializeManager.getObjectInstance(clazz);
    }

    public boolean setField(String fieldName, Object object, Object objectToSet) {
        Field field = ReflectionsUtils.getField(object.getClass(), fieldName);
        return setField(field, object, objectToSet);
    }

    @SuppressWarnings({"java:S3011"})
    private boolean setField(Field field, Object object, Object objectToSet) {
        if (field == null) return false;
        try {
            field.setAccessible(true);
            field.set(object, objectToSet);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public String getArrayType() {
        return yapionDeserializer.getArrayType();
    }

}
