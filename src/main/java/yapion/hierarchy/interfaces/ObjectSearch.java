package yapion.hierarchy.interfaces;

import yapion.hierarchy.YAPIONAny;

import java.util.Optional;

public interface ObjectSearch {

    Optional<YAPIONSearchResult<? extends YAPIONAny>> get(String... s);

    Optional<ObjectSearch.YAPIONSearchResult<? extends YAPIONAny>> get(String key);

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
