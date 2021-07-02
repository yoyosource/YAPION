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

package yapion.serializing.annotationproccessing.generator;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Modifier;

@RequiredArgsConstructor
public enum ModifierType {
    PUBLIC(Modifier.PUBLIC),
    PRIVATE(Modifier.PRIVATE),
    PROTECTED(Modifier.PROTECTED),
    STATIC(Modifier.STATIC),
    FINAL(Modifier.FINAL),
    SYNCHRONIZED(Modifier.SYNCHRONIZED),
    VOLATILE(Modifier.VOLATILE),
    TRANSIENT(Modifier.TRANSIENT),
    NATIVE(Modifier.NATIVE),
    INTERFACE(Modifier.INTERFACE),
    ABSTRACT(Modifier.ABSTRACT),
    STRICT(Modifier.STRICT);

    final int modifier;
}
