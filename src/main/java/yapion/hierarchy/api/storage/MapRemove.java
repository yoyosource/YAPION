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
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface MapRemove<I> extends ObjectRemove<I, YAPIONAnyType> {

    default I remove(@NonNull String key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull char key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull boolean key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull byte key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull short key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull int key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull long key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull BigInteger key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull float key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull double key) {
        return remove(new YAPIONValue<>(key));
    }

    default I remove(@NonNull BigDecimal key) {
        return remove(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull String key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull char key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull boolean key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull byte key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull short key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull int key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull long key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull BigInteger key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull float key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull double key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

    default YAPIONAnyType removeAndGet(@NonNull BigDecimal key) {
        return removeAndGet(new YAPIONValue<>(key));
    }

}
