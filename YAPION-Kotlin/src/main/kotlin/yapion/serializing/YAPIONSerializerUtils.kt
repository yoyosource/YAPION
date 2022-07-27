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

package yapion.serializing

import yapion.hierarchy.types.YAPIONArray
import yapion.hierarchy.types.YAPIONMap
import yapion.hierarchy.types.YAPIONObject

fun <T> serialize(value: Array<T>): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun serialize(value: BooleanArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun serialize(value: ByteArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun serialize(value: CharArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun serialize(value: DoubleArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun serialize(value: FloatArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun serialize(value: IntArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun serialize(value: LongArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun serialize(value: ShortArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun <T: Any> serialize(value: T): YAPIONObject {
    return YAPIONSerializer.serialize(value)
}

@ExperimentalUnsignedTypes
fun serialize(value: UByteArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

@ExperimentalUnsignedTypes
fun serialize(value: UIntArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

@ExperimentalUnsignedTypes
fun serialize(value: ULongArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

@ExperimentalUnsignedTypes
fun serialize(value: UShortArray): YAPIONArray {
    return YAPIONSerializer.serialize(value)
}

fun <T> deserialize(value: YAPIONObject): T {
    return YAPIONDeserializer.deserialize(value)
}

fun <T> deserialize(value: YAPIONArray): Array<T> {
    return YAPIONDeserializer.deserialize(value)
}