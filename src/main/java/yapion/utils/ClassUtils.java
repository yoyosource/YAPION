/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.utils;

import lombok.experimental.UtilityClass;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.YAPIONException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@UtilityClass
public class ClassUtils {

    /**
     * Checks if a given field should be serialized or not.
     * Returns {@code true} if it should not be serialized.
     *
     * @param field Field to check
     * @return {@code true} if field should not be serialized; {@code false} otherwise.
     */
    @InternalAPI
    public static boolean removed(Field field) {
        return Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers());
    }

    @InternalAPI
    public static Class<?> getClass(String className) {
        switch (className) {
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "char":
                return char.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            default:
                break;
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new YAPIONException(e.getMessage(), e);
        }
    }

    @InternalAPI
    public static String getPrimitive(String className) {
        switch (className) {
            case "java.lang.Boolean":
                return "boolean";
            case "java.lang.Byte":
                return "byte";
            case "java.lang.Short":
                return "short";
            case "java.lang.Integer":
                return "int";
            case "java.lang.Long":
                return "long";
            case "java.lang.Character":
                return "char";
            case "java.lang.Float":
                return "float";
            case "java.lang.Double":
                return "double";
            default:
                return className;
        }
    }

    @InternalAPI
    public static Class<?> getPrimitive(Class<?> clazz) {
        if (clazz == Void.class) return void.class;
        if (clazz == Boolean.class) return boolean.class;
        if (clazz == Byte.class) return byte.class;
        if (clazz == Short.class) return short.class;
        if (clazz == Integer.class) return int.class;
        if (clazz == Long.class) return long.class;
        if (clazz == Character.class) return char.class;
        if (clazz == Float.class) return float.class;
        if (clazz == Double.class) return double.class;
        return clazz;
    }

    @InternalAPI
    public static String getBoxed(String className) {
        switch (className) {
            case "boolean":
                return "java.lang.Boolean";
            case "byte":
                return "java.lang.Byte";
            case "short":
                return "java.lang.Short";
            case "int":
                return "java.lang.Integer";
            case "long":
                return "java.lang.Long";
            case "char":
                return "java.lang.Character";
            case "float":
                return "java.lang.Float";
            case "double":
                return "java.lang.Double";
            default:
                return className;
        }
    }

    @InternalAPI
    public static Class<?> getBoxed(Class<?> clazz) {
        if (clazz == void.class) return Void.class;
        if (clazz == boolean.class) return Boolean.class;
        if (clazz == byte.class) return Byte.class;
        if (clazz == short.class) return Short.class;
        if (clazz == int.class) return Integer.class;
        if (clazz == long.class) return Long.class;
        if (clazz == char.class) return Character.class;
        if (clazz == float.class) return Float.class;
        if (clazz == double.class) return Double.class;
        return clazz;
    }

    @InternalAPI
    public static boolean isPrimitive(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }
        if (clazz == void.class) {
            return true;
        }
        return false;
    }

}
