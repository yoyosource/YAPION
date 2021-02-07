// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.exceptions.parser;

import yapion.exceptions.YAPIONException;

public class JSONParserException extends YAPIONException {

    public JSONParserException() {
        super();
    }

    public JSONParserException(String message) {
        super(message);
    }

    public JSONParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONParserException(Throwable cause) {
        super(cause);
    }

}