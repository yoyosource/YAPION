package yapion.packet;

public class YAPIONPacketTestObjects {

    public enum TestType {
        DropHandler,
        ErrorHandler,
        ExceptionHandler,
        HandleFailedHandler,
        UnknownHandler,
        DeserializationExceptionHandler,
        ValidHandler
    }

    public static class TestPacket extends YAPIONPacket {

    }

    public static class TestPacketTwo extends YAPIONPacket {

    }


}
