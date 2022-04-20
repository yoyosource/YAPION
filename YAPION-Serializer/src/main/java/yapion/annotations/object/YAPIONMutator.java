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

import yapion.serializing.views.Mutator;

import java.lang.annotation.*;

/**
 * This annotation is used to add a specific {@link Mutator} to a class.
 * When a specific {@link yapion.serializing.views.View} is used while deserializing and the view itself
 * is also a {@link Mutator} that has this annotation standing on the class file the mutation will take place.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(YAPIONMutator.YAPIONMutators.class)
public @interface YAPIONMutator {
    Class<? extends Mutator> value();

    /**
     * This is just a container to make the outer class {@link Repeatable}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface YAPIONMutators {
        YAPIONMutator[] value() default {};
    }
}
