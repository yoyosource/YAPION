// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.YAPIONLoad;
import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSave;
import yapion.annotations.YAPIONSaveExclude;
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

    private final String type;
    private final Map<String, Object> payload = new HashMap<>();

    public YAPIONPacket(String type) {
        this.type = type;
    }

    public YAPIONPacket add(String key, Object value) {
        lastModified = System.currentTimeMillis();
        payload.put(key, value);
        return this;
    }

    public Object get(String key) {
        return payload.get(key);
    }

    public String getType() {
        return type;
    }

    public long length() {
        return getYAPION().toString().length();
    }

    public YAPIONObject getYAPION() {
        if (cache != null && lastModified == lastCreated) {
            return cache;
        }
        lastCreated = lastModified;
        cache = YAPIONSerializer.serialize(this);
        return cache;
    }

    public YAPIONObject toObject() {
        return getYAPION();
    }

    public YAPIONObject getObject() {
        return getYAPION();
    }

    public String toSendString() {
        return getYAPION().toString();
    }

    @Override
    public String toString() {
        return "YAPIONPacket{" +
                "lastModified=" + lastModified +
                ", lastCreated=" + lastCreated +
                ", cache=" + cache +
                ", type='" + type + '\'' +
                ", payload=" + payload +
                '}';
    }

}