// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.exceptions.value;

import yapion.exceptions.YAPIONException;

public class YAPIONPointerException extends YAPIONException {

    public YAPIONPointerException() {
        super();
    }

    public YAPIONPointerException(String message) {
        super(message);
    }

    public YAPIONPointerException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONPointerException(Throwable cause) {
        super(cause);
    }

}