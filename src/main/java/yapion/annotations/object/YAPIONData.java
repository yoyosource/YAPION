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

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.lang.annotation.*;

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
@Repeatable(YAPIONData.YAPIONDatas.class)
public @interface YAPIONData {
    String[] context() default {};

    /**
     * Cascade this annotation to instance properties / objects and their properties, too.
     */
    boolean cascading() default false;

    /**
     * This is just a container to make the outer class {@link Repeatable}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface YAPIONDatas {
        YAPIONData[] value() default {};
    }
}
