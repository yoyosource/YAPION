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

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.20.0")
public class BufferedImageSerializer implements InternalSerializer<BufferedImage> {

    @Override
    public Class<?> type() {
        return BufferedImage.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<BufferedImage> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("height", serializeData.serialize(serializeData.object.getHeight()));
        yapionObject.add("width", serializeData.serialize(serializeData.object.getWidth()));
        yapionObject.add("type", serializeData.object.getType());

        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("pixels", yapionArray);
        WritableRaster writableRaster = serializeData.object.getRaster();
        for (int y = 0; y < serializeData.object.getHeight(); y++) {
            for (int x = 0; x < serializeData.object.getWidth(); x++) {
                int[] ints = writableRaster.getPixel(x, y, new int[4]);
                yapionArray.add(serializeData.serialize(ints));
            }
        }
        return yapionObject;
    }

    @Override
    public BufferedImage deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int height = yapionObject.getValue("height", 0).get();
        int width = yapionObject.getValue("width", 0).get();
        int type = yapionObject.getValue("type", 0).get();

        YAPIONArray yapionArray = yapionObject.getArray("pixels");
        int index = 0;

        BufferedImage bufferedImage = new BufferedImage(width, height, type);
        WritableRaster writableRaster = bufferedImage.getRaster();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                YAPIONArray pixel = (YAPIONArray) yapionArray.getYAPIONAnyType(index);
                int[] ints = new int[4];
                for (int i = 0; i < ints.length; i++) {
                    ints[i] = (int) deserializeData.deserialize(pixel.getYAPIONAnyType(i));
                }
                writableRaster.setPixel(x, y, ints);
                index++;
            }
        }
        return bufferedImage;
    }

}
