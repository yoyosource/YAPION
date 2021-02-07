// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

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

}