// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.typeinterfaces;

import yapion.hierarchy.typegroups.YAPIONAnyType;

import java.util.Optional;

public interface ObjectSearch {

    Optional<YAPIONSearchResult<?>> get(String... s);

    Optional<YAPIONSearchResult<?>> get(String key);

    class YAPIONSearchResult<T extends YAPIONAnyType> {

        public final T value;

        public YAPIONSearchResult(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "SearchResult{" + value + '}';
        }

    }

}