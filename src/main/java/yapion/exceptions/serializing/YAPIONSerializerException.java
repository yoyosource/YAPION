// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.exceptions.serializing;

import yapion.exceptions.YAPIONException;

public class YAPIONSerializerException extends YAPIONException {

    public YAPIONSerializerException() {
        super();
    }

    public YAPIONSerializerException(String message) {
        super(message);
    }

    public YAPIONSerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONSerializerException(Throwable cause) {
        super(cause);
    }

    public YAPIONSerializerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}