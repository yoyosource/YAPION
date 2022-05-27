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

package yapion.hierarchy

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

    infix fun K.Object(init: YAPIONObjectBuilder.() -> Unit) = add(this, initPattern(YAPIONObjectBuilder(), init).yapionObject)

    infix fun K.Array(init: YAPIONArrayBuilder.() -> Unit) = add(this, initPattern(YAPIONArrayBuilder(), init).yapionArray)

    infix fun K.Map(init: YAPIONMapBuilder.() -> Unit) = add(this, initPattern(YAPIONMapBuilder(), init).yapionMap)

    infix fun <@YAPIONPrimitive T> K.Value(value: T?) {
        add(this, YAPIONValue(value))
    }

    infix fun K.Pointer(pointer: Builder<*>) {
        add(this, YAPIONPointer(pointer.yapionDataType))
    }

    infix fun K.Pointer(value: YAPIONDataType<*, *>) {
        add(this, YAPIONPointer(value))
    }

    infix fun K.with(value: YAPIONAnyType) {
        add(this, value)
    }

    infix fun <@YAPIONPrimitive T> K.with(value: T) {
        add(this, YAPIONValue(value))
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

    fun Object(init: YAPIONObjectBuilder.() -> Unit) = yapionArray.add(initPattern(YAPIONObjectBuilder(), init).yapionObject)

    fun Array(init: YAPIONArrayBuilder.() -> Unit) = yapionArray.add(initPattern(YAPIONArrayBuilder(), init).yapionArray)

    fun Map(init: YAPIONMapBuilder.() -> Unit) = yapionArray.add(initPattern(YAPIONMapBuilder(), init).yapionMap)

    fun <@YAPIONPrimitive T> Value(value: T?) {
        yapionArray.add(YAPIONValue(value))
    }

    fun Pointer(pointer: Builder<*>) {
        yapionArray.add(YAPIONPointer(pointer.yapionDataType))
    }

    fun Pointer(value: YAPIONDataType<*, *>) {
        yapionArray.add(YAPIONPointer(value))
    }
}

class YAPIONMapBuilder : YAPIONBuilder<YAPIONAnyType, YAPIONMap> {
    internal val yapionMap = YAPIONMap()

    override val yapionDataType: YAPIONMap
        get() = yapionMap

    override fun add(key: YAPIONAnyType, value: YAPIONAnyType) {
        yapionMap.add(key, value)
    }

    fun Object(init: YAPIONObjectBuilder.() -> Unit) = initPattern(YAPIONObjectBuilder(), init).yapionObject

    fun Array(init: YAPIONArrayBuilder.() -> Unit) = initPattern(YAPIONArrayBuilder(), init).yapionArray

    fun Map(init: YAPIONMapBuilder.() -> Unit) = initPattern(YAPIONMapBuilder(), init).yapionMap

    fun <@YAPIONPrimitive T> Value(value: T?) = YAPIONValue(value)

    fun Pointer(pointer: Builder<*>) = YAPIONPointer(pointer.yapionDataType)
    fun Pointer(value: YAPIONDataType<*, *>) = YAPIONPointer(value)

    @JvmName("with1")
    infix fun <@YAPIONPrimitive T> T.with(value: T) {
        add(YAPIONValue(this), YAPIONValue(value))
    }
}

fun <P> initPattern(p: P, init: P.() -> Unit): P {
    p.init()
    return p
}

fun yapionObject(init: YAPIONObjectBuilder.() -> Unit): YAPIONObject = initPattern(YAPIONObjectBuilder(), init).yapionObject

fun yapionArray(init: YAPIONArrayBuilder.() -> Unit): YAPIONArray = initPattern(YAPIONArrayBuilder(), init).yapionArray

fun yapionMap(init: YAPIONMapBuilder.() -> Unit): YAPIONMap = initPattern(YAPIONMapBuilder(), init).yapionMap

fun <@YAPIONPrimitive T> yapionValue(value: T?): YAPIONValue<T> {
    return YAPIONValue(value)
}

fun yapionPointer(value: YAPIONDataType<*, *>): YAPIONPointer {
    return YAPIONPointer(value)
}