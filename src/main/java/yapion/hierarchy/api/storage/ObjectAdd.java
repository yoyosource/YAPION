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

package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.annotations.api.OptionalAPI;
import yapion.annotations.api.YAPIONPrimitive;
import yapion.exceptions.serializing.YAPIONClassTypeException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.storage.internal.InternalAdd;
import yapion.hierarchy.types.YAPIONValue;

public interface ObjectAdd<I, K> extends InternalAdd<I, K> {

    default I add(@NonNull K key, @NonNull YAPIONAnyType value) {
        return internalAdd(key, value);
    }

    default I add(@NonNull K key, @NonNull Class<?> value) {
        return internalAdd(key, new YAPIONValue<>(value.getTypeName()));
    }

    default <@YAPIONPrimitive T> I add(@NonNull K key, T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException();
        }
        return add(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default I addOrPointer(@NonNull K key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    default YAPIONAnyType addAndGetPrevious(@NonNull K key, @NonNull YAPIONAnyType value) {
        return internalAddAndGetPrevious(key, value);
    }

    default <@YAPIONPrimitive T> YAPIONAnyType addAndGetPrevious(@NonNull K key, T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException();
        }
        return addAndGetPrevious(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default YAPIONAnyType addOrPointerAndGetPrevious(@NonNull K key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    default I putAndGetItself(@NonNull K key, @NonNull YAPIONAnyType value) {
        return add(key, value);
    }

    default <@YAPIONPrimitive T> I putAndGetItself(@NonNull K key, T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException();
        }
        return putAndGetItself(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default I putOrPointerAndGetItself(@NonNull K key, @NonNull YAPIONAnyType value) {
        return addOrPointer(key, value);
    }

    default YAPIONAnyType put(@NonNull K key, @NonNull YAPIONAnyType value) {
        return addAndGetPrevious(key, value);
    }

    default <@YAPIONPrimitive T> YAPIONAnyType put(@NonNull K key, T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException();
        }
        return put(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default YAPIONAnyType putOrPointer(@NonNull K key, @NonNull YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(key, value);
    }

}
