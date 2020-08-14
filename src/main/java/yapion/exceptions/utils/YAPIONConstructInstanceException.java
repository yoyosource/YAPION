package yapion.exceptions.utils;

import yapion.exceptions.YAPIONException;

public class YAPIONConstructInstanceException extends YAPIONException {

    public YAPIONConstructInstanceException() {
        super();
    }

    public YAPIONConstructInstanceException(String message) {
        super(message);
    }

    public YAPIONConstructInstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONConstructInstanceException(Throwable cause) {
        super(cause);
    }

    protected YAPIONConstructInstanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
