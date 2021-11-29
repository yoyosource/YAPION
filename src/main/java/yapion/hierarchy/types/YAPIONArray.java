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

package yapion.hierarchy.types;

import lombok.NonNull;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.utils.YAPIONArrayIndexOutOfBoundsException;
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.api.groups.SerializingType;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.storage.ArrayAdd;
import yapion.hierarchy.api.storage.ArrayRemove;
import yapion.hierarchy.api.storage.ArrayRetrieve;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.utils.RecursionUtils;
import yapion.utils.ReferenceFunction;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class YAPIONArray extends YAPIONDataType<YAPIONArray, Integer> implements ArrayAdd<YAPIONArray, Integer>, ArrayRemove<YAPIONArray, Integer>, ArrayRetrieve<Integer>, SerializingType {

    private final List<YAPIONAnyType> array = new ArrayList<>();

    @Override
    public YAPIONType getType() {
        return YAPIONType.ARRAY;
    }

    @Override
    protected long referenceValueProvider(ReferenceFunction referenceFunction) {
        ReferenceValue referenceValue = new ReferenceValue();
        referenceValue.increment(getDepth());
        referenceValue.update(getType().getReferenceValue());
        for (int i = 0; i < array.size(); i++) {
            referenceValue.increment(i);
            referenceValue.update(array.get(i).referenceValue(referenceFunction));
        }
        return referenceValue.referenceValue;
    }

    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        abstractOutput.consume("[");

        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + 1);
        boolean b = false;
        for (YAPIONAnyType yapionAnyType : array) {
            if (b) abstractOutput.consume(",");
            outputComments(abstractOutput, yapionAnyType.getComments(), indent);

            abstractOutput.consumePrettified(indent);
            if (yapionAnyType == null) {
                abstractOutput.consume("null");
            } else if (yapionAnyType instanceof YAPIONValue yapionValue) {
                yapionValue.toStrippedYAPION(abstractOutput);
            } else {
                yapionAnyType.toYAPION(abstractOutput);
            }

            b = true;
        }

        if (!array.isEmpty() || hasEndingComments()) {
            if (array.get(array.size() - 1) instanceof YAPIONValue) {
                abstractOutput.consumePrettified(",");
            }
            outputComments(abstractOutput, getEndingComments(), indent);
            abstractOutput.consumePrettified("\n").consumeIndent(getDepth());
        }

        abstractOutput.consume("]");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        abstractOutput.consume("[");

        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + 1);
        boolean b = false;
        for (YAPIONAnyType yapionAnyType : array) {
            if (b) abstractOutput.consume(",");
            abstractOutput.consumePrettified(indent);
            yapionAnyType.toJSON(abstractOutput);
            b = true;
        }

        if (!array.isEmpty()) {
            abstractOutput.consumePrettified("\n").consumeIndent(getDepth());
        }

        abstractOutput.consume("]");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
        abstractOutput.consume("[");

        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + 1);
        boolean b = false;
        for (YAPIONAnyType yapionAnyType : array) {
            if (b) abstractOutput.consume(",");
            abstractOutput.consumePrettified(indent);
            yapionAnyType.toJSONLossy(abstractOutput);
            b = true;
        }

        if (!array.isEmpty()) {
            abstractOutput.consumePrettified("\n").consumeIndent(getDepth());
        }

        abstractOutput.consume("]");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toThunderFile(T abstractOutput) {
        abstractOutput.consume("[");

        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth());
        for (YAPIONAnyType yapionAnyType : array) {
            abstractOutput.consume(indent).consume("- ");
            yapionAnyType.toThunderFile(abstractOutput);
        }

        if (!array.isEmpty()) {
            abstractOutput.consume("\n").consumeIndent(getDepth() - 1);
        }

        abstractOutput.consume("]");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toXML(T abstractOutput) {
        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + 1);
        for (YAPIONAnyType yapionAnyType : array) {
            abstractOutput.consumePrettified(indent);
            abstractOutput.consume("<element>");
            yapionAnyType.toXML(abstractOutput);
            abstractOutput.consume("</element>");
        }

        if (!array.isEmpty()) {
            abstractOutput.consumePrettified("\n").consumeIndent(getDepth());
        }
        return abstractOutput;
    }

    @Override
    public List<Object> unwrap() {
        List<Object> result = new ArrayList<>();
        for (YAPIONAnyType yapionAnyType : array) {
            result.add(yapionAnyType.unwrap());
        }
        return result;
    }

    @Override
    public String getPath(YAPIONAnyType yapionAnyType) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == yapionAnyType) {
                return i + "";
            }
        }
        return "";
    }

    @Override
    public boolean internalContainsKey(@NonNull Integer key, YAPIONType yapionType) {
        try {
            YAPIONAnyType yapionAnyType = getAnyType(key);
            if (yapionAnyType == null) return false;
            if (yapionType == YAPIONType.ANY) return true;
            return yapionType == yapionAnyType.getType();
        } catch (YAPIONArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public <T> boolean internalContainsKey(@NonNull Integer key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return false;
        }
        try {
            YAPIONAnyType yapionAnyType = getAnyType(key);
            if (yapionAnyType == null) return false;
            if (yapionAnyType instanceof YAPIONValue<?> yapionValue) {
                return yapionValue.isValidCastType(type);
            }
            return false;
        } catch (YAPIONArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean internalContainsValue(@NonNull YAPIONAnyType yapionAnyType) {
        return array.contains(yapionAnyType);
    }

    public YAPIONAnyType internalGetAnyType(@NonNull Integer key) {
        checkIndex(key);
        return array.get(key);
    }

    private void check(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType.getType() == YAPIONType.VALUE || yapionAnyType.getType() == YAPIONType.POINTER) {
            return;
        }
        RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(yapionAnyType, this);
        if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
            if (result.getRecursionType() == RecursionUtils.RecursionType.DIRECT) {
                throw new YAPIONRecursionException("Direct Recursion failure. Use a pointer instead.");
            } else {
                throw new YAPIONRecursionException("Back Reference failure. Use a pointer instead.");
            }
        }
    }

    private void checkIndex(Integer index) {
        if (index < 0 || index >= length()) {
            throw new YAPIONArrayIndexOutOfBoundsException("Index " + index + " out of bounds for length " + length());
        }
    }

    public YAPIONArray internalAdd(@NonNull Integer key, @NonNull YAPIONAnyType value) {
        checkIndex(key);
        check(value);
        discardReferenceValue();
        array.get(key).removeParent();
        array.add(key, value);
        value.setParent(this);
        return this;
    }

    @Override
    public YAPIONAnyType internalAddAndGetPrevious(@NonNull Integer key, @NonNull YAPIONAnyType value) {
        checkIndex(key);
        check(value);
        discardReferenceValue();
        YAPIONAnyType previous = array.get(key);
        previous.removeParent();
        array.add(key, value);
        value.setParent(this);
        return previous;
    }

    public YAPIONArray set(@NonNull Integer key, @NonNull YAPIONAnyType value) {
        checkIndex(key);
        check(value);
        discardReferenceValue();
        array.get(key).removeParent();
        array.set(key, value);
        value.setParent(this);
        return this;
    }

    public YAPIONArray add(@NonNull YAPIONAnyType value) {
        discardReferenceValue();
        array.add(value);
        value.setParent(this);
        return this;
    }

    @Override
    public YAPIONArray addOrPointer(@NonNull Integer key, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        if (value.getType() != YAPIONType.VALUE && value.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(value, this);
            if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (result.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                add(key, new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
                return this;
            }
        }
        add(key, value);
        return this;
    }

    @Override
    public YAPIONAnyType addOrPointerAndGetPrevious(@NonNull Integer key, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        if (value.getType() != YAPIONType.VALUE && value.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(value, this);
            if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (result.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                return addAndGetPrevious(key, new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
            }
        }
        return addAndGetPrevious(key, value);
    }

    @Override
    public YAPIONArray addOrPointer(@NonNull YAPIONAnyType value) {
        discardReferenceValue();
        if (value.getType() != YAPIONType.VALUE && value.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(value, this);
            if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (result.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                add(new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
                return this;
            }
        }
        add(value);
        return this;
    }

    @Override
    public YAPIONArray setOrPointer(@NonNull Integer key, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        if (value.getType() != YAPIONType.VALUE && value.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(value, this);
            if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (result.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                set(key, new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
                return this;
            }
        }
        set(key, value);
        return this;
    }

    @Override
    public YAPIONArray internalRemove(@NonNull Integer key) {
        checkIndex(key);
        discardReferenceValue();
        array.get(key).removeParent();
        array.remove((int) key);
        return this;
    }

    @Override
    public YAPIONAnyType internalRemoveAndGet(@NonNull Integer key) {
        checkIndex(key);
        discardReferenceValue();
        YAPIONAnyType yapionAnyType = array.get(key);
        array.remove((int) key);
        yapionAnyType.removeParent();
        return yapionAnyType;
    }

    @Override
    public YAPIONArray internalClear() {
        discardReferenceValue();
        array.forEach(YAPIONAnyType::removeParent);
        array.clear();
        return this;
    }

    @Override
    public <T> int indexOf(T element) {
        if (YAPIONValue.validType(element)) {
            return indexOf(new YAPIONValue<>(element));
        } else if (!(element instanceof YAPIONAnyType)) {
            throw new YAPIONException("element is not from valid type");
        }
        return array.indexOf(element);
    }

    @Override
    public <T> int lastIndexOf(T element) {
        if (YAPIONValue.validType(element)) {
            return lastIndexOf(new YAPIONValue<>(element));
        } else if (!(element instanceof YAPIONAnyType)) {
            throw new YAPIONException("element is not from valid type");
        }
        return array.lastIndexOf(element);
    }

    @Override
    public YAPIONArray itself() {
        return this;
    }

    @Override
    public Iterator<YAPIONAnyType> iterator() {
        return array.iterator();
    }

    @Override
    public Set<Integer> allKeys() {
        Set<Integer> allKeys = new HashSet<>();
        for (int i = 0; i < size(); i++) allKeys.add(i);
        return allKeys;
    }

    @Override
    public <T extends YAPIONAnyType> T addIfAbsent(@NonNull Integer key, @NonNull T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends YAPIONAnyType> T addIfAbsent(@NonNull Integer key, @NonNull YAPIONType yapionType, @NonNull T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> YAPIONValue<T> addIfAbsent(@NonNull Integer key, @NonNull Class<T> type, @NonNull T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends YAPIONAnyType> T computeIfAbsent(@NonNull Integer key, @NonNull Function<Integer, T> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends YAPIONAnyType> T compute(@NonNull Integer key, @NonNull BiFunction<Integer, T, T> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public long deepSize() {
        return size() + stream().filter(YAPIONDataType.class::isInstance)
                .map(YAPIONDataType.class::cast)
                .map(YAPIONDataType::deepSize)
                .mapToLong(value -> value)
                .sum();
    }

    @Override
    public List<YAPIONAnyType> getAllValues() {
        return array;
    }

    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(@NonNull String key) {
        try {
            return Optional.of(new YAPIONSearchResult<>(getAnyType(Integer.parseInt(key))));
        } catch (NumberFormatException | YAPIONArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    /**
     * Modifying this is an unsafe operation. When you edit this you are on your own!
     *
     * @return the internal backed array
     */
    @InternalAPI
    public List<YAPIONAnyType> getBackedArray() {
        return array;
    }

    @Override
    public String toString() {
        StringOutput stringOutput = new StringOutput();
        toYAPION(stringOutput);
        return stringOutput.getResult();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YAPIONArray)) return false;
        YAPIONArray that = (YAPIONArray) o;
        if (size() != that.size()) return false;
        for (int i = 0; i < size(); i++) {
            if (!array.get(i).equals(that.array.get(i))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (int) referenceValue();
    }

    public YAPIONArray copy() {
        return (YAPIONArray) internalCopy();
    }
}
