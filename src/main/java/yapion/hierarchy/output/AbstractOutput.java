package yapion.hierarchy.output;

import yapion.exceptions.YAPIONException;

import java.nio.charset.StandardCharsets;

public abstract class AbstractOutput {

    public abstract AbstractOutput consume(String s);

    protected final byte[] bytes(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    protected final void validCall() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length < 4) {
            throw new YAPIONException("Invalid calling class");
        }
        if (stackTraceElements[3].getClassName().startsWith("yapion.hierarchy.types.YAPION")) {
            return;
        }
        throw new YAPIONException("Invalid calling class");
    }

}
