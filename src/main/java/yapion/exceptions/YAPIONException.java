// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.exceptions;

public class YAPIONException extends RuntimeException {

    public YAPIONException() {
        super();
    }

    public YAPIONException(String message) {
        super(message);
    }

    public YAPIONException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONException(Throwable cause) {
        super(cause);
    }

    protected YAPIONException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}