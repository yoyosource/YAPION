package yapion.serializing.data;

import lombok.RequiredArgsConstructor;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.serializing.YAPIONSerializer;

import java.lang.reflect.Field;

@RequiredArgsConstructor
@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class SerializeData<T> {

    public final T object;
    public final String context;
    private final YAPIONSerializer yapionSerializer;

    @SuppressWarnings({"java:S3011"})
    public final YAPIONAnyType serialize(String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return serialize(field.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new YAPIONSerializerException();
        }
    }

    public final YAPIONAnyType serialize(Object o) {
        return yapionSerializer.parse(o);
    }

}
