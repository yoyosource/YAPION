package yapion.packet;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;

@YAPIONLoadExclude(context = "*")
@YAPIONSaveExclude(context = "*")
public interface YAPIONPacketHandler {

    void handlePacket(YAPIONPacket yapionPacket);

}
