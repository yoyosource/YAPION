// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.validators;

import yapion.hierarchy.types.YAPIONValue;

@FunctionalInterface
public interface Validation {

    /**
     * This method should return true if the inputted
     * {@link YAPIONValue} value is considered to be valid.
     *
     * @param o the value of the {@link YAPIONValue}
     * @return {@code true} if valid, {@code false} otherwise
     */
    boolean validate(Object o);

}