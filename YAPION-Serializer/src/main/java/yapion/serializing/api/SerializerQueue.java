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

package yapion.serializing.api;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.*;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.util.Queue;

public abstract class SerializerQueue<T extends Queue<?>> extends SerializerBase<T, YAPIONArray> {

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
     * @param serializeData all data for serialization
     * @return the serialized version of the inputted Object
     */
    public abstract YAPIONArray serialize(SerializeData<T> serializeData);

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
     * @param deserializeData all data for deserialization
     * @return the deserialized version of the inputted {@link YAPIONArray}
     */
    public abstract T deserialize(DeserializeData<YAPIONArray> deserializeData);

}
