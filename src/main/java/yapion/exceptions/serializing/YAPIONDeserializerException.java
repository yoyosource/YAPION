package yapion.exceptions.serializing;

import yapion.exceptions.YAPIONException;

public class YAPIONDeserializerException extends YAPIONException {

    public YAPIONDeserializerException() {
        super();
    }

    public YAPIONDeserializerException(String message) {
        super(message);
    }

    public YAPIONDeserializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONDeserializerException(Throwable cause) {
        super(cause);
    }

    public YAPIONDeserializerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
