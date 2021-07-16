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
 * A data structure to hold the default value of a specific {@link YAPIONFlag}.
 */
@EqualsAndHashCode
public final class YAPIONFlag {

    static final Map<String, YAPIONFlag> YAPION_FLAG_KEYS = new HashMap<>();

    @Getter
    private final String keyName;

    private boolean flagDefault;

    private YAPIONFlag(String keyName) {
        YAPION_FLAG_KEYS.put(keyName, this);
        this.keyName = keyName;
    }

    public boolean getFlagDefault() {
        return flagDefault;
    }

    public YAPIONFlag setFlagDefault(boolean flagDefault) {
        this.flagDefault = flagDefault;
        return this;
    }

    /**
     * The key to specify if data loss should be handled silently {@code false} or should throw an exception {@code true}.
     */
    public static final YAPIONFlag DATA_LOSS_EXCEPTION = new YAPIONFlag("base.dataLoss.exception").setFlagDefault(false);

    /**
     * The key to specify if a {@link PrivateKey} should be serialized {@code false} or should result in an thrown exception {@code true}.
     */
    public static final YAPIONFlag PRIVATE_KEY_EXCEPTION = new YAPIONFlag("base.privateKey.exception").setFlagDefault(true);

    /**
     * The key to specify if a {@link PrivateKey} should be serialized as {@code null} {@code true} or serialized properly {@code false}.
     */
    public static final YAPIONFlag PRIVATE_KEY_AS_NULL = new YAPIONFlag("base.privateKey.as.null").setFlagDefault(false);

    /**
     * The key to specify if a {@link Error} should be handled silently {@code false} or should throw an exception {@code true}.
     */
    public static final YAPIONFlag ERROR_EXCEPTION = new YAPIONFlag("base.error.exception").setFlagDefault(false);

    /**
     * The key to specify if a Reflection should be serialized {@code false} or should result in an thrown exception {@code true}.
     */
    public static final YAPIONFlag REFLECTION_EXCEPTION = new YAPIONFlag("base.reflection.exception").setFlagDefault(true);

    /**
     * The key to specify if a Reflection should be serialized as {@code null} {@code true} or serialized properly {@code false}.
     */
    public static final YAPIONFlag REFLECTION_AS_NULL = new YAPIONFlag("base.reflection.as.null").setFlagDefault(false);

    /**
     * The key to specify a Class should serialize its byteCode {@code true} or not {@code false}.
     */
    public static final YAPIONFlag CLASS_INJECTION = new YAPIONFlag("base.reflection.class.injection").setFlagDefault(false);

    /**
     * Retrieve every flagKey specified in the current Runtime up until now.
     *
     * @return a String array with every {@link YAPIONFlag#keyName}
     */
    public static String[] flagKeys() {
        return YAPION_FLAG_KEYS.values().stream().map(yapionSerializerFlagKey -> yapionSerializerFlagKey.keyName).toArray(String[]::new);
    }

    /**
     * Static instance getter method which either creates a new {@link YAPIONFlag} or returns an already existing Instance.
     *
     * @param key the key to retrieve
     * @return the {@link YAPIONFlag}
     */
    public static YAPIONFlag flagKey(String key) {
        if (YAPION_FLAG_KEYS.containsKey(key)) {
            return YAPION_FLAG_KEYS.get(key);
        }
        return new YAPIONFlag(key);
    }

}
