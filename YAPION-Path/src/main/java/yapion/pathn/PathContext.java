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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PathContext {

    public static PathContext of(YAPIONAnyType... yapionAnyTypes) {
        return new PathContext(true, Arrays.asList(yapionAnyTypes), Arrays.asList(yapionAnyTypes).stream());
    }

    public static PathContext of(List<YAPIONAnyType> yapionAnyTypes) {
        return new PathContext(true, new ArrayList<>(yapionAnyTypes), new ArrayList<>(yapionAnyTypes).stream());
    }

    private boolean isRoot;
    private List<YAPIONAnyType> rootElements;
    private Stream<YAPIONAnyType> current;

    public PathContext root() {
        return new PathContext(true, rootElements, rootElements.stream());
    }

    public PathContext empty() {
        return new PathContext(false, rootElements, Stream.empty());
    }

    public PathContext removeIf(Predicate<YAPIONAnyType> filter) {
        return retainIf(filter.negate());
    }

    public PathContext retainIf(Predicate<YAPIONAnyType> filter) {
        return new PathContext(false, rootElements, current.filter(filter));
    }

    public PathContext map(Function<YAPIONAnyType, List<YAPIONAnyType>> mapper) {
        return new PathContext(false, rootElements, current.map(mapper).filter(Objects::nonNull).flatMap(List::stream));
    }

    public PathContext streamMap(Function<YAPIONAnyType, Stream<YAPIONAnyType>> mapper) {
        return new PathContext(false, rootElements, current.map(mapper).filter(Objects::nonNull).flatMap(Function.identity()));
    }

    public PathContext mapViaPathContext(Function<YAPIONAnyType, Stream<PathContext>> mapper) {
        return new PathContext(false, rootElements, current.flatMap(mapper).filter(Objects::nonNull).flatMap(pathContext -> pathContext.current));
    }

    public PathContext distinct() {
        return new PathContext(false, rootElements, current.distinct());
    }

    public Stream<YAPIONAnyType> stream() {
        return current;
    }

    public PathContext stream(UnaryOperator<Stream<YAPIONAnyType>> streamer) {
        return new PathContext(false, rootElements, streamer.apply(current));
    }

    public PathContext with(List<YAPIONAnyType> elements) {
        return new PathContext(false, rootElements, elements.stream());
    }

    public List<YAPIONAnyType> eval() {
        return current.collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "PathContext{" +
                "isRoot=" + isRoot +
                ", rootElements=" + rootElements +
                ", current=" + current +
                '}';
    }
}
