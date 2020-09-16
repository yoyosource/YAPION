package yapion.hierarchy.interfaces;

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
