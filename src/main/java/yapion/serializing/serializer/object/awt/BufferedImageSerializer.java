// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.awt;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.0")
public class BufferedImageSerializer implements InternalSerializer<BufferedImage> {

    @Override
    public String type() {
        return "java.awt.image.BufferedImage";
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
                YAPIONArray pixel = (YAPIONArray) yapionArray.get(index);
                int[] ints = new int[4];
                for (int i = 0; i < ints.length; i++) {
                    ints[i] = (int) deserializeData.deserialize(pixel.get(i));
                }
                writableRaster.setPixel(x, y, ints);
                index++;
            }
        }
        return bufferedImage;
    }

}