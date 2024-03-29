# Packet System [WIP]

YAPION in itself has an extensive packet system to use between programms. To create a custom packet you need to extend YAPIONPacket. Every field of this class will be saved and loaded back by the serialization call. You can implement the reading and writing of those packets into an Input/OutputStream by yourself, or just use the YAPIONInputStream and YAPIONOutputStream in conjunction with a YAPIONSocket. With the YAPIONInputStream you can use the YAPIONPacketReceiver with YAPIONPacketHandlers. A YAPIONPacketHandler is used to handle any incoming packets from the YAPIONInputStream, and the YAPIONPacketReceiver is used to bundle YAPIONPacketHandlers together. It will handle any incoming packets and call the specified YAPIONPacketHandler.

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

While using the YAPIONPacketReceiver some special receivers should be kept in mind. They handle dropping of data, failure of deserialization or any thrown exception in an user defined handler.

```java
import yapion.packet.YAPIONPacketReceiver;

public class ExampleReceiver {

    public static void main(String[] args) {
        YAPIONPacketReceiver yapionPacketReceiver = new YAPIONPacketReceiver();
        yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Hanlder.DROP, yapionPacket -> {
            // This 'yapionPacket' is an instance of 'DropPacket'
            // This code will be called for any data drop
        });
        yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Hanlder.UNKNOWN, yapionPacket -> {
            // This code will be called if an unknown packet was handled
        });
        yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Hanlder.DESERIALIZE_EXCEPTION, yapionPacket -> {
            // This 'yapionPacket' is an instance of 'DeserializationExceptionPacket'
            // This code will be called if an Exception was thrown by deserialization
        });
        yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Hanlder.HANDLE_FAILED, yapionPacket -> {
            // This 'yapionPacket' is an instance of 'HandleFailedPacket'
            // This code will be called if an unexpected this happened
        });
        yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Hanlder.EXCEPTION, yapionPacket -> {
            // This code will be called if an exception was thrown while handling
        });
        yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Hanlder.ERROR, yapionPacket -> {
            // This code will be called if an exception was thrown internally or in the exception handler.
        });
        yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Hanlder.FILTERED, yapionPacket -> {
            // This code will be called if an YAPIONPacket was filtered, see below
        });
        yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Hanlder.HEART_BEAT, yapionPacket -> {
            // This 'yapionPacket' is an instance of 'HeartBeatPacket'
            // This code will be called if a HeartBeat Packet was received, see below
        });
        yapionPacketReceiver.setHandler(YAPIONPacketReceiver.Hanlder.LOST_HEART_BEAT, yapionPacket -> {
            // This 'yapionPacket' is an instance of 'LostHeartBeatPacket'
            // This code will be called if a HeartBeat Packet was not received, see below
        });
    }

}
```

All these handlers are used in conjunction with an YAPIONInputStream and the internal handling system of packets inside the YAPIONInputStream.

```java
import yapion.hierarchy.output.StringOutput;
import yapion.packet.YAPIONInputStream;
import yapion.packet.YAPIONPacket;
import yapion.packet.YAPIONPacketReceiver;
import yapion.packet.YAPIONPacketStream;

import java.io.ByteArrayInputStream;
import java.util.UUID;

public class ExampleReceiver {

    public static void main(String[] args) {
        YAPIONPacketReceiver yapionPacketReceiver = new YAPIONPacketReceiver();
        // Adding the ExamplePacket to the 'yapionPacketReceiver'
        yapionPacketReceiver.add(ExamplePacket.class, yapionPacket -> {
            ExamplePacket examplePacket = (ExamplePacket) yapionPacket;
            System.out.println(examplePacket.uuid);
            System.out.println(examplePacket.time);
        });
        
        // Normally your input is read from an InputStream and not a ByteArrayInputStream 
        String stringPacket = new ExamplePacket().convertToYAPION().toYAPION(new StringOutput()).getResult();
        
        // This would not be just a string but an InputStream created by a socket or for a file or something else
        YAPIONPacketStream yapionPacketStream = new YAPIONPacketStream(new YAPIONInputStream(new ByteArrayInputStream(stringPacket.getBytes())), null);
        yapionPacketStream.setYAPIONPacketReceiver(yapionPacketReceiver);
    }

}

class ExamplePacket extends YAPIONPacket {

    UUID uuid = UUID.randomUUID();
    long time = System.currentTimeMillis();

}
```

If you use the YAPIONSocket instead of getting the InputStream and OutputStream from a socket yourself, you can get the YAPIONOutputStream from the YAPIONPacket corresponding to the YAPIONInputStream. You can define some more behaviour on the YAPIONPacketHandler individual for any implementation.

