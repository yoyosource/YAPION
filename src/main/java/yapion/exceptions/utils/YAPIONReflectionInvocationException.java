// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.exceptions.utils;

import yapion.exceptions.YAPIONException;

import java.lang.reflect.InvocationTargetException;

public class YAPIONReflectionInvocationException extends YAPIONException {

    private Throwable target;

    public YAPIONReflectionInvocationException(InvocationTargetException invocationTargetException) {
        super(invocationTargetException.getTargetException().getMessage(), invocationTargetException.getTargetException() );
        target = invocationTargetException.getTargetException();
    }

    public Throwable getTargetException() {
        return target;
    }

}