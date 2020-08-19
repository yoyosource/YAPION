// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class ModifierUtils {

    private ModifierUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean removed(Field field) {
        return Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers());
    }

}