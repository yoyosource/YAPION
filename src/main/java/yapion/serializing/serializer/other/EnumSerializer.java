// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
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
    public YAPIONAnyType serialize(Enum<?> object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, new YAPIONValue<>(type()));
        yapionObject.add(ENUM_IDENTIFIER, new YAPIONValue<>(object.getClass().getTypeName()));
        yapionObject.add("value", new YAPIONValue<>(object.name()));
        yapionObject.add("ordinal", new YAPIONValue<>(object.ordinal()));
        return yapionObject;
    }

    @Override
    public Enum<?> deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
        String type = yapionObject.getValue(ENUM_IDENTIFIER, "").get();
        String enumType = yapionObject.getValue("value", "").get();
        int ordinal = -1;
        if (yapionObject.getValue("ordinal", 0) != null) {
            ordinal = yapionObject.getValue("ordinal", 0).get();;
        }
        try {
            if (!Class.forName(type).isEnum()) return null;
            Enum<?>[] enums = (Enum<?>[]) Class.forName(type).getEnumConstants();
            if (ordinal != -1 && enums[ordinal].name().equals(enumType)) {
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