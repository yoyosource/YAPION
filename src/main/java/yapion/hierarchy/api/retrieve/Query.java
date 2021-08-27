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

package yapion.hierarchy.api.retrieve;

import lombok.NonNull;
import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.function.Function;

@FunctionalInterface
public interface Query<I extends YAPIONAnyType, O extends YAPIONAnyType> {

    O get(I input);

    default <K extends YAPIONAnyType> Query<I, K> chainQuery(@NonNull Query<O, K> next) {
        return input -> {
            O value = Query.this.get(input);
            if (value == null) return null;
            return next.get(value);
        };
    }

    default <K extends YAPIONAnyType> Query<I, K> chainFunction(@NonNull Function<O, K> next) {
        return chainQuery(next::apply);
    }

    default <K extends YAPIONAnyType> Query<K, O> backChainQuery(@NonNull Query<K, I> previous) {
        return input -> {
            I value = previous.get(input);
            if (value == null) return null;
            return Query.this.get(value);
        };
    }

    default <K extends YAPIONAnyType> Query<K, O> backChainFunction(@NonNull Function<K, I> previous) {
        return backChainQuery(previous::apply);
    }
}
