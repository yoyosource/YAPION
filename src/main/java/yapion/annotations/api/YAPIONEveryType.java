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

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This annotation describes that any YAPIONPrimitive is allowed as parameter.
 * A YAPIONPrimitive is anything you can store in a {@link YAPIONValue}. This
 * also includes any thing that is {@link YAPIONAnyType}.
 *
 * <br><br>YAPIONEveryType:
 * <br>- {@link String}
 * <br>- <b>char</b> or {@link Character}
 * <br>- <b>boolean</b> or {@link Boolean}
 * <br>- <b>byte</b> or {@link Byte}
 * <br>- <b>short</b> or {@link Short}
 * <br>- <b>int</b> or {@link Integer}
 * <br>- <b>long</b> or {@link Long}
 * <br>- {@link BigInteger}
 * <br>- <b>float</b> or {@link Float}
 * <br>- <b>double</b> or {@link Double}
 * <br>- {@link BigDecimal}
 * <br>- anything of type {@link YAPIONAnyType}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE_PARAMETER)
public @interface YAPIONEveryType {
}
