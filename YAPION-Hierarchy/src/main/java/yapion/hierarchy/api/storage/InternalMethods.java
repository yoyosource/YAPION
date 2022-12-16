/*
 * Copyright 2022 yoyosource
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
import yapion.annotations.api.InternalAPI;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONType;

@InternalAPI
public interface InternalMethods<I, K> {

    // -------- Add --------

    I internalAdd(@NonNull K key, @NonNull YAPIONAnyType value);

    YAPIONAnyType internalAddAndGetPrevious(@NonNull K key, @NonNull YAPIONAnyType value);

    // -------- Remove --------

    I internalRemove(@NonNull K key);

    YAPIONAnyType internalRemoveAndGet(@NonNull K key);

    I internalClear();

    // -------- Get --------

    default boolean internalContainsKey(@NonNull K key) {
        return internalContainsKey(key, YAPIONType.ANY);
    }

    boolean internalContainsKey(@NonNull K key, YAPIONType yapionType);

    <T> boolean internalContainsKey(@NonNull K key, Class<T> type);

    boolean internalContainsValue(@NonNull YAPIONAnyType yapionAnyType);

    YAPIONAnyType internalGetAnyType(@NonNull K key);
}