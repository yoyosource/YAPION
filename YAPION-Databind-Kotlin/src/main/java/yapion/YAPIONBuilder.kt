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

package yapion

import yapion.annotations.api.YAPIONPrimitive
import yapion.hierarchy.api.groups.YAPIONAnyType
import yapion.hierarchy.api.groups.YAPIONDataType
import yapion.hierarchy.types.*

interface Builder<T : YAPIONDataType<*, *>> {
    val yapionDataType: T
}

interface YAPIONBuilder<K, T : YAPIONDataType<*, *>>: Builder<T> {
    fun add(key: K, value: YAPIONAnyType)

    operator fun K.unaryPlus(): Key<K> {
        return Key(this, this@YAPIONBuilder)
    }

    operator fun K.invoke(): Key<K> {
        return Key(this, this@YAPIONBuilder)
    }

    operator fun K.invoke(value: () -> YAPIONAnyType) {
        add(this, value())
    }

    operator fun set(key: K, value: YAPIONAnyType) {
        add(key, value)
    }

    infix fun K.Object(init: YAPIONObjectBuilder.() -> Unit) {
        val yapionObjectBuilder = YAPIONObjectBuilder()
        yapionObjectBuilder.init()
        add(this, yapionObjectBuilder.yapionObject)
    }

    infix fun K.Array(init: YAPIONArrayBuilder.() -> Unit) {
        val yapionArrayBuilder = YAPIONArrayBuilder()
        yapionArrayBuilder.init()
        add(this, yapionArrayBuilder.yapionArray)
    }

    infix fun K.Map(init: YAPIONMapBuilder.() -> Unit) {
        val yapionMapBuilder = YAPIONMapBuilder()
        yapionMapBuilder.init()
        add(this, yapionMapBuilder.yapionMap)
    }

    infix fun <@YAPIONPrimitive T> K.Value(value: T) {
        add(this, YAPIONValue(value))
    }

    infix fun K.Pointer(pointer: Builder<*>) {
        add(this, YAPIONPointer(pointer.yapionDataType))
    }
}

class Key<K>(internal val key: K, internal val yapionBuilder: YAPIONBuilder<K, *>)

infix fun <K> Key<K>.with(value: YAPIONAnyType) {
    this.yapionBuilder.add(this.key, value)
}

class YAPIONObjectBuilder : YAPIONBuilder<String, YAPIONObject> {
    internal val yapionObject = YAPIONObject()

    override val yapionDataType: YAPIONObject
        get() = yapionObject

    override fun add(key: String, value: YAPIONAnyType) {
        yapionObject.add(key, value)
    }
}

class YAPIONArrayBuilder : Builder<YAPIONArray> {
    internal val yapionArray = YAPIONArray()

    override val yapionDataType: YAPIONArray
        get() = yapionArray

    fun Object(init: YAPIONObjectBuilder.() -> Unit) {
        val yapionObjectBuilder = YAPIONObjectBuilder()
        yapionObjectBuilder.init()
        yapionArray.add(yapionObjectBuilder.yapionObject)
    }

    fun Array(init: YAPIONArrayBuilder.() -> Unit) {
        val yapionArrayBuilder = YAPIONArrayBuilder()
        yapionArrayBuilder.init()
        yapionArray.add(yapionArrayBuilder.yapionArray)
    }

    fun Map(init: YAPIONMapBuilder.() -> Unit) {
        val yapionMapBuilder = YAPIONMapBuilder()
        yapionMapBuilder.init()
        yapionArray.add(yapionMapBuilder.yapionMap)
    }

    fun <@YAPIONPrimitive T> Value(value: T) {
        yapionArray.add(YAPIONValue(value))
    }

    fun Pointer(pointer: Builder<*>) {
        yapionArray.add(YAPIONPointer(pointer.yapionDataType))
    }
}

class YAPIONMapBuilder : YAPIONBuilder<YAPIONAnyType, YAPIONMap> {
    internal val yapionMap = YAPIONMap()

    override val yapionDataType: YAPIONMap
        get() = yapionMap

    override fun add(key: YAPIONAnyType, value: YAPIONAnyType) {
        yapionMap.add(key, value)
    }
}

fun yapionObject(init: YAPIONObjectBuilder.() -> Unit): YAPIONObject {
    val yapionObjectBuilder = YAPIONObjectBuilder()
    yapionObjectBuilder.init()
    return yapionObjectBuilder.yapionObject
}

fun yapionArray(init: YAPIONArrayBuilder.() -> Unit): YAPIONArray {
    val yapionArrayBuilder = YAPIONArrayBuilder()
    yapionArrayBuilder.init()
    return yapionArrayBuilder.yapionArray
}

fun yapionMap(init: YAPIONMapBuilder.() -> Unit): YAPIONMap {
    val yapionMapBuilder = YAPIONMapBuilder()
    yapionMapBuilder.init()
    return yapionMapBuilder.yapionMap
}

fun <@YAPIONPrimitive T> yapionValue(value: T): YAPIONValue<T> {
    return YAPIONValue(value)
}

fun yapionPointer(value: YAPIONDataType<*, *>): YAPIONPointer {
    return YAPIONPointer(value)
}