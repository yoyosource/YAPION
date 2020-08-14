// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.exceptions.utils;

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