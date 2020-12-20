package yapion.packet;

import yapion.hierarchy.types.YAPIONObject;

public class YAPIONHandleFailedPacket extends YAPIONPacket {

    public YAPIONObject yapionObject;

    public YAPIONHandleFailedPacket(YAPIONObject yapionObject) {
        this.yapionObject = yapionObject;
    }

}
