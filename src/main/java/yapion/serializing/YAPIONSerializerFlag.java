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
import lombok.Setter;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * A data structure to hold the default value of a specific {@link YAPIONSerializerFlag}.
 */
@EqualsAndHashCode
public final class YAPIONSerializerFlag {

    static final Map<String, YAPIONSerializerFlag> YAPION_FLAG_KEYS = new HashMap<>();

    @Getter
    private final String keyName;

    @Setter
    private boolean flagDefault;

    private YAPIONSerializerFlag(String keyName) {
        YAPION_FLAG_KEYS.put(keyName, this);
        this.keyName = keyName;
    }

    public boolean getFlagDefault() {
        return flagDefault;
    }

    /**
     * The key to specify if data loss should be handled silently {@code false} or should throw an exception {@code true}.
     */
    public static final YAPIONSerializerFlag DATA_LOSS_EXCEPTION = new YAPIONSerializerFlag("base.dataLoss.exception");

    /**
     * The key to specify if a {@link PrivateKey} should be serialized {@code false} or should result in an thrown exception {@code true}.
     */
    public static final YAPIONSerializerFlag PRIVATE_KEY_EXCEPTION = new YAPIONSerializerFlag("base.privateKey.exception");

    /**
     * The key to specify if a {@link PrivateKey} should be serialized as {@code null} {@code true} or serialized properly {@code false}.
     */
    public static final YAPIONSerializerFlag PRIVATE_KEY_AS_NULL = new YAPIONSerializerFlag("base.privateKey.as.null");

    /**
     * The key to specify if a {@link Error} should be handled silently {@code false} or should throw an exception {@code true}.
     */
    public static final YAPIONSerializerFlag ERROR_EXCEPTION = new YAPIONSerializerFlag("base.error.exception");

    /**
     * The key to specify if a Reflection should be serialized {@code false} or should result in an thrown exception {@code true}.
     */
    public static final YAPIONSerializerFlag REFLECTION_EXCEPTION = new YAPIONSerializerFlag("base.reflection.exception");

    /**
     * The key to specify if a Reflection should be serialized as {@code null} {@code true} or serialized properly {@code false}.
     */
    public static final YAPIONSerializerFlag REFLECTION_AS_NULL = new YAPIONSerializerFlag("base.reflection.as.null");

    /**
     * Retrieve every flagKey specified in the current Runtime up until now.
     *
     * @return a String array with every {@link YAPIONSerializerFlag#keyName}
     */
    public static String[] flagKeys() {
        return YAPION_FLAG_KEYS.values().stream().map(yapionSerializerFlagKey -> yapionSerializerFlagKey.keyName).toArray(String[]::new);
    }

    /**
     * Static instance getter method which either creates a new {@link YAPIONSerializerFlag} or returns an already existing Instance.
     *
     * @param key the key to retrieve
     * @return the {@link YAPIONSerializerFlag}
     */
    public static YAPIONSerializerFlag flagKey(String key) {
        if (YAPION_FLAG_KEYS.containsKey(key)) {
            return YAPION_FLAG_KEYS.get(key);
        }
        return new YAPIONSerializerFlag(key);
    }

}
