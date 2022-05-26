import yapion.annotations.`object`.YAPIONData
import yapion.hierarchy.types.YAPIONObject
import yapion.serializing.YAPIONDeserializer
import yapion.serializing.YAPIONSerializer
import kotlin.properties.Delegates

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

fun main() {
    val initial = TestData(0.toUByte(), "Hello World")
    val yapionObject: YAPIONObject = YAPIONSerializer.serialize(initial)
    println(yapionObject)
    val testData: TestData = YAPIONDeserializer.deserialize(yapionObject)
    println(testData)
}

@YAPIONData
open class TestData(open var data: UByte, var testString: String) {
    val lazyTest: Lazy<Byte> = lazy { data.toByte() }

    var test: Int = 0
        get() = 0
        set(test) {
            field = test
        }

    val s : String by lazy { testString }

    val max : Int by Delegates.vetoable(0) { _, oldValue, newValue -> newValue > oldValue }

    override fun toString(): String {
        return "TestData(data=$data, lazyTest=$lazyTest, test=$test, s='$s', max='$max')"
    }
}