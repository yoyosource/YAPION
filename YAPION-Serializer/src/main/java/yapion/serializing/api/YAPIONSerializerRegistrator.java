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
     * This method is a shortcut to register a {@link SerializerList}.
     *
     * @param serializerList the serializer to register
     */
    default void register(SerializerList<?> serializerList) {
        SerializeManager.add(serializerList);
    }

    /**
     * This method is a shortcut to register a {@link SerializerMap}.
     *
     * @param serializerMap the serializer to register
     */
    default void register(SerializerMap<?> serializerMap) {
        SerializeManager.add(serializerMap);
    }

    /**
     * This method is a shortcut to register a {@link SerializerObject}.
     *
     * @param serializerObject the serializer to register
     */
    default void register(SerializerObject<?> serializerObject) {
        SerializeManager.add(serializerObject);
    }

    /**
     * This method is a shortcut to register a {@link SerializerQueue}.
     *
     * @param serializerQueue the serializer to register
     */
    default void register(SerializerQueue<?> serializerQueue) {
        SerializeManager.add(serializerQueue);
    }

    /**
     * This method is a shortcut to register a {@link SerializerSet}.
     *
     * @param serializerSet the serializer to register
     */
    default void register(SerializerSet<?> serializerSet) {
        SerializeManager.add(serializerSet);
    }

    /**
     * This method is a shortcut to register a {@link InstanceFactory}.
     *
     * @param instanceFactoryInterface the factory to register
     */
    default void register(InstanceFactory<?> instanceFactoryInterface) {
        SerializeManager.add(instanceFactoryInterface);
    }

}
