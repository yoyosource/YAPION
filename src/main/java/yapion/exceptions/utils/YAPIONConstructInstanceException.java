// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

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