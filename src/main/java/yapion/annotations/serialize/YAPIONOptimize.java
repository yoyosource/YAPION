// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.annotations.serialize;

import yapion.serializing.YAPIONSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is for optimizing the YAPIONObject to remove Objects
 * that are {@code null}. This is for preventing clutter in the
 * serialization. Only use this if you can handle the edge case that, in
 * the deserialization, the field can be after deserialization {@code null}.
 *
 * <br><br>The context describes the state in which the {@link YAPIONSerializer}
 * should be in for this annotation to take effect.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface YAPIONOptimize {
    String context() default "";
}