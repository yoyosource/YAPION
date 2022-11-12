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
import yapion.annotations.api.YAPIONEveryType;
import yapion.annotations.api.YAPIONPrimitive;
import yapion.exceptions.serializing.YAPIONClassTypeException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;

public interface ArrayMethods<I, K> extends ObjectMethods<I, K> {

    // -------- Add --------

    I add(@NonNull YAPIONAnyType value);

    default <@YAPIONPrimitive T> I add(T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException("The type '" + value.getClass().getTypeName() + "' is not a valid YAPIONPrimitive");
        }
        return add(new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default I addOrPointer(@NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    I set(@NonNull K key, @NonNull YAPIONAnyType value);

    default <@YAPIONPrimitive T> I set(@NonNull K key, T value) {
        if (!YAPIONValue.validType(value)) {
            throw new YAPIONClassTypeException("The type '" + value.getClass().getTypeName() + "' is not a valid YAPIONPrimitive");
        }
        return set(key, new YAPIONValue<>(value));
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default I setOrPointer(@NonNull K key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    // -------- Remove --------

    default I remove(@NonNull K key) {
        return internalRemove(key);
    }

    default YAPIONAnyType removeAndGet(@NonNull K key) {
        return internalRemoveAndGet(key);
    }

    default I clear() {
        return internalClear();
    }

    // -------- Get --------

    <@YAPIONEveryType T> int indexOf(T element);

    <@YAPIONEveryType T> int lastIndexOf(T element);
}
