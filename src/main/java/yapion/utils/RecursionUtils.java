// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;

public class RecursionUtils {

    public enum RecursionType {
        DIRECT,
        BACK_REFERENCE,
        NONE
    }

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

    public static RecursionResult checkRecursion(YAPIONVariable toCheck, YAPIONAny instance) {
        return checkRecursion(toCheck.getValue(), instance);
    }

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