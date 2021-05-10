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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AdvancedBaseStream<T> {

    @Getter(AccessLevel.PACKAGE)
    private Stream<T> stream;

    public AdvancedStream<T> filter(Predicate<? super T> predicate) {
        return AdvancedStream.of(stream.filter(predicate));
    }

    public <R> AdvancedStream<R> map(Function<? super T, ? extends R> mapper) {
        return AdvancedStream.of(stream.map(mapper));
    }

    public <R> AdvancedStream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return AdvancedStream.of(stream.flatMap(mapper));
    }

    public <R> AdvancedStream<R> advancedFlatMap(Function<? super T, ? extends AdvancedStream<? extends R>> mapper) {
        return flatMap(t -> mapper.apply(t).getStream());
    }

    public AdvancedStream<T> distinct() {
        return AdvancedStream.of(stream.distinct());
    }

    public AdvancedStream<T> sorted() {
        return AdvancedStream.of(stream.sorted());
    }

    public AdvancedStream<T> sorted(Comparator<? super T> comparator) {
        return AdvancedStream.of(stream.sorted(comparator));
    }

    public AdvancedStream<T> peek(Consumer<? super T> action) {
        return AdvancedStream.of(stream.peek(action));
    }

    public AdvancedStream<T> limit(long maxSize) {
        return AdvancedStream.of(stream.limit(maxSize));
    }

    public AdvancedStream<T> skip(long n) {
        return AdvancedStream.of(stream.skip(n));
    }

    public void forEach(Consumer<? super T> action) {
        stream.forEach(action);
    }

    public void forEachOrdered(Consumer<? super T> action) {
        stream.forEachOrdered(action);
    }

    public Object[] toArray() {
        return stream.toArray();
    }

    public <A> A[] toArray(IntFunction<A[]> generator) {
        return stream.toArray(generator);
    }

    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return stream.reduce(identity, accumulator);
    }

    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return stream.reduce(accumulator);
    }

    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return stream.reduce(identity, accumulator, combiner);
    }

    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return stream.collect(supplier, accumulator, combiner);
    }

    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return stream.collect(collector);
    }

    public Optional<T> min(Comparator<? super T> comparator) {
        return stream.min(comparator);
    }

    public Optional<T> max(Comparator<? super T> comparator) {
        return stream.max(comparator);
    }

    public long count() {
        return stream.count();
    }

    public boolean anyMatch(Predicate<? super T> predicate) {
        return stream.anyMatch(predicate);
    }

    public boolean allMatch(Predicate<? super T> predicate) {
        return stream.allMatch(predicate);
    }

    public boolean noneMatch(Predicate<? super T> predicate) {
        return stream.noneMatch(predicate);
    }

    public Optional<T> findFirst() {
        return stream.findFirst();
    }

    public Optional<T> findAny() {
        return stream.findAny();
    }

    public Iterator<T> iterator() {
        return stream.iterator();
    }

    public Spliterator<T> spliterator() {
        return stream.spliterator();
    }

    public boolean isParallel() {
        return stream.isParallel();
    }

    public Stream<T> sequential() {
        return stream.sequential();
    }

    public Stream<T> parallel() {
        return stream.parallel();
    }

    public Stream<T> unordered() {
        return stream.unordered();
    }

    public Stream<T> onClose(Runnable closeHandler) {
        return stream.onClose(closeHandler);
    }

    public void close() {
        stream.close();
    }

    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return stream.mapToInt(mapper);
    }

    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return stream.mapToLong(mapper);
    }

    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return stream.mapToDouble(mapper);
    }

    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return stream.flatMapToInt(mapper);
    }

    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return stream.flatMapToLong(mapper);
    }

    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return stream.flatMapToDouble(mapper);
    }
}
