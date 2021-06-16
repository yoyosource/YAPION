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

package yapion.annotations.serialize;

import yapion.serializing.YAPIONSerializer;

import java.lang.annotation.*;

/**
 * This annotation is for optimizing the YAPIONObject to remove Objects
 * that are {@code null}. This is for preventing clutter in the
 * serialization. Only use this if you can handle the edge case that, in
 * the deserialization, the field can be after deserialization {@code null}.
 *
 * <br><br>The context describes the state in which the {@link YAPIONSerializer}
 * should be for this annotation to take effect.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Repeatable(YAPIONOptimize.YAPIONOptimizes.class)
public @interface YAPIONOptimize {
    String[] context() default {};

    /**
     * This is just a container to make the outer class {@link Repeatable}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface YAPIONOptimizes {
        YAPIONOptimize[] value() default {};
    }
}
