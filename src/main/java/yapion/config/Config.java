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

package yapion.config;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import yapion.annotations.config.YAPIONConfig;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.ReflectionStrategy;
import yapion.serializing.reflection.PureStrategy;
import yapion.utils.ClassUtils;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

@UtilityClass
public class Config {

    public static <T> T apply(@NonNull T object, YAPIONObject config) {
        return apply(object, config, new PureStrategy());
    }

    public static <T> T apply(@NonNull T object, YAPIONObject config, ReflectionStrategy reflectionStrategy) {
        return apply(object, config, (key, field) -> key, reflectionStrategy);
    }

    public static <T> T apply(@NonNull T object, YAPIONObject config, BiFunction<String[], Field, String[]> mutator) {
        return apply(object, config, mutator, new PureStrategy());
    }

    public static <T> T apply(@NonNull T object, YAPIONObject config, BiFunction<String[], Field, String[]> mutator, ReflectionStrategy reflectionStrategy) {
        ReflectionsUtils.getFields(object.getClass()).forEach(field -> {
            YAPIONConfig yapionConfig = field.getAnnotation(YAPIONConfig.class);
            if (yapionConfig == null) {
                return;
            }
            String[] key = mutator.apply(yapionConfig.value(), field);
            Class<?> fieldType = field.getType();
            if (ClassUtils.isPrimitive(fieldType) || ClassUtils.isBoxedPrimitive(fieldType)) {
                config.get(key).ifPresent(value -> {
                    YAPIONAnyType yapionAnyType = value.value;
                    if (yapionAnyType instanceof YAPIONValue yapionValue) {
                        reflectionStrategy.set(field, object, yapionValue.get());
                    }
                });
            }
        });
        return object;
    }
}
