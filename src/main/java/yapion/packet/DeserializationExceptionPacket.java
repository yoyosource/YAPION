package yapion.packet;

import lombok.Getter;
import yapion.hierarchy.types.YAPIONObject;

@Getter
public class DeserializationExceptionPacket extends YAPIONPacket {

    private YAPIONObject yapionObject;

    DeserializationExceptionPacket(YAPIONObject yapionObject) {
        this.yapionObject = yapionObject;
    }

}
