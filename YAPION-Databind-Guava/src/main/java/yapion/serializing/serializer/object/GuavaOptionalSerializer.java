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

import com.google.common.base.Optional;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

public class GuavaOptionalSerializer implements FinalInternalSerializer<Optional<?>> {

    @Override
    public Class<?> type() {
        return Optional.class;
    }

    @Override
    public Class<?> classType() {
        return Optional.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Optional<?>> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("isPresent", serializeData.object.isPresent());
        if (serializeData.object.isPresent()) {
            yapionObject.add("value", serializeData.serialize(serializeData.object.get()));
        }
        return yapionObject;
    }

    @Override
    public Optional<?> deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        if (yapionObject.getBoolean("isPresent")) {
            return Optional.of(deserializeData.deserialize(yapionObject.getAny("value")));
        }
        return Optional.absent();
    }
}
