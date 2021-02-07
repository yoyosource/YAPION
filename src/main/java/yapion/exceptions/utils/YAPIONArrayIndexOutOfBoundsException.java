// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.exceptions.utils;

import yapion.exceptions.YAPIONException;

public class YAPIONArrayIndexOutOfBoundsException extends YAPIONException {

    public YAPIONArrayIndexOutOfBoundsException() {
        super();
    }

    public YAPIONArrayIndexOutOfBoundsException(String message) {
        super(message);
    }

    public YAPIONArrayIndexOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONArrayIndexOutOfBoundsException(Throwable cause) {
        super(cause);
    }

}