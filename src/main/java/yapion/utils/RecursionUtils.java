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
import yapion.hierarchy.api.groups.YAPIONAnyType;

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
