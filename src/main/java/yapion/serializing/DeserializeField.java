// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import yapion.hierarchy.typegroups.YAPIONAnyType;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public final class DeserializeField {

    /**
     * The fieldName that could not be serialized.
     */
    public final String fieldName;

    /**
     * The value that should have been serialized into the fieldName.
     */
    @EqualsAndHashCode.Exclude
    public final YAPIONAnyType ignoredValue;

}