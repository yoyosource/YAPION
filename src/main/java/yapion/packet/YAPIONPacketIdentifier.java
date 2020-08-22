package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONPacketIdentifier<T> {

    private T identifier;

    /**
     * Creates an YAPIONPacketIdentifier with the specified
     * value.
     *
     * @param identifier the Value
     */
    public YAPIONPacketIdentifier(T identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the specified value.
     *
     * @return the Value
     */
    public T get() {
        return identifier;
    }

}
