// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.exceptions.serializing;

import yapion.exceptions.YAPIONException;

public class YAPIONPackageTypeException extends YAPIONException {

    public YAPIONPackageTypeException() {
        super();
    }

    public YAPIONPackageTypeException(String message) {
        super(message);
    }

    public YAPIONPackageTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONPackageTypeException(Throwable cause) {
        super(cause);
    }

    protected YAPIONPackageTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}