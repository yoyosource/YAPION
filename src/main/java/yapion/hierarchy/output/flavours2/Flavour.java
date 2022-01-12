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

package yapion.hierarchy.output.flavours2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.types.YAPIONValue;

import java.util.HashSet;
import java.util.Set;

public interface Flavour {

    enum PrettifyBehaviour {
        CHOOSEABLE,
        ALWAYS,
        NEVER
    }

    enum HierarchyTypes {
        OBJECT,
        ARRAY,
        MAP,
        VALUE,
        POINTER,
        COMMENT
    }

    default Set<HierarchyTypes> unsupportedTypes() {
        return new HashSet<>();
    }

    default void checkType(HierarchyTypes hierarchyTypes) {
        if (unsupportedTypes().contains(hierarchyTypes)) {
            throw new UnsupportedOperationException("Flavour " + this.getClass().getSimpleName() + " does not support " + hierarchyTypes.name());
        }
    }

    /**
     * @return how prettify should be handled
     */
    default PrettifyBehaviour getPrettifyBehaviour() {
        return PrettifyBehaviour.CHOOSEABLE;
    }

    /**
     * @return if the root value should be removed from the output
     */
    default boolean removeRootObject() {
        return false;
    }

    /**
     * Write an optional header if wanted.
     *
     * @param output the output to write to
     */
    default void header(AbstractOutput output) {
    }

    /**
     * Write an optional footer if wanted.
     *
     * @param output the output to write to
     */
    default void footer(AbstractOutput output) {
    }

    /**
     * Begin a new {@link HierarchyTypes}
     *
     * @param hierarchyTypes the new {@link HierarchyTypes} to begin
     * @param output the output to write to
     */
    default void begin(HierarchyTypes hierarchyTypes, AbstractOutput output) {
    }

    /**
     * End a {@link HierarchyTypes}
     *
     * @param hierarchyTypes the {@link HierarchyTypes} to end
     * @param output the output to write to
     */
    default void end(HierarchyTypes hierarchyTypes, AbstractOutput output) {
    }

    /**
     * Begin an Element inside a {@link HierarchyTypes}
     *
     * @param hierarchyTypes the {@link HierarchyTypes} the element is inside
     * @param output the output to write to
     * @param name the name of the element
     */
    default void beginElement(HierarchyTypes hierarchyTypes, AbstractOutput output, String name) {
    }

    /**
     * End an Element inside a {@link HierarchyTypes}
     *
     * @param hierarchyTypes the {@link HierarchyTypes} the element is inside
     * @param output the output to write to
     * @param name the name of the element
     */
    default void endElement(HierarchyTypes hierarchyTypes, AbstractOutput output, String name) {
    }

    /**
     * Separate an Element inside a {@link HierarchyTypes}
     *
     * @param hierarchyTypes the {@link HierarchyTypes} the element is inside
     * @param output the output to write to
     */
    default void elementSeparator(HierarchyTypes hierarchyTypes, AbstractOutput output) {
    }

    /**
     * @param hierarchyTypes can be either {@link HierarchyTypes#VALUE} or {@link HierarchyTypes#ARRAY} if the value is inside an array or {@link HierarchyTypes#COMMENT}
     * @param output the output to write to
     * @param valueData the data to write either as {@link ValueData<?>} or {@link ValueData<String>} the latter is only used with the {@link HierarchyTypes#COMMENT} and does not have the {@link ValueData#wrappedValue} set.
     */
    default <T> void elementValue(HierarchyTypes hierarchyTypes, AbstractOutput output, ValueData<T> valueData) {
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    class ValueData<T> {
        private YAPIONValue<T> wrappedValue;
        private final T value;
    }
}
