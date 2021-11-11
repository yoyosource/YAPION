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

import org.objenesis.ObjenesisBase;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.views.View;

import java.lang.annotation.*;

/**
 * This annotation describes one of the four steps in the process
 * of deserialization. You can use this annotation for a method
 * that defaults the data or object before deserialized by YAPION.
 * This can be useful to evaluate {@code null} pointer to their
 * default value or to set default values. This annotation is useful
 * if you use {@link YAPIONData} or {@link YAPIONObjenesis} as annotations.
 *
 * <br><br>The four Steps are:
 * <br>- Constructing the object (either by using the Constructor or {@link ObjenesisBase})
 * <br>- PreDeserialization method call
 * <br>- Deserializing all fields ({@link YAPIONDeserializer})
 * <br>- PostDeserialization method call
 *
 * <br><br>The context describes the state in which the {@link YAPIONDeserializer}
 * should be for this annotation to take effect.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Repeatable(YAPIONPreDeserialization.YAPIONPreDeserializations.class)
public @interface YAPIONPreDeserialization {
    Class<? extends View>[] context() default {};

    /**
     * This is just a container to make the outer class {@link Repeatable}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @interface YAPIONPreDeserializations {
        YAPIONPreDeserialization[] value() default {};
    }
}
