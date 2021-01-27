// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.exceptions.serializing;

import yapion.exceptions.YAPIONException;

public class YAPIONClassTypeException extends YAPIONException {

    public YAPIONClassTypeException() {
        super();
    }

    public YAPIONClassTypeException(String message) {
        super(message);
    }

    public YAPIONClassTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONClassTypeException(Throwable cause) {
        super(cause);
    }

    protected YAPIONClassTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}