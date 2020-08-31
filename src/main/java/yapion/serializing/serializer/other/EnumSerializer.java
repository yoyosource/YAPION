package yapion.serializing.serializer.other;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

@SuppressWarnings({"java:S1192"})
public class EnumSerializer implements InternalSerializer<Enum<?>> {

    @Override
    public String type() {
        return "java.lang.Enum";
    }

    @Override
    public YAPIONAny serialize(Enum<?> object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type()));
        yapionObject.add("@enum", new YAPIONValue<>(object.getClass().getTypeName()));
        yapionObject.add("value", new YAPIONValue<>(object.name()));
        yapionObject.add("ordinal", new YAPIONValue<>(object.ordinal()));
        return yapionObject;
    }

    @Override
    public Enum<?> deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAny;
        String type = yapionObject.getValue("@enum", "").get();
        String enumType = yapionObject.getValue("value", "").get();
        int ordinal = yapionObject.getValue("ordinal", 0).get();
        try {
            if (!Class.forName(type).isEnum()) return null;
            Enum<?>[] enums = (Enum<?>[]) Class.forName(type).getEnumConstants();
            if (enums[ordinal].name().equals(enumType)) {
                return enums[ordinal];
            }
            for (Enum<?> e : enums) {
                if (e.name().equals(enumType)) {
                    yapionObject.add("ordinal", new YAPIONValue<>(e.ordinal()));
                    return e;
                }
            }
        } catch (ClassNotFoundException e) {
            // Ignored
        }
        return null;
    }
}
