package yapion.exceptions.utils;

import yapion.exceptions.YAPIONException;

import java.lang.reflect.InvocationTargetException;

public class YAPIONReflectionInvocationException extends YAPIONException {

    private Throwable target;

    public YAPIONReflectionInvocationException(InvocationTargetException invocationTargetException) {
        super(invocationTargetException.getTargetException().getMessage(), invocationTargetException.getTargetException() );
        target = invocationTargetException.getTargetException();
    }

    public Throwable getTargetException() {
        return target;
    }

}
