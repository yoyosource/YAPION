// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yapion.hierarchy.typegroups.YAPIONAnyType;

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