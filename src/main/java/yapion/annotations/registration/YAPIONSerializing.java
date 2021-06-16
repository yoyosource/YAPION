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

package yapion.annotations.registration;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.serializing.GeneratedSerializerLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To load a generated Serializer for the annotated class you need to allow it in {@link GeneratedSerializerLoader} by
 * either allowing only the annotated class {@link GeneratedSerializerLoader#addClass(Class)} or the package and every
 * sub package {@link GeneratedSerializerLoader#addPackage(Package)}. This is a security measurement so that code
 * injections will only be done if directly stated in {@link GeneratedSerializerLoader}. If you specify a class
 * to allow loading of its generated Serializer you cannot undo this when it happened. It happens when you serialize or
 * deserialize any class annotated with this annotation and allowance by {@link GeneratedSerializerLoader}. You can
 * always allow more generated serializer to be loaded later on, by just adding them to {@link GeneratedSerializerLoader}.
 *
 * <br><br>Generator Rules
 * <br>- Act like a {@link YAPIONData} with no context
 * <br>- Allow removing specific fields with {@link YAPIONSaveExclude} and {@link YAPIONLoadExclude}
 * <br>- For evey field the {@link YAPIONOptimize} annotation will be considered
 * <br>- Any field marked static or transient will be ignored
 * <br>- Any field of super classes in the same compilation context will be considered
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface YAPIONSerializing {
}
