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

package yapion.serializing.serializer.object.awt;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.awt.*;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.19.0")
public class ColorSerializer implements InternalSerializer<Color> {

    @Override
    public String type() {
        return "java.awt.Color";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Color> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("red", serializeData.object.getRed());
        yapionObject.add("green", serializeData.object.getGreen());
        yapionObject.add("blue", serializeData.object.getBlue());
        yapionObject.add("alpha", serializeData.object.getAlpha());
        return yapionObject;
    }

    @Override
    public Color deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int red = yapionObject.getValue("red", 0).get();
        int green = yapionObject.getValue("green", 0).get();
        int blue = yapionObject.getValue("blue", 0).get();
        int alpha = yapionObject.getValue("alpha", 0).get();
        return new Color(red, green, blue, alpha);
    }

}
