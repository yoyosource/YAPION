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

package yapion.serializing.serializer.object.yapion.diff;

import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.diff.DiffBase;
import yapion.hierarchy.diff.YAPIONDiff;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.util.List;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.25.0")
public class YAPIONDiffSerializer implements InternalSerializer<YAPIONDiff> {

    @Override
    public void init() {
        new YAPIONDiff.YAPIONDiffFactory().add();
    }

    @Override
    public String type() {
        return "yapion.hierarchy.diff.YAPIONDiff";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<YAPIONDiff> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("diff", serializeData.serialize(serializeData.object.getDiffs()));
        return yapionObject;
    }

    @Override
    @SuppressWarnings("unchecked")
    public YAPIONDiff deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        List<DiffBase> diffs = (List<DiffBase>) deserializeData.deserialize(yapionObject.getObject("diff"));
        try {
            YAPIONDiff yapionDiff = deserializeData.getInstance(YAPIONDiff.class);
            yapionDiff.getDiffs().addAll(diffs);
            return yapionDiff;
        } catch (ClassNotFoundException e) {
            // This should never be reached
            throw new YAPIONException(e.getMessage(), e);
        }
    }
}
