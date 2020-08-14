// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.exceptions.utils;

import yapion.exceptions.YAPIONException;

public class YAPIONReflectionException extends YAPIONException {

    public YAPIONReflectionException() {
        super();
    }

    public YAPIONReflectionException(String message) {
        super(message);
    }

    public YAPIONReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONReflectionException(Throwable cause) {
        super(cause);
    }

    protected YAPIONReflectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}