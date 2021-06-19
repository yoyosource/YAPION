package yapion.serializing.serializer.special;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.registration.YAPIONSerializing;

@AllArgsConstructor
@Getter
@YAPIONSerializing
@YAPIONData
public class RecordValue {
    protected String name;
    protected int index;
    protected Class<?> type;
    protected Object value;
}