```java
import yapion.packet.YAPIONPacket;
import yapion.packet.YAPIONPacketHandler;

public class ExampleHandler {

    public static void main(String[] args) {
        YAPIONPacketHandler yapionPacketHandler = new YAPIONPacketHandler() {
            @Override
            public void handlePacket(YAPIONPacket yapionPacket) {
                // This gets called to handle the YAPIONPacket
            }

            @Override
            public boolean runThread() {
                // This defines if this handle call should be run in a separate Thread
                return false;
            }

            @Override
            public boolean daemonThread() {
                // This defines if the thread should be a daemon
                return false;
            }

            @Override
            public boolean ignoreException() {
                // This defines if any exception thrown from the #handlePacket(YAPIONPacket) should be ignored
                return false;
            }

            @Override
            public boolean closeOnException() {
                // This defines if the underlying outputStream should be closed by an exception from the #handlePacket(YAPIONPacket)
                return false;
            }
        };
    }

}
```

You can also create a YAPIONPacketHandler by the static call to `YAPIONPacketHandler.createInstance()`.

```java
import yapion.packet.YAPIONPacketHandler;

public class ExampleStaticHandler {

    public static void main(String[] args) {
        // arguments: Consumer<YAPIONPacket> yapionPacketConsumer, boolean runThread, boolean daemonThread, boolean ignoreException
        YAPIONPacketHandler.createInstance(yapionPacket -> {

        }, false, false, false);

        // arguments: Consumer<YAPIONPacket> yapionPacketConsumer, boolean runThread, boolean ignoreException
        YAPIONPacketHandler.createInstance(yapionPacket -> {

        }, false, false);
    }

}
```

If you use all of this in conjunction you can create a complex protocol for a client-client or client-server even server-server application. One example is a server time returning system.

```java
import yapion.packet.YAPIONPacket;
import yapion.packet.YAPIONPacketReceiver;
import yapion.packet.YAPIONPacketStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ExamplePacket {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(<PORT>)
        Socket socket = new Socket("localhost", <PORT>);
        Socket serverSocket = server.accept();

        YAPIONPacketStream yapionPacketStream = new YAPIONPacketStream(socket);
        YAPIONPacketStream serverYapionPacketStream = new YAPIONPacketStream(serverSocket);

        YAPIONPacketReceiver serverReceiver = new YAPIONPacketReceiver();
        serverReceiver.setHandler(YAPIONPacketReceiver.Handler.UNKNOWN, yapionPacket -> {
            try {
                yapionPacket.getYAPIONOutputStream().close();
            } catch (IOException e) {
                throw new SecurityException(e.getMessage(), e);
            }
        });
        serverReceiver.add(GetTimePacket.class, yapionPacket -> {
            // Return the time after the Time Request
            yapionPacket.getYAPIONOutputStream().write(new TimePacket());
        });
        serverYapionPacketStream.setYAPIONPacketReceiver(serverReceiver);

        yapionPacketStream.write(new GetTimePacket());
        TimePacket timePacket = (TimePacket) yapionPacketStream.readObject();
        System.out.println(timePacket.time);
    }

}

class GetTimePacket extends YAPIONPacket {

}

class TimePacket extends YAPIONPacket {

    long time = System.currentTimeMillis();

}
```

You can also use the internal `#closeOnException()` method declared in the 'YAPIONPacketHandler'.

```java
import yapion.packet.YAPIONPacket;
import yapion.packet.YAPIONPacketHandler;
import yapion.packet.YAPIONPacketReceiver;
import yapion.packet.YAPIONPacketStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ExamplePacket {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(<PORT>)
        Socket socket = new Socket("localhost", <PORT>);
        Socket serverSocket = server.accept();

        YAPIONPacketStream yapionPacketStream = new YAPIONPacketStream(socket);
        YAPIONPacketStream serverYapionPacketStream = new YAPIONPacketStream(serverSocket);

        YAPIONPacketReceiver serverReceiver = new YAPIONPacketReceiver();
        serverReceiver.setHanlder(YAPIONPacketReceiver.Hanlder.UNKNOWN, new YAPIONPacketHandler() {
            @Override
            public void handlePacket(YAPIONPacket yapionPacket) {
                throw new SecurityException();
            }

            @Override
            public boolean closeOnException() {
                return true;
            }
        });
        serverReceiver.add(GetTimePacket.class, yapionPacket -> {
            // Return the time after the Time Request
            yapionPacket.getYAPIONOutputStream().write(new TimePacket());
        });
        serverYapionPacketStream.setYAPIONPacketReceiver(serverReceiver);

        yapionPacketStream.write(new GetTimePacket());
        TimePacket timePacket = (TimePacket) yapionPacketStream.readObject();
        System.out.println(timePacket.time);
    }

}

class GetTimePacket extends YAPIONPacket {

}

class TimePacket extends YAPIONPacket {

    long time = System.currentTimeMillis();

}
```

HeartBeat und Filtering [WIP]