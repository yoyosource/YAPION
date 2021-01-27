// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.exceptions.parser;

import yapion.exceptions.YAPIONException;

public class YAPIONParserException extends YAPIONException {

    public YAPIONParserException() {
        super();
    }

    public YAPIONParserException(String message) {
        super(message);
    }

    public YAPIONParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONParserException(Throwable cause) {
        super(cause);
    }

    protected YAPIONParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}