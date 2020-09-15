package yapion.hierarchy.interfaces;

import yapion.hierarchy.typegroups.YAPIONAnyType;

import java.util.Optional;

public interface ObjectSearch {

    Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(String... s);

    Optional<ObjectSearch.YAPIONSearchResult<? extends YAPIONAnyType>> get(String key);

    class YAPIONSearchResult<T> {

        public final T value;

        public YAPIONSearchResult(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "YAPIONSearch{" +
                    "value=" + value +
                    '}';
        }

    }


}
