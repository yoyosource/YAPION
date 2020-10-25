package yapion.serializing.data;

import lombok.RequiredArgsConstructor;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.serializing.YAPIONDeserializer;

import java.lang.reflect.Field;

@RequiredArgsConstructor
@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class DeserializeData<T extends YAPIONAnyType> {

    public final T object;
    public final String context;
    private final YAPIONDeserializer yapionDeserializer;

    public <R extends YAPIONAnyType> DeserializeData<R> clone(R object) {
        return new DeserializeData<>(object, context, yapionDeserializer);
    }

    @SuppressWarnings({"java:S3011"})
    public boolean deserialize(String fieldName, Object object, YAPIONAnyType yapionAnyType) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, deserialize(yapionAnyType));
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {

        }
        return false;
    }

    public Object deserialize(YAPIONAnyType yapionAnyType) {
        return yapionDeserializer.parse(yapionAnyType);
    }

}
