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

@DslMarker
annotation class YAPIONHierarchyBuilderMarker

@YAPIONHierarchyBuilderMarker
interface Builder<T : YAPIONAnyType> {
    val yapionDataType: T
}

interface YAPIONBuilder<K, T : YAPIONDataType<*, *>> : Builder<T> {
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

    infix fun K.Object(init: YAPIONObjectBuilder.() -> Unit) =
        add(this, initPattern(YAPIONObjectBuilder(), init).yapionObject)

    infix fun K.Array(init: YAPIONArrayBuilder.() -> Unit) =
        add(this, initPattern(YAPIONArrayBuilder(), init).yapionArray)

    infix fun K.Map(init: YAPIONMapBuilder.() -> Unit) =
        add(this, initPattern(YAPIONMapBuilder(), init).yapionMap)

    infix fun <@YAPIONPrimitive T> K.Value(value: T?) {
        add(this, YAPIONValue(value))
    }

    infix fun <L : YAPIONDataType<*, *>> K.Pointer(pointer: Builder<L>) {
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

    operator fun IntRange.times(element: YAPIONBuilder<K, T>.(Int) -> Unit) {
        this.forEach {
            this@YAPIONBuilder.element(it)
        }
    }

    operator fun Int.times(element: YAPIONBuilder<K, T>.(Int) -> Unit) =
        (0 until this) * element

    fun Objects(value: YAPIONObjectBuilder.(Int) -> K): YAPIONBuilder<K, T>.(Int) -> Unit {
        return {
            var key: K? = null
            val result = initPattern(YAPIONObjectBuilder()) {
                key = this.value(it)
            }
            this@YAPIONBuilder.add(key!!, result.yapionDataType)
        }
    }

    fun Arrays(value: YAPIONArrayBuilder.(Int) -> K): YAPIONBuilder<K, T>.(Int) -> Unit {
        return {
            var key: K? = null
            val result = initPattern(YAPIONArrayBuilder()) {
                key = this.value(it)
            }
            this@YAPIONBuilder.add(key!!, result.yapionDataType)
        }
    }

    fun Maps(value: YAPIONMapBuilder.(Int) -> K): YAPIONBuilder<K, T>.(Int) -> Unit {
        return {
            var key: K? = null
            val result = initPattern(YAPIONMapBuilder()) {
                key = this.value(it)
            }
            this@YAPIONBuilder.add(key!!, result.yapionDataType)
        }
    }

    fun <V> Values(value: (Int) -> Pair<K, V>): YAPIONArrayBuilder.(Int) -> Unit {
        return {
            val result = value(it)
            this@YAPIONBuilder.add(result.first, YAPIONValue(result.second))
        }
    }
}

class Key<K>(internal val key: K, internal val yapionBuilder: YAPIONBuilder<K, *>)

infix fun <K> Key<K>.with(value: YAPIONAnyType) {
    this.yapionBuilder.add(this.key, value)
}

open class YAPIONObjectBuilder : YAPIONBuilder<String, YAPIONObject> {
    internal val yapionObject = YAPIONObject()

    override val yapionDataType: YAPIONObject
        get() = yapionObject

    override fun add(key: String, value: YAPIONAnyType) {
        yapionObject.add(key, value)
    }
}

open class YAPIONArrayBuilder : Builder<YAPIONArray> {
    internal val yapionArray = YAPIONArray()

    override val yapionDataType: YAPIONArray
        get() = yapionArray

    fun Object(init: YAPIONObjectBuilder.() -> Unit) =
        yapionArray.add(initPattern(YAPIONObjectBuilder(), init).yapionObject)

    fun Array(init: YAPIONArrayBuilder.() -> Unit) =
        yapionArray.add(initPattern(YAPIONArrayBuilder(), init).yapionArray)

    fun Map(init: YAPIONMapBuilder.() -> Unit) =
        yapionArray.add(initPattern(YAPIONMapBuilder(), init).yapionMap)

    fun <@YAPIONPrimitive T> Value(value: T?) {
        yapionArray.add(YAPIONValue(value))
    }

