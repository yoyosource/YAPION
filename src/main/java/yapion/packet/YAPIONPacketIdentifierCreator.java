package yapion.packet;

@FunctionalInterface
public interface YAPIONPacketIdentifierCreator<T> {

    YAPIONPacketIdentifier<T> identifier();

}
