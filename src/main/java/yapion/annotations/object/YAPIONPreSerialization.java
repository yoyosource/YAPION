// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.annotations.object;

import yapion.serializing.YAPIONSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation describes one of the three steps in the process
 * of serialization. You can use this annotation for a method
 * that remove the data or object before serialized by YAPION. This
 * can be useful to remove and values from the serialized object that
 * should be null. You can also use this to remove any data that is not
 * serializable.
 *
 * <br><br>Thr four Steps are:
 * <br>- PreSerialization method call
 * <br>- Serializing all fields ({@link YAPIONSerializer})
 * <br>- PostSerialization method call
 *
 * <br><br>The context describes the state in which the {@link YAPIONSerializer}
 * should be for this annotation to take effect.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface YAPIONPreSerialization {
    String[] context() default {};
}