// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.serializing.YAPIONDeserializerException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import static yapion.utils.IdentifierUtils.ENUM_IDENTIFIER;
import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SuppressWarnings({"java:S1192"})
@SerializerImplementation
public class EnumSerializer implements InternalSerializer<Enum<?>> {

    @Override
    public String type() {
        return "java.lang.Enum";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Enum<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add(ENUM_IDENTIFIER, serializeData.object.getClass().getTypeName());
        yapionObject.add("value", serializeData.object.name());
        yapionObject.add("ordinal", serializeData.object.ordinal());
        return yapionObject;
    }

    @Override
    public Enum<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String type = yapionObject.getValue(ENUM_IDENTIFIER, "").get();
        String enumType = yapionObject.getValue("value", "").get();
        int ordinal = -1;
        if (yapionObject.getValue("ordinal", 0) != null) {
            ordinal = yapionObject.getValue("ordinal", 0).get();
        }
        try {
            if (!Class.forName(type).isEnum()) {
                throw new YAPIONDeserializerException("Class " + type + " is not an enum");
            }
            Enum<?>[] enums = (Enum<?>[]) Class.forName(type).getEnumConstants();
            if (ordinal >= 0 && ordinal < enums.length && enums[ordinal].name().equals(enumType)) {
                return enums[ordinal];
            }
            for (Enum<?> e : enums) {
                if (e.name().equals(enumType)) {
                    yapionObject.add("ordinal", new YAPIONValue<>(e.ordinal()));
                    return e;
                }
            }
            throw new YAPIONDeserializerException("Enum element not found: " + type + "[" + enumType + "]");

        } catch (ClassNotFoundException e) {
            throw new YAPIONDeserializerException("Class not found: " + type);
        }
    }

}
