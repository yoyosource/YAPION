// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.exceptions.serializing;

import yapion.exceptions.YAPIONException;

public class YAPIONDataLossException extends YAPIONException {

    public YAPIONDataLossException() {
        super();
    }

    public YAPIONDataLossException(String message) {
        super(message);
    }

    public YAPIONDataLossException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONDataLossException(Throwable cause) {
        super(cause);
    }

    protected YAPIONDataLossException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}