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

package yapion.utils;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class ModifierUtils {

    private ModifierUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks if a given field should be serialized or not.
     * Returns {@code true} if it should not be serialized.
     *
     * @param field Field to check
     * @return {@code true} if field should not be serialized; {@code false} otherwise.
     */
    public static boolean removed(Field field) {
        return Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers());
    }

}
