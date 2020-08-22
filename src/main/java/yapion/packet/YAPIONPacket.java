// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSave;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.utils.YAPIONPacketException;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import java.util.HashMap;
import java.util.Map;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONPacket {

    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private long lastModified = 0;
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private long lastCreated = 0;
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private YAPIONObject cache = null;
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private String cacheString = null;

    private final String type;
    private final Map<String, Object> payload = new HashMap<>();

    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private YAPIONPacketIdentifier yapionPacketIdentifier = null;

    /**
     * Creates an YAPIONPacket with an specific type. If the type
     * is {@code null} this constructor will throw an
     * YAPIONPacketException. The type name "@error" and "@exception"
     * are not allowed and will throw an YAPIONPacketException.
     *
     * @param type the specified type of packet
     */
    public YAPIONPacket(String type) {
        if (type == null) {
            throw new YAPIONPacketException();
        }
        if (type.equals("@error") || type.equals("@exception")) {
            throw new YAPIONPacketException();
        }
        this.type = type;
    }

    /**
     * Adds another key value pair to this YAPIONPacket.
     *
     * @param key the key
     * @param value the value
     */
    public YAPIONPacket add(String key, Object value) {
        lastModified = System.currentTimeMillis();
        payload.put(key, value);
        return this;
    }

    /**
     * Removes a key value pair from this YAPIONPacket.
     *
     * @param key the key
     */
    public YAPIONPacket remove(String key) {
        lastModified = System.currentTimeMillis();
        payload.remove(key);
        return this;
    }

    /**
     * Gets a value of a specified key from this YAPIONPacket.
     *
     * @param key the key to retrieve
     * @return the Object associated by the key
     */
    public Object get(String key) {
        return payload.get(key);
    }

    /**
     * Gets the YAPIONPacketIdentifier from this YAPIONPacket.
     * This value can return null when the value is not
     * specified.
     *
     * @return the specified YAPIONPacketIdentifier
     */
    public YAPIONPacketIdentifier getYapionPacketIdentifier() {
        return yapionPacketIdentifier;
    }

    YAPIONPacket setYapionPacketIdentifier(YAPIONPacketIdentifier yapionPacketIdentifier) {
        this.yapionPacketIdentifier = yapionPacketIdentifier;
        return this;
    }

    /**
     * Gets the type of this YAPIONPacket.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the YAPION packet length that will be send.
     *
     * @return the length.
     */
    public long length() {
        return toSendString().length();
    }

    /**
     * Creates the YAPIONObject from this YAPIONPacket and caches it
     * for further use. Using {@code add} discards this cache. This
     * method uses the {@code YAPIONSerializer} to serialize itself
     * to the used YAPIONObject.
     *
     * @return the YAPIONObject
     */
    public YAPIONObject getYAPION() {
        if (cache != null && lastModified == lastCreated) {
            return cache;
        }
        lastCreated = lastModified;
        cache = YAPIONSerializer.serialize(this);
        return cache;
    }

    /**
     * Creates a String from the YAPIONObject created by {@code getYAPION}.
     * Creates the YAPIONObject along side creating the string. The string
     * gets cached for further use. Using {@code add} discards this cache.
     *
     * @return the String from the YAPIONObject
     */
    public String toSendString() {
        if (cacheString != null && lastModified == lastCreated) {
            return cacheString;
        }
        cacheString = getYAPION().toString();
        return cacheString;
    }

    @Override
    public String toString() {
        return "YAPIONPacket{" +
                "type='" + type + '\'' +
                ", payload=" + payload +
                '}';
    }

}