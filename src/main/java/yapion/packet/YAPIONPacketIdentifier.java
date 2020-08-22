package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONPacketIdentifier<T> {

    private T identifier;

    public YAPIONPacketIdentifier(T identifier) {
        this.identifier = identifier;
    }

    public T get() {
        return identifier;
    }

}
