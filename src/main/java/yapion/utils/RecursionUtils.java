// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONVariable;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class RecursionUtils {

    private RecursionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public enum RecursionType {
        DIRECT,
        BACK_REFERENCE,
        NONE
    }

    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    public static class RecursionResult {
        private final RecursionType recursionType;
        private final YAPIONAnyType yapionAnyType;

        private RecursionResult(RecursionType recursionType, YAPIONAnyType yapionAnyType) {
            this.recursionType = recursionType;
            this.yapionAnyType = yapionAnyType;
        }

        public RecursionType getRecursionType() {
            return recursionType;
        }

        public YAPIONAnyType getYAPIONAny() {
            return yapionAnyType;
        }
    }

    /**
     * Checks if a variable is already given in the hierarchy.
     *
     * @param toCheck is the variable to test for
     * @param instance is the instance of a hierarchy to check against
     * @return {@link RecursionResult} with the {@link RecursionType} and the {@code instance} against which was checked.
     */
    public static RecursionResult checkRecursion(YAPIONVariable toCheck, YAPIONAnyType instance) {
        return checkRecursion(toCheck.getValue(), instance);
    }

    /**
     * Checks if a instance is already given in the hierarchy.
     *
     * @param toCheck is the instance to test for
     * @param instance is the instance of a hierarchy to check against
     * @return {@link RecursionResult} with the {@link RecursionType} and the {@code instance} against which was checked.
     */
    public static RecursionResult checkRecursion(YAPIONAnyType toCheck, YAPIONAnyType instance) {
        if (toCheck == instance) {
            return new RecursionResult(RecursionType.DIRECT, instance);
        }

        while ((instance = instance.getParent()) != null) {
            if (instance == toCheck) {
                return new RecursionResult(RecursionType.BACK_REFERENCE, instance);
            }
        }
        return new RecursionResult(RecursionType.NONE, null);
    }

}