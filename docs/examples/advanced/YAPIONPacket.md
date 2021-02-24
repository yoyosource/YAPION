# Packet System

YAPION in itself has an extensive Packet system to use between programms. To create a custom packet you need to extend YAPIONPacket. Every field of this class will be saved and loaded back by the serialization call. You can implement the reading and writing of those packets into an InputStream/OutputStream by yourself, or just use my YAPIONInputStream and YAPIONOutputStream in conjunction with a YAPIONSocket. With the YAPIONInputStream you can use the YAPIONPacketReceiver with a YAPIONPacketHandler. A YAPIONPacketHandler is used to handle any incoming Packets from the YAPIONInputStream, and the YAPIONPacketReceiver is used to bundle YAPIONPacketHandler together. It will handle any incoming packets and call the specified YAPIONPacketHandler.

```java
import yapion.packet.YAPIONPacketReceiver;

public class ExampleReceiver {

    public static void main(String[] args) {
        YAPIONPacketReceiver yapionPacketReceiver = new YAPIONPacketReceiver();
        yapionPacketReceiver.add(Class, yapionPacket -> {
            // Executed when a specific YAPIONPacket is handled denoted by 'Class'
        });
    }
    
}
```

There are some special receiver to keep in mind if using the YAPIONPacketReceiver. They handle Dropping of data or if deserialization fails or if an Exception was thrown in an user defined handle.

```java
import yapion.packet.YAPIONPacketReceiver;

public class ExampleReceiver {

    public static void main(String[] args) {
        YAPIONPacketReceiver yapionPacketReceiver = new YAPIONPacketReceiver();
        yapionPacketReceiver.setDropHandler(yapionPacket -> {
            // This code will be called for any data drop
        });
        yapionPacketReceiver.setUnknownHandler(yapionPacket -> {
            // This code will be called if an unknown packet was handled
        });
        yapionPacketReceiver.setDeserializationExceptionHandler(yapionPacket -> {
            // This code will be called if an Exception was thrown by deserialization
        });
        yapionPacketReceiver.setHandleFailedHandler(yapionPacket -> {
            // This code will be called if an unexpected this happened
        });
        yapionPacketReceiver.setExceptionHandler(yapionPacket -> {
            // This code will be called if an exception was thrown while handling
        });
        yapionPacketReceiver.setErrorHandler(yapionPacket -> {
            // This code will be called if an exception was thrown internally or in the exception handler.
        });
    }

}
```

All these handlers get used in conjunction with an YAPIONInputStream and the internal handling system of packets inside the YAPIONInputStream.