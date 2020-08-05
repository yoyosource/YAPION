// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.exceptions.value;

import yapion.exceptions.YAPIONException;

public class YAPIONRecursionException extends YAPIONException {

    public YAPIONRecursionException() {
        super();
    }

    public YAPIONRecursionException(String message) {
        super(message);
    }

    public YAPIONRecursionException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONRecursionException(Throwable cause) {
        super(cause);
    }

    protected YAPIONRecursionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}