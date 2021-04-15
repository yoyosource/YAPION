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

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * A data structure to hold the default value of a specific {@link YAPIONSerializerFlagKey}.
 */
@EqualsAndHashCode
public final class YAPIONSerializerFlagDefault {

    private static final Map<String, YAPIONSerializerFlagKey> YAPION_FLAG_KEYS = new HashMap<>();

    /**
     * The key to specify if data loss should be handled silently {@code false} or should throw an exception {@code true}.
     */
    public static final YAPIONSerializerFlagKey DATA_LOSS_EXCEPTION = new YAPIONSerializerFlagKey("dataLoss.exception");

    /**
     * The key to specify if a {@link PrivateKey} should be serialized {@code false} or should result in an thrown exception {@code true}.
     */
    public static final YAPIONSerializerFlagKey PRIVATE_KEY_EXCEPTION = new YAPIONSerializerFlagKey("privateKey.exception");

    /**
     * The key to specify if a {@link PrivateKey} should be serialized as {@code null} {@code true} or serialized properly {@code false}.
     */
    public static final YAPIONSerializerFlagKey PRIVATE_KEY_AS_NULL = new YAPIONSerializerFlagKey("privateKey.as.null");

    /**
     * The key to specify if a {@link Error} should be handled silently {@code false} or should throw an exception {@code true}.
     */
    public static final YAPIONSerializerFlagKey ERROR_EXCEPTION = new YAPIONSerializerFlagKey("error.exception");

    /**
     * The key to specify if a Reflection should be serialized {@code false} or should result in an thrown exception {@code true}.
     */
    public static final YAPIONSerializerFlagKey REFLECTION_EXCEPTION = new YAPIONSerializerFlagKey("reflection.exception");

    /**
     * The key to specify if a Reflection should be serialized as {@code null} {@code true} or serialized properly {@code false}.
     */
    public static final YAPIONSerializerFlagKey REFLECTION_AS_NULL = new YAPIONSerializerFlagKey("reflection.as.null");

    /**
     * Retrieve every flagKey specified in the current Runtime up until now.
     *
     * @return a String array with every {@link YAPIONSerializerFlagKey#keyName}
     */
    public static String[] flagKeys() {
        return YAPION_FLAG_KEYS.values().stream().map(yapionSerializerFlagKey -> yapionSerializerFlagKey.keyName).toArray(String[]::new);
    }

    /**
     * Static instance getter method which either creates a new {@link YAPIONSerializerFlagKey} or returns an already existing Instance.
     *
     * @param key the key to retrieve
     * @return the {@link YAPIONSerializerFlagKey}
     */
    public static YAPIONSerializerFlagKey flagKey(String key) {
        return YAPION_FLAG_KEYS.computeIfAbsent(key, YAPIONSerializerFlagKey::new);
    }

    /**
     * A data structure to hold the FlagKey used by {@link YAPIONSerializerFlags}.
     */
    @EqualsAndHashCode
    public static final class YAPIONSerializerFlagKey {

        @Getter
        private final String keyName;

        private YAPIONSerializerFlagKey(String keyName) {
            YAPIONSerializerFlagDefault.YAPION_FLAG_KEYS.put(keyName, this);
            this.keyName = keyName;
        }

    }

    final YAPIONSerializerFlagKey flagKey;

    @EqualsAndHashCode.Exclude
    final boolean flagDefault;

    public YAPIONSerializerFlagDefault(YAPIONSerializerFlagKey flagKey, boolean flagDefault) {
        this.flagKey = flagKey;
        this.flagDefault = flagDefault;
    }

}
