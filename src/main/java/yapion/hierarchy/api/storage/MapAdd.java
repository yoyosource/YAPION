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
import yapion.exceptions.serializing.YAPIONClassTypeException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.internal.InternalAdd;
import yapion.hierarchy.types.YAPIONValue;

public interface MapAdd<I, K> extends InternalAdd<I, K> {

    default <@YAPIONEveryType C, @YAPIONEveryType V> I add(@NonNull C key, @NonNull V value) {
        if (YAPIONValue.validType(key)) {
            return add(new YAPIONValue<>(key), value);
        } else if (!(key instanceof YAPIONAnyType)) {
            throw new YAPIONClassTypeException();
        }
        if (YAPIONValue.validType(value)) {
            return add(key, new YAPIONValue<>(value));
        } else if (!(value instanceof YAPIONAnyType)) {
            throw new YAPIONClassTypeException();
        }
        return internalAdd((K) key, (YAPIONAnyType) value);
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default <@YAPIONEveryType C> I addOrPointer(@NonNull C key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    default <@YAPIONEveryType C, @YAPIONEveryType V> YAPIONAnyType addAndGetPrevious(@NonNull C key, @NonNull V value) {
        if (YAPIONValue.validType(key)) {
            return addAndGetPrevious(new YAPIONValue<>(key), value);
        } else if (!(key instanceof YAPIONAnyType)) {
            throw new YAPIONClassTypeException();
        }
        if (YAPIONValue.validType(value)) {
            return addAndGetPrevious(key, new YAPIONValue<>(value));
        } else if (!(value instanceof YAPIONAnyType)) {
            throw new YAPIONClassTypeException();
        }
        return internalAddAndGetPrevious((K) key, (YAPIONAnyType) value);
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default <@YAPIONEveryType C> YAPIONAnyType addOrPointerAndGetPrevious(@NonNull C key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    default <@YAPIONEveryType C, @YAPIONEveryType V> I putAndGetItself(@NonNull C key, @NonNull V value) {
        return add(key, value);
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default <@YAPIONEveryType C> I putOrPointerAndGetItself(@NonNull C key, @NonNull YAPIONAnyType value) {
        return addOrPointer(key, value);
    }

    default <@YAPIONEveryType C, @YAPIONEveryType V> YAPIONAnyType put(@NonNull C key, @NonNull V value) {
        return addAndGetPrevious(key, value);
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    default <@YAPIONEveryType C> YAPIONAnyType putOrPointer(@NonNull C key, @NonNull YAPIONAnyType value) {
        return addOrPointerAndGetPrevious(key, value);
    }

}
