package yapion.packet;

import lombok.Getter;
import yapion.hierarchy.types.YAPIONObject;
import yapion.packet.YAPIONPacket;

@Getter
public class YAPIONDeserializationExceptionPacket extends YAPIONPacket {

    private YAPIONObject yapionObject;

    YAPIONDeserializationExceptionPacket(YAPIONObject yapionObject) {
        this.yapionObject = yapionObject;
    }

}
