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

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

/**
 * A data structure to hold the flags for an {@code YAPIONSerializer}.
 */
public final class YAPIONFlags {

    static {
        SerializeManager.init();
    }

    private Map<YAPIONFlag, Boolean> flags = new HashMap<>();

    public YAPIONFlags() {
        YAPIONFlag.YAPION_FLAG_KEYS.forEach((s, yapionFlag) -> {
            flags.put(yapionFlag, yapionFlag.getFlagDefault());
        });
    }

    /**
     * Disallow any special serialization flags set in this {@link YAPIONFlags} instance.
     */
    public void strict() {
        for (YAPIONFlag yapionFlag : flags.keySet()) {
            flags.put(yapionFlag, false);
        }
    }

    /**
     * Default any special serialization flags set in this {@link YAPIONFlags} instance.
     */
    public void defaultFlags() {
        for (YAPIONFlag yapionFlag : flags.keySet()) {
            flags.put(yapionFlag, yapionFlag.getFlagDefault());
        }
    }

    /**
     * Default the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to defailt
     */
    public void setDefault(YAPIONFlag key) {
        set(key, key.getFlagDefault());
    }

    /**
     * Allow the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to allow
     */
    public void setTrue(YAPIONFlag key) {
        set(key, true);
    }

    /**
     * Disallow the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to disallow
     */
    public void setFalse(YAPIONFlag key) {
        set(key, false);
    }

    /**
     * Set the allowance/disallowance of the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to set
     * @param value the value {@code true} of allow, {@code false} otherwise
     */
    public void set(YAPIONFlag key, boolean value) {
        flags.put(key, value);
    }

    /**
     * Returns if a given special serialization denoted by <b>key</b> is set to {@code true}.
     *
     * @param key the special serialization to check
     * @return {@code true} if set to true, {@code key.getFlagDefault()} otherwise
     */
    public boolean isSet(YAPIONFlag key) {
        return flags.getOrDefault(key, key.getFlagDefault());
    }

    public void isSet(YAPIONFlag key, Runnable allowed) {
        isSet(key, allowed, () -> {});
    }

    public void isNotSet(YAPIONFlag key, Runnable disallowed) {
        isSet(key, () -> {}, disallowed);
    }

    public void isSet(YAPIONFlag key, Runnable allowed, Runnable disallowed) {
        if (isSet(key)) {
            allowed.run();
        } else {
            disallowed.run();
        }
    }

    @Builder
    public static class CheckOptions<T> {
        private T allowedValue;
        private T disallowedValue;
        private Runnable allowedRunnable;
        private Runnable disallowedRunnable;
    }

    public <T> T isSet(YAPIONFlag key, CheckOptions<T> checkOptions) {
        if (isSet(key)) {
            if (checkOptions.allowedRunnable != null) checkOptions.allowedRunnable.run();
            return checkOptions.allowedValue;
        } else {
            if (checkOptions.disallowedRunnable != null) checkOptions.disallowedRunnable.run();
            return checkOptions.disallowedValue;
        }
    }
}
