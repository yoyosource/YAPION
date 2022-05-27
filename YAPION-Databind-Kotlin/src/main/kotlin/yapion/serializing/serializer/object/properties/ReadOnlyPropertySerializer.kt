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

package yapion.serializing.serializer.`object`.properties

import yapion.annotations.api.SerializerImplementation
import yapion.hierarchy.api.groups.YAPIONAnyType
import yapion.serializing.data.DeserializeData
import yapion.serializing.data.SerializeData
import yapion.serializing.serializer.FinalInternalSerializer
import kotlin.properties.ReadOnlyProperty

@SerializerImplementation(since = "1.0.0")
class ReadOnlyPropertySerializer : FinalInternalSerializer<ReadOnlyProperty<Any, Any>> {

    override fun type(): Class<*> {
        return ReadOnlyProperty::class.java
    }

    override fun interfaceType(): Class<*> {
        return ReadOnlyProperty::class.java
    }

    override fun empty(): Boolean {
        return true
    }

    override fun saveWithoutAnnotation(): Boolean {
        return true
    }

    override fun loadWithoutAnnotation(): Boolean {
        return true
    }

    override fun createWithObjenesis(): Boolean {
        return true
    }

    override fun serialize(serializeData: SerializeData<ReadOnlyProperty<Any, Any>>?): YAPIONAnyType? {
        return null
    }

    override fun deserialize(deserializeData: DeserializeData<out YAPIONAnyType>?): ReadOnlyProperty<Any, Any>? {
        return null
    }
}