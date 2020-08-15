// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.number;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.lang.reflect.Field;
import java.math.BigDecimal;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class BigDecimalSerializer implements Serializer<BigDecimal> {

    @Override
    public String type() {
        return "java.math.BigDecimal";
    }

    @Override
    public YAPIONAny serialize(BigDecimal object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public BigDecimal deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer, Field field) {
        return ((YAPIONValue<BigDecimal>) yapionAny).get();
    }
}