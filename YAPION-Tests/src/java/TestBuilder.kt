import yapion.*

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
    val value = yapionObject {
        this["i"] = yapionObject {
            this["0"] = yapionObject { }
        }
        "j"() with yapionObject {  }
        "j" {
            yapionObject {

            }
        }
        "t" Object {
            + "t" with yapionObject {  }
        }
        + "j" with yapionObject {
            + "0" with yapionObject {  }
        }
        "hello" Map {
            yapionObject {

            } Object {

            }
            yapionMap {

            } Map {

            }
            yapionArray {

            } Array {

            }
            yapionValue("") Value("")
            yapionPointer(this.yapionDataType) Pointer(this@yapionObject)
        }
        "array" Array {
            Object {
                "j" Value("Hello World")
            }
        }
        "tst" Pointer(this@yapionObject)
    }
    println(value)
}