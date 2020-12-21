package yapion.packet;

import yapion.hierarchy.types.YAPIONObject;

public class HandleFailedPacket extends YAPIONPacket {

    public YAPIONObject yapionObject;

    public HandleFailedPacket(YAPIONObject yapionObject) {
        this.yapionObject = yapionObject;
    }

}
