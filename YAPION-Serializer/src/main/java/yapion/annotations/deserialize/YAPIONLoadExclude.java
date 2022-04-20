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

package yapion.annotations.deserialize;

import yapion.serializing.YAPIONSerializer;
import yapion.serializing.views.View;

import java.lang.annotation.*;

/**
 * This annotation describes a Field or Type to be excluded in the process
 * of deserialization. Excluding means that the Field or Type annotated by
 * this annotation will not be written back into the java object. If this
 * annotation annotates a Type it is considered as a class description.
 *
 * <br><br>The context describes the state in which the {@link YAPIONSerializer}
 * should be in this annotation to take effect.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Repeatable(YAPIONLoadExclude.YAPIONLoadExcludes.class)
public @interface YAPIONLoadExclude {
    Class<? extends View>[] context() default {};

    /**
     * This is just a container to make the outer class {@link Repeatable}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    @interface YAPIONLoadExcludes {
        YAPIONLoadExclude[] value() default {};
    }
}
