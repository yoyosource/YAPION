// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.annotations.object;

import org.objenesis.ObjenesisBase;
import yapion.serializing.YAPIONDeserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to describe that the
 * {@link YAPIONDeserializer} should use {@link ObjenesisBase} to
 * create an instance of this Type. This annotation should be used
 * with care and is best used with {@link YAPIONPreDeserialization}
 * and {@link YAPIONPostDeserialization}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface YAPIONObjenesis {
}