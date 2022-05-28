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

package yapion.path

import yapion.hierarchy.api.groups.YAPIONAnyType
import yapion.hierarchy.types.YAPIONArray
import yapion.hierarchy.types.YAPIONObject
import yapion.hierarchy.types.YAPIONValue
import yapion.hierarchy.yapionObject
import yapion.path.impl.*
import yapion.path.impl.array.Range
import yapion.path.impl.`object`.Contains
import yapion.path.impl.`object`.EndsWith
import yapion.path.impl.`object`.StartsWith
import yapion.path.impl.value.Value
import yapion.path.impl.value.ValueAnd
import yapion.path.impl.value.ValueElement
import yapion.path.impl.value.ValueOr
import yapion.path.impl.value.impl.*

@DslMarker
annotation class YAPIONPathTagMarker

@YAPIONPathTagMarker
abstract class BuilderPathElement<T : PathElement> {
    internal val pathElements: MutableList<T> = mutableListOf()

    protected fun using(pathElement: T) {
        pathElements.add(pathElement)
    }

    protected fun <V : PathElement, E : BuilderPathElement<V>, R : PathElement> initElement(tag: E, init: E.() -> Unit, resultCreator: (List<V>) -> R): R {
        tag.init()
        return resultCreator(tag.pathElements)
    }
}

abstract class SelectorElementBuilder : BuilderPathElement<PathElement>() {

    fun select(s: String) =
        using(Element(s))

    fun select(s: Int) =
        using(Element(s))

    fun contains(s: String) =
        using(Contains(s))

    fun endsWith(s: String) =
        using(EndsWith(s))

    fun startsWith(s: String) =
        using(StartsWith(s))

    fun regex(s: String) =
        using(yapion.path.impl.`object`.Regex(s))

    fun min(min: Int) =
        using(Range.min(min))

    fun max(max: Int) =
        using(Range.max(max))

    fun range(min: Int, max: Int) =
        using(Range.range(min, max))

    fun each() =
        using(AnyElement())

    fun any() =
        using(AnyElement())

    fun eachDeeper() =
        using(AnyDeeperElement())

    fun anyDeeper() =
        using(AnyDeeperElement())

    fun distinct() =
        using(DistinctElements())

    fun identityDistinct() =
        using(IdentityDistinctElements())

    fun <T : YAPIONAnyType> type(type: Class<T>) =
        using(ElementType(type))

    fun objects() =
        type(YAPIONObject::class.java)

    fun arrays() =
        type(YAPIONArray::class.java)

    fun values() =
        type(YAPIONValue::class.java)

    fun root() =
        using(RootElement())

    fun current() =
        using(CurrentElement())

    fun streamEntries() =
        using(Spread())

    fun spread() =
        using(Spread())

    fun flattenEntries() =
        using(Flatten())

    fun flatten() =
        using(Flatten())

    fun anyOf(init: AnyOf.() -> Unit) =
        initElement(AnyOf(), init) { elements -> AnyOf(elements) }

    fun allOf(init: AllOf.() -> Unit) =
        initElement(AllOf(), init) { elements -> AllOf(elements) }

    fun anyWith(init: AnyWith.() -> Unit) =
        initElement(AnyWith(), init) { elements -> AnyWith(elements) }

    fun allWith(init: AllWith.() -> Unit) =
        initElement(AllWith(), init) { elements -> AllWith(elements) }

    fun whenTest(init: When.() -> Unit) =
        initElement(When(), init) { elements -> When(YAPIONPath(elements)) }

    fun subSelect(init: SubSelector.() -> Unit) =
        initElement(SubSelector(), init) { elements -> YAPIONPath(elements) }

    fun keySelect(init: KeySelector.() -> Unit) =
        initElement(KeySelector(), init) { elements -> KeySelection(ValueAnd(elements)) }

    fun value(init: ValueSelector.() -> Unit) =
        initElement(ValueSelector(), init) { elements -> Value(ValueAnd(elements)) }
}

abstract class ValueElementBuilder : BuilderPathElement<ValueElement>() {

