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

package yapion.annotations.object;

import yapion.serializing.YAPIONSerializer;
import yapion.serializing.views.View;

import java.lang.annotation.*;

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
@Repeatable(YAPIONPreSerialization.YAPIONPreSerializations.class)
public @interface YAPIONPreSerialization {
    Class<? extends View>[] context() default {};

    /**
     * This is just a container to make the outer class {@link Repeatable}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @interface YAPIONPreSerializations {
        YAPIONPreSerialization[] value() default {};
    }
}
