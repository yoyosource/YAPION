// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;

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
        private final YAPIONAny yapionAny;

        private RecursionResult(RecursionType recursionType, YAPIONAny yapionAny) {
            this.recursionType = recursionType;
            this.yapionAny = yapionAny;
        }

        public RecursionType getRecursionType() {
            return recursionType;
        }

        public YAPIONAny getYAPIONAny() {
            return yapionAny;
        }
    }

    /**
     * Checks if a variable is already given in the hierarchy.
     *
     * @param toCheck is the variable to test for
     * @param instance is the instance of a hierarchy to check against
     * @return {@code RecursionResult} with the {@code RecursionType} and the {@code instance} against which was checked.
     */
    public static RecursionResult checkRecursion(YAPIONVariable toCheck, YAPIONAny instance) {
        return checkRecursion(toCheck.getValue(), instance);
    }

    /**
     * Checks if a instance is already given in the hierarchy.
     *
     * @param toCheck is the instance to test for
     * @param instance is the instance of a hierarchy to check against
     * @return {@code RecursionResult} with the {@code RecursionType} and the {@code instance} against which was checked.
     */
    public static RecursionResult checkRecursion(YAPIONAny toCheck, YAPIONAny instance) {
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