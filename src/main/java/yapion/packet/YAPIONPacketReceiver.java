package yapion.packet;

import yapion.exceptions.packet.YAPIONPacketException;

import java.util.HashMap;
import java.util.Map;

public class YAPIONPacketReceiver {

    private final Map<String, YAPIONPacketHandler> handlerMap = new HashMap<>();

    public YAPIONPacketReceiver add(String packetType, YAPIONPacketHandler yapionPacketHandler) {
        if (yapionPacketHandler == null || packetType == null) {
            throw new YAPIONPacketException();
        }
        handlerMap.put(packetType, yapionPacketHandler);
        return this;
    }

    public YAPIONPacketReceiver add(String[] packetTypes, YAPIONPacketHandler yapionPacketHandler) {
        for (String s : packetTypes) {
            add(s, yapionPacketHandler);
        }
        return this;
    }

    public YAPIONPacketReceiver add(YAPIONPacketHandler yapionPacketHandler, String packetType) {
        return add(packetType, yapionPacketHandler);
    }

    public YAPIONPacketReceiver add(YAPIONPacketHandler yapionPacketHandler, String... packetTypes) {
        return add(packetTypes, yapionPacketHandler);
    }

    public void handle(YAPIONPacket yapionPacket) {
        handlerMap.get(yapionPacket.getType()).handlePacket(yapionPacket);
    }

}
