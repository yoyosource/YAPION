// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.validators;

import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.packet.YAPIONPacket;

import java.util.List;

@FunctionalInterface
public interface ValidationHook {

    /**
     * Alter the validation so that the path of you {@link YAPIONAnyType}
     * can be deeper in the object. This is useful for validating
     * data in an {@link YAPIONPacket} as you don't want to generate
     * a complete validator for the whole Object, just for the data.
     *
     * @param path
     * @return the new path
     */
    List<String> path(List<String> path);

}