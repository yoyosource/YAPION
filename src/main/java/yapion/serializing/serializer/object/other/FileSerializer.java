// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing.serializer.object.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.io.File;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class FileSerializer implements InternalSerializer<File> {

    @Override
    public String type() {
        return "java.io.File";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<File> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("absolutePath", serializeData.object.getAbsolutePath());
        return yapionObject;
    }

    @Override
    public File deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        return new File(yapionObject.getValue("absolutePath", "").get());
    }

}