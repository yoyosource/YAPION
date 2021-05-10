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

package yapion.hierarchy.api.stream;

import yapion.hierarchy.api.stream.util.Pair;
import yapion.hierarchy.api.stream.util.UnaryPair;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AdvancedStream<T> extends AdvancedBaseStream<T> {

    private Random random = new Random();

    public AdvancedStream(Stream<T> stream) {
        super(stream);
    }

    public static <T> AdvancedStream<T> of(Stream<T> stream) {
        return new AdvancedStream<>(stream);
    }

    public static <T> AdvancedStream<T> of(Iterable<T> iterable) {
        return new AdvancedStream<>(StreamSupport.stream(iterable.spliterator(), false));
    }

    public <K> AdvancedStream<K> ofType(Class<K> type) {
        return filter(t -> type.isAssignableFrom(t == null ? null : t.getClass())).map(type::cast);
    }

    public <K> AdvancedStream<K> testUnwrap(PredicateFunction<T, K> predicateFunction) {
        return map(predicateFunction).filter(Optional::isPresent).map(Optional::get);
    }

    public <K> AdvancedStream<K> testUnwrap(Predicate<T> predicate, Function<T, K> mapper) {
        return filter(predicate).map(mapper);
    }

    public AdvancedStream<UnaryPair<T>> pair() {
        AtomicReference<T> current = new AtomicReference<>(null);
        AtomicBoolean filter = new AtomicBoolean(false);
        return filter(t -> {
            if (!filter.get()) current.set(t);
            return filter.getAndSet(!filter.get());
        }).map(t -> new UnaryPair<>(current.get(), t));
    }

    public <K> AdvancedStream<Pair<K, T>> keyPair(Function<T, K> function) {
        return map(t -> new Pair<>(function.apply(t), t));
    }

    public AdvancedStream<UnaryPair<T>> keyPair(UnaryOperator<T> function) {
        return map(t -> new UnaryPair<>(function.apply(t), t));
    }

    public <K> AdvancedStream<Pair<T, K>> valuePair(Function<T, K> function) {
        return map(t -> new Pair<>(t, function.apply(t)));
    }

    public AdvancedStream<UnaryPair<T>> valuePair(UnaryOperator<T> function) {
        return map(t -> new UnaryPair<>(t, function.apply(t)));
    }

    public AdvancedStream<T> shuffle() {
        return sorted((o1, o2) -> random.nextInt(2) - 1);
    }

    public AdvancedStream<T> peekCollect(List<T> list) {
        return peek(list::add);
    }

    public AdvancedStream<T> mutate(UnaryOperator<T> unaryOperator) {
        return map(unaryOperator);
    }

    public List<T> toList() {
        return collect(Collectors.toList());
    }

    public Set<T> toSet() {
        return collect(Collectors.toSet());
    }

    public <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory) {
        return collect(Collectors.toCollection(collectionFactory));
    }

    public String joining(String delimiter) {
        return map(Object::toString).collect(Collectors.joining(delimiter));
    }

    public void eval() {
        forEach(t -> {});
    }
}