    fun <T> equalTo(value: T) =
        using(ValueEquals(value))

    fun <T> min(min: T) where T : Number, T : Comparable<T> =
        using(ValueRange.min(min))

    fun <T> max(max: T) where T : Number, T : Comparable<T> =
        using(ValueRange.max(max))

    fun <T> range(min: T, max: T) where T : Number, T : Comparable<T> =
        using(ValueRange.range(min, max))

    fun contains(s: String) =
        using(ValueContains(s))

    fun startsWith(s: String) =
        using(ValueStartsWith(s))

    fun endsWith(s: String) =
        using(ValueEndsWith(s))

    fun regex(s: String) =
        using(ValueRegex(s))

    fun and(init: AndValueSelector.() -> Unit) =
        initElement(AndValueSelector(), init) { elements -> ValueAnd(elements) }

    fun or(init: OrValueSelector.() -> Unit) =
        initElement(OrValueSelector(), init) { elements -> ValueOr(elements) }

    fun set(init: SetValueSelector.() -> Unit) =
        initElement(SetValueSelector(), init) { elements -> ValueSet(elements) }
}

class Selector : SelectorElementBuilder()
class AnyOf : SelectorElementBuilder()
class AllOf : SelectorElementBuilder()
class AnyWith : SelectorElementBuilder()
class AllWith : SelectorElementBuilder()
class When : SelectorElementBuilder()
class SubSelector : SelectorElementBuilder()
class KeySelector : ValueElementBuilder()
class ValueSelector : ValueElementBuilder()
class AndValueSelector : ValueElementBuilder()
class OrValueSelector : ValueElementBuilder()
class SetValueSelector : ValueElementBuilder()

fun selector(init: Selector.() -> Unit): YAPIONPath {
    val selector = Selector()
    selector.init()
    return YAPIONPath(selector.pathElements)
}

operator fun YAPIONAnyType.get(path: YAPIONPath): PathResult {
    return path.apply(this)
}

fun main() {
    if (true) {
        val yapionObject = yapionObject {
            "element" with 0
            for (i in 0 until 100) {
                "test$i" Array {
                    100 * { Value(it) }
                }
            }
        }
        yapionObject.put("element", 1)
        yapionObject.remove("element")
        println(yapionObject)
    }

    if (true) {
        val yapionObject = yapionObject {
            "element" with 0
            100 * {
                "test$it" Array {
                    100 * { Value(it) }
                }
            }
        }
        yapionObject.put("element", 1)
        yapionObject.remove("element")
        println(yapionObject)
    }

    if (true) {
        val yapionObject = yapionObject {
            "element" with 0
            100 * {
                "test$it" Array {
                    100 * Values { it }
                }
            }
        }
        yapionObject.put("element", 1)
        yapionObject.remove("element")
        println(yapionObject)
    }

    if (true) {
        val yapionObject = yapionObject {
            "element" with 0
            100 * Arrays {
                100 * Values { it }
                "test$it"
            }
        }
        yapionObject.put("element", 1)
        yapionObject.remove("element")
        println(yapionObject)
    }

    val yapionObject = YAPIONObject()
    yapionObject.add("element", 0)
    for (i in 0..99) {
        val yapionArray = YAPIONArray()
        for (j in 0..99) {
            yapionArray.add(YAPIONValue(j))
        }
        yapionObject.add("test$i", yapionArray)
    }
    yapionObject.put("element", 1)
    yapionObject.remove("element")

    if (true) {
        val path = selector {
            any()
            keySelect {
                contains("0")
            }
        }
        output(yapionObject[path])
    }
}

private fun output(result: PathResult) {
    println(result.size)
    println((result.nanoTime / 10000 / 100.0).toString() + "ms")
    val st = StringBuilder()
    for ((key, value) in result.timingMap) {
        if (st.length != 0) {
            st.append(", ")
        }
        st.append(key.toString()).append("=").append(value / 1000000.0).append("ms")
    }
    println(st)
    println(result.yapionAnyTypeList)
}