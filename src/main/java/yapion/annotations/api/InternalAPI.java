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

package yapion.annotations.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anything annotated with this Annotation is considered internal
 * API and should only be used if you know what you do.
 *
 * <br><br>Type:<br>
 * If annotating a type (Class) the class itself is internal you
 * should not implement or extend it. It does not say anything
 * about the methods defined in this Class.
 *
 * <br><br>Methods:<br>
 * If annotating a method the method is considered internal
 * API. Checks and security measurements are omitted in these
 * and the Method signature or implementation can change
 * each and every version.
 *
 * <br><br>Fields:<br>
 * If annotating a field the field should not be used from anywhere.
 * It is public because it speeds up something or makes something
 * easier to use.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface InternalAPI {
}
