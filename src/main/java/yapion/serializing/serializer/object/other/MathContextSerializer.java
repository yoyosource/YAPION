/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.serializing.serializer.object.other;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.math.MathContext;
import java.math.RoundingMode;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.13.1")
public class MathContextSerializer implements InternalSerializer<MathContext> {

    @Override
    public Class<?> type() {
        return MathContext.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<MathContext> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("precision", serializeData.object.getPrecision());
        yapionObject.add("roundMode", serializeData.serialize(serializeData.object.getRoundingMode()));
        return yapionObject;
    }

    @Override
    public MathContext deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int precision = yapionObject.getValue("precision", 0).get();
        RoundingMode roundingMode = (RoundingMode) deserializeData.deserialize(yapionObject.getObject("roundMode"));
        return new MathContext(precision, roundingMode);
    }

}
