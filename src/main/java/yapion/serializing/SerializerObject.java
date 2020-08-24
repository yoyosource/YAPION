package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.types.YAPIONObject;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public interface SerializerObject<T> {

    Class<T> type();
    YAPIONObject serialize(T object, YAPIONSerializer yapionSerializer);
    T deserialize(YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer);

    /**
     * Create a Serializer from three interfaces: TypeGetter,
     * SerializationGetter and DeserializationGetter. Those are
     * all FunctionalInterfaces with just one method to override.
     * This makes the use off those interfaces really easy.
     * If any of the three arguments is {@code null} an
     * YAPIONException is thrown.
     *
     * @param typeGetter the TypeGetter
     * @param serializationGetter the SerializationGetter
     * @param deserializationGetter the DeserializationGetter
     * @param <T> the Type of this Serializer
     * @return the Serializer that wraps the Interfaces
     */
    static <T> SerializerObject<T> Serializer(TypeGetter<T> typeGetter, SerializationGetter<T> serializationGetter, DeserializationGetter<T> deserializationGetter) {
        if (typeGetter == null || serializationGetter == null || deserializationGetter == null) {
            throw new YAPIONException();
        }
        return new SerializerObject<T>() {
            @Override
            public Class<T> type() {
                return typeGetter.type();
            }

            @Override
            public YAPIONObject serialize(T object, YAPIONSerializer yapionSerializer) {
                return serializationGetter.serialize(object, yapionSerializer);
            }

            @Override
            public T deserialize(YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer) {
                return deserializationGetter.deserialize(yapionObject, yapionDeserializer);
            }
        };
    }

    @FunctionalInterface
    interface TypeGetter<T> {
        Class<T> type();
    }

    @FunctionalInterface
    interface SerializationGetter<T> {
        YAPIONObject serialize(T object, YAPIONSerializer yapionSerializer);
    }

    @FunctionalInterface
    interface DeserializationGetter<T> {
        T deserialize(YAPIONObject yapionObject, YAPIONDeserializer yapionDeserializer);
    }

}
