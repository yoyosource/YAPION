// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.api;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.*;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Queue;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SuppressWarnings({"java:S1610"})
public abstract class SerializerQueue<T extends Queue<?>> implements SerializerQueueInterface<T> {

    /**
     * Describes the Class type this Serializer should be used for
     * by the {@link YAPIONSerializer} and {@link YAPIONDeserializer}
     * this Serializer will be used preferably as it should suite
     * the Class better than the internal fallback.
     *
     * @return the Class Type this Serializer is for
     */
    public abstract Class<T> type();

    /**
     * This method is used to serialize the type defined by
     * {@link #type()}. You will get the object to serialize
     * as well as the current {@link YAPIONSerializer} designated to
     * this serialization. This serializer should be used to
     * serialize any parts of the {@link Object} that are not
     * primitive types. So anything that cannot be represented
     * by {@link YAPIONValue}. To use the serializer call
     * {@link YAPIONSerializer#parse(Object)}. You will need
     * to provide the Object and the YAPIONSerializer will
     * provide you with an {@link YAPIONAnyType}. This is generic
     * and can be an {@link YAPIONObject}, {@link YAPIONMap},
     * {@link YAPIONArray}, {@link YAPIONPointer} or
     * {@link YAPIONValue}. So you should be able to handle any
     * thing you can get or just handle it generically with
     * {@link YAPIONAnyType} to be safe.
     *
     * @param object the {@link Object} to serialize
     * @param yapionSerializer the current YAPIONSerializer
     * @return the serialized version of the inputted Object
     */
    public abstract YAPIONArray serialize(T object, YAPIONSerializer yapionSerializer);

    /**
     * This method is used to deserialize the type defined
     * by {@link #type()}. You will get the specified
     * {@link YAPIONArray} as well as the current {@link YAPIONDeserializer} designed to
     * this deserialization. This deserializer should be used to
     * deserialize any parts of the {@link YAPIONArray} that are not
     * {@link YAPIONValue} types. So anything that cannot be
     * represented by {@link YAPIONValue}. To use the serializer
     * call {@link YAPIONDeserializer#parse(YAPIONAnyType)}. You will
     * need to provide the {@link YAPIONAnyType} and the YAPIONDeserializer
     * will provide you with an {@link Object}. This is generic
     * and can be any Object. So you should be able to handle
     * any thing generically.
     *
     * @param yapionArray The {@link YAPIONArray} to deserialize
     * @param yapionDeserializer the current YAPIONDeserializer
     * @return the deserialized version of the inputted {@link YAPIONArray}
     */
    public abstract T deserialize(YAPIONArray yapionArray, YAPIONDeserializer yapionDeserializer);

    /**
     * Add this ListSerializer to the SerializeManager by calling
     * {@link SerializeManager#add(SerializerQueueInterface)}.
     */
    public final void add() {
        SerializeManager.add(this);
    }

}