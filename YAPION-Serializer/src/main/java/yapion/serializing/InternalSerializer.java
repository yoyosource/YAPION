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

import yapion.annotations.api.InternalAPI;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

@InternalAPI
public interface InternalSerializer<T> {

    default void init() {
        // Used to initialize some internal state if needed
    }
    
    Class<?> type();

    @InternalAPI
    default Class<?> defaultImplementation() {
        return null;
    }

    @InternalAPI
    default Class<?> primitiveType() {
        return null;
    }

    default Class<?> interfaceType() {
        return null;
    }

    default Class<?> classType() {
        return null;
    }

    @InternalAPI
    default boolean empty() {
        return false;
    }

    @InternalAPI
    default boolean finished() {
        return true;
    }

    @InternalAPI
    default boolean saveWithoutAnnotation() {
        return false;
    }

    @InternalAPI
    default boolean loadWithoutAnnotation() {
        return false;
    }

    @InternalAPI
    default boolean createWithObjenesis() {
        return false;
    }

    YAPIONAnyType serialize(SerializeData<T> serializeData);

    default void serialize(SerializeData<T> serializeData, ResolutionGraph<Object, YAPIONAnyType> resolutionGraph) {}

    T deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData);

    default void deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData, ResolutionGraph<YAPIONAnyType, Object> resolutionGraph) {}
}
