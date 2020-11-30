// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.output;

import yapion.exceptions.YAPIONException;

import java.nio.charset.StandardCharsets;

public abstract class AbstractOutput {

    public final AbstractOutput consume(String s) {
        validateMethodCall();
        internalConsume(s);
        return this;
    }

    public final AbstractOutput consumePrettified(String s) {
        validateMethodCall();
        if (prettified()) {
            internalConsume(s);
        }
        return this;
    }

    protected abstract void internalConsume(String s);

    protected boolean prettified() {
        return false;
    }

    protected final byte[] bytes(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private void validateMethodCall() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length < 4) {
            throw new YAPIONException("Invalid calling class");
        }
        if (stackTraceElements[3].getClassName().startsWith("yapion.hierarchy.types.YAPION")) {
            return;
        }
        throw new YAPIONException("Invalid calling class");
    }

}