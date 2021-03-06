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

package yapion.serializing.serializer.object.time;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.time.Period;

@SerializerImplementation(since = "0.18.0")
public class PeriodSerializer implements FinalInternalSerializer<Period> {

    @Override
    public Class<?> type() {
        return Period.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Period> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        yapionObject.add("years", serializeData.object.getYears());
        yapionObject.add("months", serializeData.object.getMonths());
        yapionObject.add("days", serializeData.object.getDays());
        return yapionObject;
    }

    @Override
    public Period deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        int years = yapionObject.getValue("years", 0).get();
        int months = yapionObject.getValue("months", 0).get();
        int days = yapionObject.getValue("days", 0).get();
        return Period.of(years, months, days);
    }
}
