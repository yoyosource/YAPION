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

package yapion.serializing.serializer.`object`.reflection

import yapion.annotations.api.SerializerImplementation
import yapion.hierarchy.api.groups.YAPIONAnyType
import yapion.hierarchy.types.YAPIONObject
import yapion.serializing.data.DeserializeData
import yapion.serializing.data.SerializeData
import yapion.serializing.serializer.FinalInternalSerializer
import kotlin.reflect.KClass

@SerializerImplementation(since = "1.0.0")
class KClassSerializer : FinalInternalSerializer<KClass<Any>> {

    override fun type(): Class<*> {
        return KClass::class.java
    }

    override fun serialize(serializeData: SerializeData<KClass<Any>>): YAPIONAnyType {
        return YAPIONObject(type()).add("class", serializeData.serialize(serializeData.`object`.java))
    }

    override fun deserialize(deserializeData: DeserializeData<out YAPIONAnyType>): KClass<Any> {
        return deserializeData.deserialize<Class<Any>>((deserializeData.`object` as YAPIONObject).get("class")).kotlin
    }
}