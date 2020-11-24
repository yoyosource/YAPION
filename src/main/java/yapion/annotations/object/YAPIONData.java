// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.annotations.object;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation describes a combined {@link YAPIONLoad} and
 * {@link YAPIONSave} and stands for every Field specified in
 * this Type. This annotation will overwrite any existing
 * {@link YAPIONLoad}, {@link YAPIONSave} and {@link YAPIONField}.
 *
 * <br><br>The context describes the state in which the {@link YAPIONSerializer}
 * or {@link YAPIONDeserializer} should be for this annotation
 * to take effect.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface YAPIONData {
    String[] context() default {};

    /**
     * Cascade this annotation to instance properties / objects and their properties, too.
     */
    boolean cascading() default false;
}