    fun <L : YAPIONDataType<*, *>> Pointer(pointer: Builder<L>) {
        yapionArray.add(YAPIONPointer(pointer.yapionDataType))
    }

    fun Pointer(value: YAPIONDataType<*, *>) {
        yapionArray.add(YAPIONPointer(value))
    }

    operator fun IntRange.times(element: YAPIONArrayBuilder.(Int) -> Unit) {
        this.forEach {
            this@YAPIONArrayBuilder.element(it)
        }
    }

    operator fun Int.times(element: YAPIONArrayBuilder.(Int) -> Unit) =
        (0 until this) * element

    fun Objects(value: YAPIONObjectBuilder.(Int) -> Unit): Builder<YAPIONArray>.(Int) -> Unit {
        return {
            val result = initPattern(YAPIONObjectBuilder()) {
                this.value(it)
            }
            this@YAPIONArrayBuilder.yapionArray.add(result.yapionDataType)
        }
    }

    fun Arrays(value: YAPIONArrayBuilder.(Int) -> Unit): Builder<YAPIONArray>.(Int) -> Unit {
        return {
            val result = initPattern(YAPIONArrayBuilder()) {
                this.value(it)
            }
            this@YAPIONArrayBuilder.yapionArray.add(result.yapionDataType)
        }
    }

    fun Maps(value: YAPIONMapBuilder.(Int) -> Unit): Builder<YAPIONArray>.(Int) -> Unit {
        return {
            val result = initPattern(YAPIONMapBuilder()) {
                this.value(it)
            }
            this@YAPIONArrayBuilder.yapionArray.add(result.yapionDataType)
        }
    }

    fun <V> Values(value: (Int) -> V): YAPIONArrayBuilder.(Int) -> Unit {
        return {
            Value(value(it))
        }
    }
}

open class YAPIONMapBuilder : YAPIONBuilder<YAPIONAnyType, YAPIONMap> {
    internal val yapionMap = YAPIONMap()

    override val yapionDataType: YAPIONMap
        get() = yapionMap

    override fun add(key: YAPIONAnyType, value: YAPIONAnyType) {
        yapionMap.add(key, value)
    }

    fun Object(init: YAPIONObjectBuilder.() -> Unit) =
        initPattern(YAPIONObjectBuilder(), init).yapionObject

    fun Array(init: YAPIONArrayBuilder.() -> Unit) =
        initPattern(YAPIONArrayBuilder(), init).yapionArray

    fun Map(init: YAPIONMapBuilder.() -> Unit) =
        initPattern(YAPIONMapBuilder(), init).yapionMap

    fun <@YAPIONPrimitive T> Value(value: T?) =
        YAPIONValue(value)

    fun <L : YAPIONDataType<*, *>> Pointer(pointer: Builder<L>) =
        YAPIONPointer(pointer.yapionDataType)

    fun Pointer(value: YAPIONDataType<*, *>) =
        YAPIONPointer(value)

    @JvmName("with1")
    infix fun <@YAPIONPrimitive T> T.with(value: T) {
        add(YAPIONValue(this), YAPIONValue(value))
    }
}

fun <P> initPattern(p: P, init: P.() -> Unit): P {
    p.init()
    return p
}

fun yapionObject(init: YAPIONObjectBuilder.() -> Unit): YAPIONObject =
    initPattern(YAPIONObjectBuilder(), init).yapionObject

fun yapionArray(init: YAPIONArrayBuilder.() -> Unit): YAPIONArray =
    initPattern(YAPIONArrayBuilder(), init).yapionArray

fun yapionMap(init: YAPIONMapBuilder.() -> Unit): YAPIONMap =
    initPattern(YAPIONMapBuilder(), init).yapionMap

fun <@YAPIONPrimitive T> yapionValue(value: T?): YAPIONValue<T> {
    return YAPIONValue(value)
}

fun yapionPointer(value: YAPIONDataType<*, *>): YAPIONPointer {
    return YAPIONPointer(value)
}