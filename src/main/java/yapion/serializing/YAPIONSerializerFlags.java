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

import java.util.HashMap;
import java.util.Map;

/**
 * A data structure to hold the flags for an {@code YAPIONSerializer}.
 */
public final class YAPIONSerializerFlags {

    static {
        SerializeManager.init();
    }

    private Map<YAPIONSerializerFlag, Boolean> flags = new HashMap<>();

    public YAPIONSerializerFlags() {
        YAPIONSerializerFlag.YAPION_FLAG_KEYS.values().forEach(yapionSerializerFlag -> {
            flags.put(yapionSerializerFlag, yapionSerializerFlag.getFlagDefault());
        });
    }

    /**
     * Disallow any special serialization flags set in this {@link YAPIONSerializerFlags} instance.
     */
    public void strict() {
        for (Map.Entry<YAPIONSerializerFlag, Boolean> entry : flags.entrySet()) {
            entry.setValue(false);
        }
    }

    /**
     * Default any special serialization flags set in this {@link YAPIONSerializerFlags} instance.
     */
    public void defaultFlags() {
        YAPIONSerializerFlag.YAPION_FLAG_KEYS.values().forEach(yapionSerializerFlag -> {
            flags.put(yapionSerializerFlag, yapionSerializerFlag.getFlagDefault());
        });
    }

    /**
     * Allow the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to allow
     */
    public void setTrue(YAPIONSerializerFlag key) {
        set(key, true);
    }

    /**
     * Disallow the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to disallow
     */
    public void setFalse(YAPIONSerializerFlag key) {
        set(key, false);
    }

    /**
     * Set the allowance/disallowance of the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to set
     * @param value the value {@code true} of allow, {@code false} otherwise
     */
    public void set(YAPIONSerializerFlag key, boolean value) {
        flags.put(key, value);
    }

    /**
     * Returns if a given special serialization denoted by <b>key</b> is set to {@code true}.
     *
     * @param key the special serialization to check
     * @return {@code true} if set to true, {@code false} otherwise
     */
    public boolean isSet(YAPIONSerializerFlag key) {
        return flags.getOrDefault(key, false);
    }

}
