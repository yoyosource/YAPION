package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

import java.util.HashMap;
import java.util.Map;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONPacketIdentifier {

    private final Map<String, Object> payload = new HashMap<>();

    /**
     * Adds another key value pair to this YAPIONPacketIdentifier.
     *
     * @param key the key
     * @param value the value
     */
    public YAPIONPacketIdentifier add(String key, Object value) {
        payload.put(key, value);
        return this;
    }

    /**
     * Removes a key value pair from this YAPIONPacketIdentifier.
     *
     * @param key the key
     */
    public YAPIONPacketIdentifier remove(String key) {
        payload.remove(key);
        return this;
    }

    /**
     * Gets a value of a specified key from this YAPIONPacketIdentifier.
     *
     * @param key the key to retrieve
     * @return the Object associated by the key
     */
    public Object get(String key) {
        return payload.get(key);
    }

}
