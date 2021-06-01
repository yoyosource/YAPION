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

package yapion.serializing.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.api.*;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@UtilityClass
public class SerializeManagerUtils {

    /**
     * Create a {@link SerializerQueue} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T> SerializerObject<T> SerializerObject(@NonNull Class<T> clazz, @NonNull SerializationGetter<T, YAPIONObject> serializationGetter, @NonNull DeserializationGetter<T, YAPIONObject> deserializationGetter) {
        return new SerializerObject<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONObject serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONObject> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    /**
     * Create a {@link SerializerMap} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends Map<?, ?>> SerializerMap<T> SerializerMap(@NonNull Class<T> clazz, @NonNull SerializationGetter<T, YAPIONMap> serializationGetter, @NonNull DeserializationGetter<T, YAPIONMap> deserializationGetter) {
        return new SerializerMap<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONMap serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONMap> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    /**
     * Create a {@link SerializerList} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends List<?>> SerializerList<T> SerializerList(@NonNull Class<T> clazz, @NonNull SerializationGetter<T, YAPIONArray> serializationGetter, @NonNull DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return new SerializerList<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONArray serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONArray> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    /**
     * Create a {@link SerializerQueue} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends Queue<?>> SerializerQueue<T> SerializerQueue(@NonNull Class<T> clazz, @NonNull SerializationGetter<T, YAPIONArray> serializationGetter, @NonNull DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return new SerializerQueue<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONArray serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONArray> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    /**
     * Create a {@link SerializerSet} from two interfaces:
     * {@link SerializationGetter} and {@link DeserializationGetter}.
     * Those are all FunctionalInterfaces with just one
     * method to override. This makes the use off those
     * interfaces really easy. If any of the three arguments
     * are {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    @SuppressWarnings({"java:S100"})
    public static <T extends Set<?>> SerializerSet<T> SerializerSet(@NonNull Class<T> clazz, @NonNull SerializationGetter<T, YAPIONArray> serializationGetter, @NonNull DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        return new SerializerSet<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public YAPIONArray serialize(SerializeData<T> serializeData) {
                return serializationGetter.serialize(serializeData);
            }

            @Override
            public T deserialize(DeserializeData<YAPIONArray> deserializeData) {
                return deserializationGetter.deserialize(deserializeData);
            }
        };
    }

    @FunctionalInterface
    public interface SerializationGetter<T, R extends YAPIONAnyType> {
        R serialize(SerializeData<T> serializeData);
    }

    @FunctionalInterface
    public interface DeserializationGetter<T, R extends YAPIONAnyType> {
        T deserialize(DeserializeData<R> deserializeData);
    }

    /**
     * Create a {@link InstanceFactory} from one interface:
     * {@link InstanceGetter}
     * Which is a FunctionalInterface with just one method
     * to override. This makes the use off this interface
     * really easy. If any of the two arguments are
     * {@code null} an YAPIONException is thrown.
     *
     * @param clazz the Class
     * @param instanceGetter the InstanceGetter
     * @param <T> the Instance type
     * @return the InstanceFactory that wraps the Interface
     */
    public static <T> InstanceFactory<T> InstanceFactory(@NonNull Class<T> clazz, @NonNull InstanceGetter<T> instanceGetter) {
        return new InstanceFactory<T>() {
            @Override
            public Class<T> type() {
                return clazz;
            }

            @Override
            public T instance() {
                return instanceGetter.instance();
            }
        };
    }

    @FunctionalInterface
    public interface InstanceGetter<T> {
        T instance();
    }

}
