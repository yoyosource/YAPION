// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.exceptions.utils;

import yapion.exceptions.YAPIONException;

public class YAPIONIOException extends YAPIONException {

    public YAPIONIOException() {
        super();
    }

    public YAPIONIOException(String message) {
        super(message);
    }

    public YAPIONIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONIOException(Throwable cause) {
        super(cause);
    }

}