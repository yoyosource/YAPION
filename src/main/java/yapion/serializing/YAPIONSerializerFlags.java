// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing;

import yapion.serializing.YAPIONSerializerFlagDefault.YAPIONSerializerFlagKey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A data structure to hold the flags for an {@code YAPIONSerializer}.
 */
public final class YAPIONSerializerFlags {

    private static final Set<YAPIONSerializerFlagDefault> YAPION_SERIALIZER_FLAG_DEFAULTS = new HashSet<>();
    public static void addFlag(YAPIONSerializerFlagDefault yapionSerializerFlagDefault) {
        YAPION_SERIALIZER_FLAG_DEFAULTS.add(yapionSerializerFlagDefault);
    }
    static {
        SerializeManager.init();
    }

    private Map<YAPIONSerializerFlagKey, Boolean> flags = new HashMap<>();

    public YAPIONSerializerFlags() {
        YAPION_SERIALIZER_FLAG_DEFAULTS.forEach(yapionSerializerFlagDefault -> {
            flags.put(yapionSerializerFlagDefault.flagKey, yapionSerializerFlagDefault.flagDefault);
        });
    }

    /**
     * Disallow any special serialization flags set in this {@link YAPIONSerializerFlags} instance.
     */
    public void strict() {
        for (Map.Entry<YAPIONSerializerFlagKey, Boolean> entry : flags.entrySet()) {
            entry.setValue(false);
        }
    }

    /**
     * Default any special serialization flags set in this {@link YAPIONSerializerFlags} instance.
     */
    public void defaultFlags() {
        YAPION_SERIALIZER_FLAG_DEFAULTS.forEach(yapionSerializerFlagDefault -> {
            flags.put(yapionSerializerFlagDefault.flagKey, yapionSerializerFlagDefault.flagDefault);
        });
    }

    /**
     * Allow the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to allow
     */
    public void setTrue(YAPIONSerializerFlagKey key) {
        set(key, true);
    }

    /**
     * Disallow the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to disallow
     */
    public void setFalse(YAPIONSerializerFlagKey key) {
        set(key, false);
    }

    /**
     * Set the allowance/disallowance of the special serialization denoted by <b>key</b>.
     *
     * @param key the special serialization to set
     * @param value the value {@code true} of allow, {@code false} otherwise
     */
    public void set(YAPIONSerializerFlagKey key, boolean value) {
        flags.put(key, value);
    }

    /**
     * Returns if a given special serialization denoted by <b>key</b> is set to {@code true}.
     *
     * @param key the special serialization to check
     * @return {@code true} if set to true, {@code false} otherwise
     */
    public boolean isSet(YAPIONSerializerFlagKey key) {
        return flags.getOrDefault(key, false);
    }

}