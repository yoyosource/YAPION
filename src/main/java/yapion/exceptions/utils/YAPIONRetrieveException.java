// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.exceptions.utils;

import yapion.exceptions.YAPIONException;

public class YAPIONRetrieveException extends YAPIONException {

    public YAPIONRetrieveException() {
        super();
    }

    public YAPIONRetrieveException(String message) {
        super(message);
    }

    public YAPIONRetrieveException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONRetrieveException(Throwable cause) {
        super(cause);
    }

    protected YAPIONRetrieveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}