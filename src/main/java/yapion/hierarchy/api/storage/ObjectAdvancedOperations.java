package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.hierarchy.api.OptionalAPI;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONType;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ObjectAdvancedOperations<I, K> extends ObjectAdd<I, K>, ObjectRetrieve<K>, ObjectRemove<I, K>, Iterable<YAPIONAnyType> {

    I itself();

    @OptionalAPI
    default I addIfAbsent(@NonNull K key, @NonNull YAPIONAnyType value) {
        if (hasValue(key)) return itself();
        return add(key, value);
    }

    @OptionalAPI
    default I addIfAbsent(@NonNull K key, @NonNull YAPIONType yapionType, @NonNull YAPIONAnyType value) {
        if (hasValue(key, yapionType)) return itself();
        return add(key, value);
    }

    @OptionalAPI
    default <T> I addIfAbsent(@NonNull K key, @NonNull Class<T> type, @NonNull YAPIONAnyType value) {
        if (hasValue(key, type)) return itself();
        return add(key, value);
    }

    @OptionalAPI
    default <T extends YAPIONAnyType> I computeIfAbsent(@NonNull K key, @NonNull Function<K, T> mappingFunction) {
        if (hasValue(key)) return itself();
        return add(key, mappingFunction.apply(key));
    }

    @SuppressWarnings("unchecked")
    default <T extends YAPIONAnyType> I computeIfPresent(@NonNull K key, @NonNull BiFunction<K, T, T> remappingFunction) {
        if (!hasValue(key)) return itself();
        T newValue = remappingFunction.apply(key, (T) getYAPIONAnyType(key));
        if (newValue == null) return remove(key);
        return add(key, newValue);
    }

    @SuppressWarnings("unchecked")
    @OptionalAPI
    default <T extends YAPIONAnyType> I compute(@NonNull K key, @NonNull BiFunction<K, T, T> remappingFunction) {
        if (hasValue(key)) {
            T newValue = remappingFunction.apply(key, null);
            if (newValue == null) return itself();
            return add(key, newValue);
        } else {
            T newValue = remappingFunction.apply(key, (T) getYAPIONAnyType(key));
            if (newValue == null) return remove(key);
            return add(key, newValue);
        }
    }

    @SuppressWarnings("unchecked")
    default <T extends YAPIONAnyType> I merge(@NonNull K key, @NonNull T value, @NonNull BiFunction<K, T, T> remappingFunction) {
        if (hasValue(key)) {
            YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
            if (yapionAnyType == null) {
                return add(key, value);
            }
            T newValue = remappingFunction.apply(key, (T) yapionAnyType);
            if (newValue == null) return remove(key);
            return add(key, newValue);
        } else {
            return add(key, value);
        }
    }

    default YAPIONAnyType replace(K key, YAPIONAnyType value) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        add(key, value);
        return yapionAnyType;
    }

    Iterator<YAPIONAnyType> iterator();

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
            action.accept(key, getYAPIONAnyType(key));
        }
    }

    default void replaceAll(@NonNull BiFunction<K, YAPIONAnyType, YAPIONAnyType> function) {
        Set<K> allKeys = allKeys();
        for (K key : allKeys) {
            YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
            yapionAnyType = function.apply(key, yapionAnyType);
            if (yapionAnyType == null) {
                remove(key);
            } else {
                add(key, yapionAnyType);
            }
        }
    }

    default boolean retainAll(@NonNull Set<K> keys) {
        Set<K> allKeys = allKeys();
        boolean result = false;
        for (K key : allKeys) {
            if (keys.contains(key)) continue;
            remove(key);
            result = true;
        }
        return result;
    }

    default boolean removeAll(@NonNull Set<K> keys) {
        boolean result = false;
        for (K key : keys) {
            if (!hasValue(key)) continue;
            remove(key);
            result = true;
        }
        return result;
    }

    default boolean retainIf(@NonNull Predicate<YAPIONAnyType> filter) {
        Set<K> allKeys = allKeys();
        boolean result = false;
        for (K key : allKeys) {
            if (filter.test(getYAPIONAnyType(key))) continue;
            remove(key);
            result = true;
        }
        return result;
    }

    default boolean retainIf(@NonNull BiPredicate<K, YAPIONAnyType> filter) {
        Set<K> allKeys = allKeys();
        boolean result = false;
        for (K key : allKeys) {
            if (filter.test(key, getYAPIONAnyType(key))) continue;
            remove(key);
            result = true;
        }
        return result;
    }

    default boolean removeIf(@NonNull Predicate<YAPIONAnyType> filter) {
        Set<K> allKeys = allKeys();
        boolean result = false;
        for (K key : allKeys) {
            if (!filter.test(getYAPIONAnyType(key))) continue;
            remove(key);
            result = true;
        }
        return result;
    }

    default boolean removeIf(@NonNull BiPredicate<K, YAPIONAnyType> filter) {
        Set<K> allKeys = allKeys();
        boolean result = false;
        for (K key : allKeys) {
            if (!filter.test(key, getYAPIONAnyType(key))) continue;
            remove(key);
            result = true;
        }
        return result;
    }

}
