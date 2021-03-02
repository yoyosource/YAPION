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

package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
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

public class SerializeManagerUtils {

    private SerializeManagerUtils() {
        throw new IllegalStateException("Utility class");
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
    public static <T> SerializerObject<T> SerializerObject(Class<T> clazz, SerializationGetter<T, YAPIONObject> serializationGetter, DeserializationGetter<T, YAPIONObject> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
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
    public static <T extends Map<?, ?>> SerializerMap<T> SerializerMap(Class<T> clazz, SerializationGetter<T, YAPIONMap> serializationGetter, DeserializationGetter<T, YAPIONMap> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
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
    public static <T extends List<?>> SerializerList<T> SerializerList(Class<T> clazz, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
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
    public static <T extends Queue<?>> SerializerQueue<T> SerializerQueue(Class<T> clazz, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
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
    public static <T extends Set<?>> SerializerSet<T> SerializerSet(Class<T> clazz, SerializationGetter<T, YAPIONArray> serializationGetter, DeserializationGetter<T, YAPIONArray> deserializationGetter) {
        if (clazz == null) throw new YAPIONException();
        if (serializationGetter == null) throw new YAPIONException();
        if (deserializationGetter == null) throw new YAPIONException();
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
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    public interface SerializationGetter<T, R extends YAPIONAnyType> {
        R serialize(SerializeData<T> serializeData);
    }

    @FunctionalInterface
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
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
    public static <T> InstanceFactory<T> InstanceFactory(Class<T> clazz, InstanceGetter<T> instanceGetter) {
        if (clazz == null) throw new YAPIONException();
        if (instanceGetter == null) throw new YAPIONException();
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
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    public interface InstanceGetter<T> {
        T instance();
    }

}
