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

import yapion.serializing.SerializeManager;

/**
 * This interface should be used to give you serializer extensions predefined entry points to register
 * their Serializer for YAPION. This entry point should not call any other entry points. It should
 * never load anything outside your packages.
 */
public interface YAPIONSerializerRegistrator {

    /**
     * This method will be called by YAPION to register any Serializer.
     */
    void register();

    /**
     * This method is a shortcut to register a {@link SerializerListInterface}.
     *
     * @param serializerListInterface the serializer to register
     */
    default void register(SerializerListInterface<?> serializerListInterface) {
        SerializeManager.add(serializerListInterface);
    }

    /**
     * This method is a shortcut to register a {@link SerializerMapInterface}.
     *
     * @param serializerMapInterface the serializer to register
     */
    default void register(SerializerMapInterface<?> serializerMapInterface) {
        SerializeManager.add(serializerMapInterface);
    }

    /**
     * This method is a shortcut to register a {@link SerializerObjectInterface}.
     *
     * @param serializerObjectInterface the serializer to register
     */
    default void register(SerializerObjectInterface<?> serializerObjectInterface) {
        SerializeManager.add(serializerObjectInterface);
    }

    /**
     * This method is a shortcut to register a {@link SerializerQueueInterface}.
     *
     * @param serializerQueueInterface the serializer to register
     */
    default void register(SerializerQueueInterface<?> serializerQueueInterface) {
        SerializeManager.add(serializerQueueInterface);
    }

    /**
     * This method is a shortcut to register a {@link SerializerSetInterface}.
     *
     * @param serializerSetInterface the serializer to register
     */
    default void register(SerializerSetInterface<?> serializerSetInterface) {
        SerializeManager.add(serializerSetInterface);
    }

    /**
     * This method is a shortcut to register a {@link InstanceFactoryInterface}.
     *
     * @param instanceFactoryInterface the factory to register
     */
    default void register(InstanceFactoryInterface<?> instanceFactoryInterface) {
        SerializeManager.add(instanceFactoryInterface);
    }

}
