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

/**
 * This class exists since Java is incapable to handle Type-Parameters properly...
 */
package yapion.serializing

fun <T> test(internalSerializer: InternalSerializer<T>) {
    // println(TypeManagerMagic.getType(internalSerializer as InternalSerializer<T>))
}

class TypeManagerMagic {

    companion object {
        inline fun <reified T> getType(internalSerializer: InternalSerializer<T>): Class<T> {
            return T::class.java
        }
    }
}