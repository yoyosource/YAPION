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

package yapion.hierarchy.api.storage.internal;

import lombok.NonNull;
import yapion.annotations.api.OptionalAPI;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONType;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface InternalAdvancedOperations<I, K> extends InternalAdd<I, K>, InternalRetrieve<K>, InternalRemove<I, K>, Iterable<YAPIONAnyType> {

    I itself();

    @OptionalAPI
    default I addIfAbsent(@NonNull K key, @NonNull YAPIONAnyType value) {
        if (internalContainsKey(key, YAPIONType.ANY)) return itself();
        return internalAdd(key, value);
    }

    @OptionalAPI
    default I addIfAbsent(@NonNull K key, @NonNull YAPIONType yapionType, @NonNull YAPIONAnyType value) {
        if (internalContainsKey(key, YAPIONType.ANY)) return itself();
        return internalAdd(key, value);
    }

    @OptionalAPI
    default <T> I addIfAbsent(@NonNull K key, @NonNull Class<T> type, @NonNull YAPIONAnyType value) {
        if (internalContainsKey(key, YAPIONType.ANY)) return itself();
        return internalAdd(key, value);
    }

    @OptionalAPI
    default <T extends YAPIONAnyType> I computeIfAbsent(@NonNull K key, @NonNull Function<K, T> mappingFunction) {
        if (internalContainsKey(key, YAPIONType.ANY)) return itself();
        return internalAdd(key, mappingFunction.apply(key));
    }

    @SuppressWarnings("unchecked")
    default <T extends YAPIONAnyType> I computeIfPresent(@NonNull K key, @NonNull BiFunction<K, T, T> remappingFunction) {
        if (!internalContainsKey(key, YAPIONType.ANY)) return itself();
        T newValue = remappingFunction.apply(key, (T) internalGetYAPIONAnyType(key));
        if (newValue == null) return internalRemove(key);
        return internalAdd(key, newValue);
    }

    @SuppressWarnings("unchecked")
    @OptionalAPI
    default <T extends YAPIONAnyType> I compute(@NonNull K key, @NonNull BiFunction<K, T, T> remappingFunction) {
        if (internalContainsKey(key, YAPIONType.ANY)) {
            T newValue = remappingFunction.apply(key, null);
            if (newValue == null) return itself();
            return internalAdd(key, newValue);
        } else {
            T newValue = remappingFunction.apply(key, (T) internalGetYAPIONAnyType(key));
            if (newValue == null) return internalRemove(key);
            return internalAdd(key, newValue);
        }
    }

    @SuppressWarnings("unchecked")
    default <T extends YAPIONAnyType> I merge(@NonNull K key, @NonNull T value, @NonNull BiFunction<K, T, T> remappingFunction) {
        if (internalContainsKey(key, YAPIONType.ANY)) {
            YAPIONAnyType yapionAnyType = internalGetYAPIONAnyType(key);
            if (yapionAnyType == null) {
                return internalAdd(key, value);
            }
            T newValue = remappingFunction.apply(key, (T) yapionAnyType);
            if (newValue == null) return internalRemove(key);
            return internalAdd(key, newValue);
        } else {
            return internalAdd(key, value);
        }
    }

    default YAPIONAnyType replace(K key, YAPIONAnyType value) {
        YAPIONAnyType yapionAnyType = internalGetYAPIONAnyType(key);
        internalAdd(key, value);
        return yapionAnyType;
    }

    Iterator<YAPIONAnyType> iterator();

    @Override
    default Spliterator<YAPIONAnyType> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }

    default Stream<YAPIONAnyType> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<YAPIONAnyType> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    Set<K> allKeys();

    default void forEach(@NonNull BiConsumer<K, YAPIONAnyType> action) {
        Set<K> allKeys = allKeys();
        for (K key : allKeys) {
            action.accept(key, internalGetYAPIONAnyType(key));
        }
    }

    default void replaceAll(@NonNull BiFunction<K, YAPIONAnyType, YAPIONAnyType> function) {
        Set<K> allKeys = allKeys();
        for (K key : allKeys) {
            YAPIONAnyType yapionAnyType = internalGetYAPIONAnyType(key);
            yapionAnyType = function.apply(key, yapionAnyType);
            if (yapionAnyType == null) {
                internalRemove(key);
            } else {
                internalAdd(key, yapionAnyType);
            }
        }
    }

    default boolean retainAll(@NonNull Set<K> keys) {
        Set<K> allKeys = allKeys();
        allKeys.removeAll(keys);
        boolean result = false;
        for (K key : allKeys) {
            if (internalRemove(key) != null) result = true;
        }
        return result;
    }

    default boolean removeAll(@NonNull Set<K> keys) {
        boolean result = false;
        for (K key : keys) {
            if (internalRemove(key) != null) result = true;
        }
        return result;
    }

    default boolean retainIf(@NonNull Predicate<YAPIONAnyType> filter) {
        Set<K> allKeys = allKeys();
        boolean result = false;
        for (K key : allKeys) {
            if (filter.test(internalGetYAPIONAnyType(key))) continue;
            if (internalRemove(key) != null) result = true;
        }
        return result;
    }

    default boolean retainIf(@NonNull BiPredicate<K, YAPIONAnyType> filter) {
        Set<K> allKeys = allKeys();
        boolean result = false;
        for (K key : allKeys) {
            if (filter.test(key, internalGetYAPIONAnyType(key))) continue;
            if (internalRemove(key) != null) result = true;
        }
        return result;
    }

    default boolean removeIf(@NonNull Predicate<YAPIONAnyType> filter) {
        Set<K> allKeys = allKeys();
        boolean result = false;
        for (K key : allKeys) {
            if (!filter.test(internalGetYAPIONAnyType(key))) continue;
            if (internalRemove(key) != null) result = true;
        }
        return result;
    }

    default boolean removeIf(@NonNull BiPredicate<K, YAPIONAnyType> filter) {
        Set<K> allKeys = allKeys();
        boolean result = false;
        for (K key : allKeys) {
            if (!filter.test(key, internalGetYAPIONAnyType(key))) continue;
            if (internalRemove(key) != null) result = true;
        }
        return result;
    }

}
