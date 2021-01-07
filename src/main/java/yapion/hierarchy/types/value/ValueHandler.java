package yapion.hierarchy.types.value;

import java.util.Optional;

public interface ValueHandler<T> {

    String output(T t);

    Optional<T> preParse(String s);

    Optional<T> parse(String s);

    long referenceValue();

}
