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

package yapion.pathn.builder;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.pathn.PathElement;
import yapion.pathn.impl.*;
import yapion.pathn.impl.array.Range;
import yapion.pathn.impl.object.Contains;
import yapion.pathn.impl.object.EndsWith;
import yapion.pathn.impl.object.Regex;
import yapion.pathn.impl.object.StartWith;

public interface SelectorBuilder<I extends SelectorBuilder<I, B>, B> {

    I add(PathElement pathElement);

    I itself();

    default I select(String selector) {
        return add(new Element(selector));
    }
    default I select(int selector) {
        return add(new Element(selector));
    }

    default I contains(String selector) {
        return add(new Contains(selector));
    }
    default I endsWith(String selector) {
        return add(new EndsWith(selector));
    }
    default I startsWith(String selector) {
        return add(new StartWith(selector));
    }
    default I regex(String selector) {
        return add(new Regex(selector));
    }

    default I min(int min) {
        return add(Range.min(min));
    }
    default I max(int max) {
        return add(Range.max(max));
    }
    default I range(int min, int max) {
        return add(Range.range(min, max));
    }

    default I each() {
        return add(new AnyElement());
    }
    default I any() {
        return each();
    }
    default I eachAndDeeper() {
        return add(new AnyDeeperElement());
    }
    default I anyDeeper() {
        return eachAndDeeper();
    }

    default I distinct() {
        return add(new DistinctElements());
    }
    default I identityDistinct() {
        return add(new IdentityDistinctElements());
    }

    default I type(Class<? extends YAPIONAnyType> type) {
        return add(new ElementType(type));
    }
    default I objects() {
        return type(YAPIONObject.class);
    }
    default I arrays() {
        return type(YAPIONArray.class);
    }
    default I values() {
        return type(YAPIONValue.class);
    }

    default I root() {
        return add(new RootElement());
    }

    default I streamEntries() {
        return add(new Spread());
    }
    default I spread() {
        return streamEntries();
    }

    default SubSelector<I> subselect() {
        return new SubSelector<>(itself());
    }

    default AnyOfSelector<I> anyOf() {
        return new AnyOfSelector<>(itself());
    }
    default AllOfSelector<I> allOf() {
        return new AllOfSelector<>(itself());
    }
    default AllWithSelector<I> allWith() {
        return new AllWithSelector<>(itself());
    }
    default AnyWithSelector<I> anyWith() {
        return new AnyWithSelector<>(itself());
    }

    default WhenSelector<I> when() {
        return new WhenSelector<>(itself());
    }

    default ValueSelector<I> value() {
        return new ValueSelector<>(itself());
    }

    B build();
}
