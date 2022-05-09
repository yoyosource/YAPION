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

package yapion.pathn;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PathContext {

    public static PathContext of(YAPIONAnyType... yapionAnyTypes) {
        return new PathContext(true, Arrays.asList(yapionAnyTypes), false, new ArrayList<>(), Arrays.asList(yapionAnyTypes).stream(), new AtomicLong(), new IdentityHashMap<>());
    }

    public static PathContext of(List<YAPIONAnyType> yapionAnyTypes) {
        return new PathContext(true, new ArrayList<>(yapionAnyTypes), false, new ArrayList<>(), new ArrayList<>(yapionAnyTypes).stream(), new AtomicLong(), new IdentityHashMap<>());
    }

    private boolean isRoot;
    private List<YAPIONAnyType> rootElements;
    private boolean isCurrent;
    private List<YAPIONAnyType> currentElement;
    private Stream<YAPIONAnyType> current;
    private AtomicLong reverseIdentifier;
    private Map<YAPIONAnyType, Long> reverseSpreadMap;

    public PathContext root() {
        return new PathContext(true, rootElements, false, currentElement, rootElements.stream(), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext current() {
        return new PathContext(false, rootElements, true, currentElement, currentElement.stream(), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext empty() {
        return new PathContext(false, rootElements, false, currentElement, Stream.empty(), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext removeIf(Predicate<YAPIONAnyType> filter) {
        return retainIf(filter.negate());
    }

    public PathContext retainIf(Predicate<YAPIONAnyType> filter) {
        return new PathContext(false, rootElements, false, currentElement, current.filter(filter).filter(Objects::nonNull), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext map(Function<YAPIONAnyType, List<YAPIONAnyType>> mapper) {
        return new PathContext(false, rootElements, false, currentElement, current.map(mapper).filter(Objects::nonNull).flatMap(List::stream).filter(Objects::nonNull), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext streamMap(Function<YAPIONAnyType, Stream<YAPIONAnyType>> mapper) {
        return new PathContext(false, rootElements, false, currentElement, current.map(mapper).filter(Objects::nonNull).flatMap(Function.identity()).filter(Objects::nonNull), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext mapViaPathContext(Function<YAPIONAnyType, Stream<PathContext>> mapper) {
        return new PathContext(false, rootElements, false, currentElement, current.flatMap(mapper).filter(Objects::nonNull).flatMap(pathContext -> pathContext.current).filter(Objects::nonNull), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext distinct() {
        return new PathContext(false, rootElements, false, currentElement, current.distinct(), reverseIdentifier, reverseSpreadMap);
    }

    public Stream<YAPIONAnyType> stream() {
        return current;
    }

    public PathContext stream(UnaryOperator<Stream<YAPIONAnyType>> streamer) {
        return new PathContext(false, rootElements, false, currentElement, streamer.apply(current).filter(Objects::nonNull), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext with(YAPIONAnyType element) {
        return new PathContext(false, rootElements, false, currentElement, Stream.of(element), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext with(List<YAPIONAnyType> elements) {
        return new PathContext(false, rootElements, false, currentElement, elements.stream(), reverseIdentifier, reverseSpreadMap);
    }

    public PathContext withAndSetCurrent(YAPIONAnyType element) {
        return new PathContext(false, rootElements, true, Arrays.asList(element), Stream.of(element), reverseIdentifier, reverseSpreadMap);
    }

    public long getReverseIdentifier() {
        return reverseIdentifier.getAndIncrement();
    }

    public void setReverseIdentifier(YAPIONAnyType element, long reverseIdentifier) {
        reverseSpreadMap.put(element, reverseIdentifier);
    }

    public long getReverseIdentifier(YAPIONAnyType element) {
        return reverseSpreadMap.getOrDefault(element, -1L);
    }

    public List<YAPIONAnyType> eval() {
        return current.collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "PathContext{" +
                "isRoot=" + isRoot +
                ", rootElements=" + rootElements.size() +
                ", isCurrent=" + isCurrent +
                ", currentElement=" + currentElement.size() +
                ", current=" + current +
                ", reverseIdentifier=" + reverseIdentifier +
                ", reverseSpreads=" + reverseSpreadMap.size() +
                '}';
    }
}
