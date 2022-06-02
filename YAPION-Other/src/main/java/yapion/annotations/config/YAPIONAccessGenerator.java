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

package yapion.annotations.config;

import yapion.config.annotationproccessing.ConstraintUtils;
import yapion.parser.CommentParsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface YAPIONAccessGenerator {

    /**
     * Inline configuration instead of file reference.
     *
     * @return {@code true} for inline, {@code false} for file reference from repository root.
     */
    boolean inline() default false;

    /**
     * Generate setter as well.
     *
     * @return {@code true} for setter generation, {@code false} otherwise.
     */
    boolean setter() default false;

    /**
     * Apply the {@link lombok.ToString} annotation to every class.
     *
     * @return {@code true} for toString generation, {@code false} otherwise.
     */
    boolean generateToString() default false;

    /**
     * Apply the {@link lombok.experimental.ExtensionMethod} annotation to the root class with the {@link ConstraintUtils} class parameter.
     *
     * @return {@code true} for extensionMethod usage, {@code false} otherwise.
     */
    boolean lombokExtensionMethods() default false;

    /**
     * How to process comments.
     *
     * @return {@link CommentParsing}
     */
    CommentParsing commentParsing() default CommentParsing.SKIP;

    /**
     * If lazy parsing should be enabled
     *
     * @return {@code true} for lazy parsing, {@code false} otherwise
     */
    boolean lazy() default true;
}
