// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.io.File;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class FileSerializer implements InternalSerializer<File> {

    @Override
    public String type() {
        return "java.io.File";
    }

    @Override
    public YAPIONAnyType serialize(File object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        yapionObject.add(new YAPIONVariable("absolutePath", new YAPIONValue<>(object.getAbsolutePath())));
        return yapionObject;
    }

    @Override
    public File deserialize(YAPIONAnyType yapionAnyType, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
        return new File(yapionObject.getValue("absolutePath", "").get());
    }
}