package yapion.exceptions.packet;

import yapion.exceptions.YAPIONException;

public class YAPIONPacketException extends YAPIONException {

    public YAPIONPacketException() {
        super();
    }

    public YAPIONPacketException(String message) {
        super(message);
    }

    public YAPIONPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONPacketException(Throwable cause) {
        super(cause);
    }

    protected YAPIONPacketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
