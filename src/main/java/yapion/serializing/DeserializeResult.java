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

package yapion.serializing;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.*;

@ToString
@EqualsAndHashCode
public final class DeserializeResult {

    private Map<Object, List<DeserializeField>> resultMap = new IdentityHashMap<>();

    /**
     * Retrieve all ignored fields for a given {@link Object}. This method will return {@code null} if none are present.
     *
     * @param object the object to look up
     * @return all ignored fields, {@code null} if none
     */
    public List<DeserializeField> ignoredFields(Object object) {
        return resultMap.get(object);
    }

    /**
     * Check if a given {@link Object} has ignored fields.
     *
     * @param object the object to look up
     * @return if the given {@link Object} has ignored fields.
     */
    public boolean hasIgnoredFields(Object object) {
        return resultMap.containsKey(object);
    }

    /**
     * Retrieve all {@link Object}'s that have ignored fields present.
     *
     * @return all {@link Object}'s with ignored fields
     */
    public Set<Object> objects() {
        return resultMap.keySet();
    }

    void add(Object object, String fieldName, YAPIONAnyType ignoredValue) {
        resultMap.computeIfAbsent(object, fields -> new ArrayList<>())
                .add(new DeserializeField(fieldName, ignoredValue));
    }

}
