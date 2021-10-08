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

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import yapion.serializing.ReflectionStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

@AllArgsConstructor
@RequiredArgsConstructor
public class VisibilityStrategy implements ReflectionStrategy {

    private final Predicate<Field> getVisibility;
    private final Predicate<Field> setVisibility;
    private ReflectionStrategy reflectionStrategy = new PureStrategy();

    @Override
    public void set(Field field, Object object, Object value) {
        if (setVisibility.test(field)) {
            reflectionStrategy.set(field, object, value);
        }
    }

    @Override
    public boolean checkGet(Field field, Object object) {
        return getVisibility.test(field) && reflectionStrategy.checkGet(field, object);
    }

    @Override
    public <T> T get(Field field, Object object) {
        return reflectionStrategy.get(field, object);
    }

    public static Predicate<Field> finalFields() {
        return field -> Modifier.isFinal(field.getModifiers());
    }

    public static Predicate<Field> publicFields() {
        return field -> Modifier.isPublic(field.getModifiers());
    }

    public static Predicate<Field> privateFields() {
        return field -> Modifier.isPrivate(field.getModifiers());
    }

    public static Predicate<Field> protectedFields() {
        return field -> Modifier.isProtected(field.getModifiers());
    }

    public static Predicate<Field> packagePrivateFields() {
        return field -> !Modifier.isPrivate(field.getModifiers()) && !Modifier.isProtected(field.getModifiers()) && !Modifier.isPublic(field.getModifiers());
    }

    public static Predicate<Field> checkClass(Class<?> type) {
        return field -> field.getDeclaringClass().equals(type);
    }

    public static Predicate<Field> checkClasses(Class<?>... types) {
        return field -> {
            for (Class<?> type : types) {
                if (field.getDeclaringClass().equals(type)) {
                    return true;
                }
            }
            return false;
        };
    }

    public static Predicate<Field> fieldType(Class<?> type) {
        return field -> field.getType().getTypeName().equals(type.getTypeName());
    }

    public static Predicate<Field> fieldTypes(Class<?>... types) {
        return field -> {
            for (Class<?> type : types) {
                return field.getType().getTypeName().equals(type.getTypeName());
            }
            return false;
        };
    }
}
