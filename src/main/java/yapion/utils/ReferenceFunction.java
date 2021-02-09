package yapion.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.function.ToLongFunction;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReferenceFunction {

    private final ToLongFunction<String> referenceFunction;

    public long stringToReferenceValue(String s) {
        return referenceFunction.applyAsLong(s);
    }

}
