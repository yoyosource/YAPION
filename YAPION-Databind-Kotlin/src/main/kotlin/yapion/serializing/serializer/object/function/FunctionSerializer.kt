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

package yapion.serializing.serializer.`object`.function

import yapion.annotations.api.SerializerImplementation
import yapion.exceptions.serializing.YAPIONDeserializerException
import yapion.exceptions.serializing.YAPIONSerializerException
import yapion.hierarchy.api.groups.YAPIONAnyType
import yapion.hierarchy.types.YAPIONObject
import yapion.hierarchy.types.YAPIONValue
import yapion.serializing.YAPIONFlag
import yapion.serializing.YAPIONFlags.CheckOptions
import yapion.serializing.data.DeserializeData
import yapion.serializing.data.SerializeData
import yapion.serializing.serializer.FinalInternalSerializer
import yapion.utils.IdentifierUtils

@SerializerImplementation(since = "1.0.0")
class FunctionSerializer : FinalInternalSerializer<Function<Any>> {

    override fun type(): Class<*> {
        return Function::class.java
    }

    override fun interfaceType(): Class<*>? {
        return Function::class.java
    }

    override fun finished(): Boolean {
        return false
    }

    override fun saveWithoutAnnotation(): Boolean {
        return true
    }

    override fun loadWithoutAnnotation(): Boolean {
        return true
    }

    override fun serialize(serializeData: SerializeData<Function<Any>>): YAPIONAnyType {
        if (!serializeData.getYAPIONFlags().isSet(YAPIONFlag.CLASS_INJECTION)) {
            serializeData.signalDataLoss()
            return YAPIONValue<Any>(null)
        }
        val yapionObject = serializeData.serialize(serializeData.`object`.javaClass) as YAPIONObject
        yapionObject.add(IdentifierUtils.CLASS_IDENTIFIER, serializeData.`object`.javaClass)
        yapionObject.add(IdentifierUtils.TYPE_IDENTIFIER, type())
        yapionObject.remap("byteCode", "@byteCode")
        yapionObject.remove("class")
        return yapionObject
    }

    override fun deserialize(deserializeData: DeserializeData<out YAPIONAnyType>): Function<Any> {
        deserializeData.yapionFlags.isNotSet(YAPIONFlag.CLASS_INJECTION) {
            deserializeData.signalDataLoss()
            throw YAPIONDeserializerException("ClassInjection is not turned on")
        }
        val yapionObject = deserializeData.`object` as YAPIONObject
        val classObject = YAPIONObject(Class::class.java)
        classObject.add("class", yapionObject.getValue(IdentifierUtils.CLASS_IDENTIFIER))
        classObject.add("byteCode", yapionObject.getValue("@byteCode"))
        val clazz = deserializeData.deserialize<Class<*>>(classObject)
        return try {
            clazz.cast(deserializeData.getInstanceByFactoryOrObjenesis(clazz)) as Function<Any>
        } catch (e: Exception) {
            throw YAPIONDeserializerException(e.message, e)
        }
    }
}