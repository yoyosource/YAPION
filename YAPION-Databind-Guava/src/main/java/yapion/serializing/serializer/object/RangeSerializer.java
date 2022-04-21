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

package yapion.serializing.serializer.object;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

public class RangeSerializer implements FinalInternalSerializer<Range<?>> {

    @Override
    public Class<?> type() {
        return Range.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Range<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        if (serializeData.object.hasLowerBound()) {
            yapionObject.add("lowerBound", serializeData.serialize(serializeData.object.lowerEndpoint()));
            yapionObject.add("lowerBoundType", serializeData.serialize(serializeData.object.lowerBoundType()));
        }
        if (serializeData.object.hasUpperBound()) {
            yapionObject.add("upperBound", serializeData.serialize(serializeData.object.upperEndpoint()));
            yapionObject.add("upperBoundType", serializeData.serialize(serializeData.object.upperBoundType()));
        }
        return yapionObject;
    }

    @Override
    public Range<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        Object lowerBound = null;
        BoundType lowerBoundType = null;
        Object upperBound = null;
        BoundType upperBoundType = null;
        if (yapionObject.containsKey("lowerBound")) {
            lowerBound = deserializeData.deserialize(yapionObject.get("lowerBound"));
            lowerBoundType = deserializeData.deserialize(yapionObject.get("lowerBoundType"));
        }
        if (yapionObject.containsKey("upperBound")) {
            upperBound = deserializeData.deserialize(yapionObject.get("upperBound"));
            upperBoundType = deserializeData.deserialize(yapionObject.get("upperBoundType"));
        }

        if (lowerBound == null && upperBound == null) {
            return Range.all();
        }
        if (lowerBound == null) {
            return Range.upTo((Comparable<?>) upperBound, upperBoundType);
        }
        if (upperBound == null) {
            return Range.downTo((Comparable<?>) lowerBound, lowerBoundType);
        }
        return Range.range((Comparable<?>) lowerBound, lowerBoundType, (Comparable<?>) upperBound, upperBoundType);
    }
}
