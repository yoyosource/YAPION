/*
 *  // SPDX-License-Identifier: Apache-2.0
 * // YAPION
 * // Copyright (C) 2019,2020 yoyosource
 */

package yapion.serializing.wrapped;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface WrappedImplementation {
    String since();
}
