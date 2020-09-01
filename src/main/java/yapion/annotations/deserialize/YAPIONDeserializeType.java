// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.annotations.deserialize;

import yapion.serializing.YAPIONDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is useful for interface implementations like
 * {@link java.util.List}, {@link java.util.Map}, {@link java.util.Queue},
 * {@link java.util.Queue} or {@link java.util.Set}. You can
 * describe with what Type the {@link YAPIONDeserializer} should
 * fill a Field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface YAPIONDeserializeType {
    Class<?> type();
}