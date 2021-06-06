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

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.diff.DiffBase;
import yapion.hierarchy.diff.YAPIONDiff;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.util.List;

@SerializerImplementation(since = "0.25.0")
public class YAPIONDiffSerializer implements InternalSerializer<YAPIONDiff> {

    private static class YAPIONDiffOther extends YAPIONDiff {}

    @Override
    public Class<?> type() {
        return YAPIONDiff.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<YAPIONDiff> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("diff", serializeData.serialize(serializeData.object.getDiffs()));
        return yapionObject;
    }

    @Override
    public YAPIONDiff deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        List<DiffBase> diffs = deserializeData.deserialize(yapionObject.getObject("diff"));
        YAPIONDiff yapionDiff = new YAPIONDiffOther();
        yapionDiff.getDiffs().addAll(diffs);
        return yapionDiff;
    }
}
