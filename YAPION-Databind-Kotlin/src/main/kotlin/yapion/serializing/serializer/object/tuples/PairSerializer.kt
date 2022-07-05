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

package yapion.serializing.serializer.`object`.tuples

import yapion.annotations.api.SerializerImplementation
import yapion.hierarchy.api.groups.YAPIONAnyType
import yapion.hierarchy.types.YAPIONObject
import yapion.serializing.data.DeserializeData
import yapion.serializing.data.SerializeData
import yapion.serializing.serializer.FinalInternalSerializer

@SerializerImplementation(since = "1.0.0")
class PairSerializer: FinalInternalSerializer<Pair<Any, Any>> {

    override fun type(): Class<*> {
        return Pair::class.java
    }

    override fun serialize(serializeData: SerializeData<Pair<Any, Any>>?): YAPIONAnyType {
        return YAPIONObject().add("first", serializeData!!.serialize(serializeData.`object`.first))
            .add("second", serializeData.serialize(serializeData.`object`.second))
    }

    override fun deserialize(deserializeData: DeserializeData<out YAPIONAnyType>?): Pair<Any, Any> {
        val yapionObject = deserializeData!!.`object` as YAPIONObject
        return Pair(
            deserializeData.deserialize(yapionObject.get("first")),
            deserializeData.deserialize(yapionObject.get("second"))
        )
    }
}