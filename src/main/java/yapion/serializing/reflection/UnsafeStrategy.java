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

package yapion.serializing.reflection;

import sun.misc.Unsafe;
import yapion.exceptions.YAPIONException;
import yapion.serializing.ReflectionStrategy;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class UnsafeStrategy implements ReflectionStrategy {

    private static Unsafe unsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new YAPIONException(e.getMessage(), e);
        }
    }

    private static class Operation {

        private long offset;
        private BiConsumer<Object, Object> setter;
        private Function<Object, Object> getter;

        public Operation(Field field) {
            offset = unsafe.objectFieldOffset(field);

            Class<?> clazz = field.getType();
            if (clazz == Byte.class || clazz == byte.class) {
                setter = (object, value) -> unsafe.putByte(object, offset, (byte) value);
                getter = o -> unsafe.getByte(o, offset);
            } else if (clazz == Short.class || clazz == short.class) {
                setter = (object, value) -> unsafe.putShort(object, offset, (short) value);
                getter = o -> unsafe.getShort(o, offset);
            } else if (clazz == Boolean.class || clazz == boolean.class) {
                setter = (object, value) -> unsafe.putBoolean(object, offset, (boolean) value);
                getter = o -> unsafe.getBoolean(o, offset);
            } else if (clazz == int.class || clazz == Integer.class) {
                setter = (object, value) -> unsafe.putInt(object, offset, (int) value);
                getter = o -> unsafe.getInt(o, offset);
            } else if (clazz == Character.class || clazz == char.class) {
                setter = (object, value) -> unsafe.putChar(object, offset, (char) value);
                getter = o -> unsafe.getChar(o, offset);
            } else if (clazz == Long.class || clazz == long.class) {
                setter = (object, value) -> unsafe.putLong(object, offset, (long) value);
                getter = o -> unsafe.getLong(o, offset);
            } else if (clazz == Float.class || clazz == float.class) {
                setter = (object, value) -> unsafe.putFloat(object, offset, (float) value);
                getter = o -> unsafe.getFloat(o, offset);
            } else if (clazz == Double.class || clazz == double.class) {
                setter = (object, value) -> unsafe.putDouble(object, offset, (double) value);
                getter = o -> unsafe.getDouble(o, offset);
            } else {
                setter = (object, value) -> unsafe.putObject(object, offset, value);
                getter = o -> unsafe.getObject(o, offset);
            }
        }

        public void set(Object object, Object value) {
            setter.accept(object, value);
        }

        public Object get(Object object) {
            return getter.apply(object);
        }

    }

    private Map<Field, Operation> operationMap = new LinkedHashMap<Field, Operation>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Field, Operation> eldest) {
            return size() > 1024;
        }
    };

    @Override
    public void set(Field field, Object object, Object value) {
        operationMap.computeIfAbsent(field, Operation::new).set(object, value);
    }

    @Override
    public <T> T get(Field field, Object object) {
        return (T) operationMap.computeIfAbsent(field, Operation::new).get(object);
    }
}
