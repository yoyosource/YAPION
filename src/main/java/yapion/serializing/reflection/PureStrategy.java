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

import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.serializing.ReflectionStrategy;

import java.lang.reflect.Field;

public class PureStrategy implements ReflectionStrategy {

    @Override
    public void set(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new YAPIONDeserializerException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T get(Field field, Object object) {
        try {
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new YAPIONSerializerException(e.getMessage(), e);
        }
    }
}
