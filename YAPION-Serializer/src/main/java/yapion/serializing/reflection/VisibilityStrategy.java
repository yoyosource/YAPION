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
public final class VisibilityStrategy implements ReflectionStrategy {

    private final Predicate<Field> getVisibility;
    private final Predicate<Field> setVisibility;
    private ReflectionStrategy reflectionStrategy = new PureStrategy();

    public VisibilityStrategy(Predicate<Field> fieldPredicate) {
        getVisibility = fieldPredicate;
        setVisibility = fieldPredicate;
    }

    public VisibilityStrategy(Predicate<Field> fieldPredicate, ReflectionStrategy reflectionStrategy) {
        getVisibility = fieldPredicate;
        setVisibility = fieldPredicate;
        this.reflectionStrategy = reflectionStrategy;
    }

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

    @AllArgsConstructor
    public enum FieldVisibilityModifier {
        FINAL(field -> Modifier.isFinal(field.getModifiers())),
        VOLATILE(field -> Modifier.isVolatile(field.getModifiers())),
        PUBLIC(field -> Modifier.isPublic(field.getModifiers())),
        PRIVATE(field -> Modifier.isPrivate(field.getModifiers())),
        PROTECTED(field -> Modifier.isProtected(field.getModifiers())),
        PACKAGE_PRIVATE(field -> {
            int modifier = field.getModifiers();
            return !Modifier.isPrivate(modifier) && !Modifier.isProtected(modifier) && !Modifier.isPublic(modifier);
        });

        private Predicate<Field> predicate;
    }

    public static Predicate<Field> checkField(boolean allowed, FieldVisibilityModifier fieldVisibilityModifier) {
        if (allowed) {
            return fieldVisibilityModifier.predicate;
        } else {
            return fieldVisibilityModifier.predicate.negate();
        }
    }

    public static Predicate<Field> checkClass(boolean allowed, Class<?> type) {
        return field -> {
            if (allowed) {
                return field.getDeclaringClass().equals(type);
            } else {
                return !field.getDeclaringClass().equals(type);
            }
        };
    }

    public static Predicate<Field> checkClasses(boolean allowed, Class<?>... types) {
        return field -> {
            for (Class<?> type : types) {
                if (field.getDeclaringClass().equals(type)) {
                    return allowed;
                }
            }
            return !allowed;
        };
    }

    public static Predicate<Field> allowFieldType(boolean allowed, Class<?> type) {
        return field -> {
            if (allowed) {
                return field.getType().getTypeName().equals(type.getTypeName());
            } else {
                return !field.getType().getTypeName().equals(type.getTypeName());
            }
        };
    }

    public static Predicate<Field> allowFieldTypes(boolean allowed, Class<?>... types) {
        return field -> {
            for (Class<?> type : types) {
                if (field.getType().getTypeName().equals(type.getTypeName())) {
                    return allowed;
                }
            }
            return !allowed;
        };
    }
}
