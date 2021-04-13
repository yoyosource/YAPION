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
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.utils.YAPIONArrayIndexOutOfBoundsException;
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.storage.ArrayAdd;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.utils.RecursionUtils;
import yapion.utils.ReferenceFunction;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONArray extends YAPIONDataType<YAPIONArray, Integer> implements ArrayAdd<YAPIONArray, Integer> {

    private final List<YAPIONAnyType> array = new ArrayList<>();

    @Override
    public YAPIONType getType() {
        return YAPIONType.ARRAY;
    }

    @Override
    protected long referenceValueProvider(ReferenceFunction referenceFunction) {
        long referenceValue = 0;
        referenceValue += getDepth();
        referenceValue ^= getType().getReferenceValue();
        for (int i = 0; i < array.size(); i++) {
            referenceValue += i;
            referenceValue ^= array.get(i).referenceValue(referenceFunction) & 0x7FFFFFFFFFFFFFFFL;
        }
        return referenceValue;
    }

    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        abstractOutput.consume("[");

        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + 1);
        boolean b = false;
        for (YAPIONAnyType yapionAnyType : array) {
            if (b) abstractOutput.consume(",");
            abstractOutput.consumePrettified(indent);

            if (yapionAnyType == null) {
                abstractOutput.consume("null");
            } else if (yapionAnyType instanceof YAPIONValue) {
                ((YAPIONValue) yapionAnyType).toStrippedYAPION(abstractOutput);
            } else {
                yapionAnyType.toYAPION(abstractOutput);
            }

            b = true;
        }

        if (!array.isEmpty()) {
            if (array.get(array.size() - 1) instanceof YAPIONValue) {
                abstractOutput.consumePrettified(",");
            }
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
    public String getPath(YAPIONAnyType yapionAnyType) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == yapionAnyType) {
                return i + "";
            }
        }
        return "";
    }

    @Override
    public boolean hasValue(@NonNull Integer key, YAPIONType yapionType) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (yapionType == YAPIONType.ANY) return true;
        return yapionType == yapionAnyType.getType();
    }

    @Override
    public <T> boolean hasValue(@NonNull Integer key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return false;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (!(yapionAnyType instanceof YAPIONValue)) return false;
        return ((YAPIONValue) yapionAnyType).isValidCastType(type.getTypeName());
    }

    public YAPIONAnyType getYAPIONAnyType(@NonNull Integer key) {
        checkIndex(key);
        return array.get(key);
    }

    private void check(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType instanceof YAPIONValue || yapionAnyType instanceof YAPIONPointer) {
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

    public YAPIONArray add(@NonNull Integer key, @NonNull YAPIONAnyType value) {
        checkIndex(key);
        check(value);
        discardReferenceValue();
        array.add(key, value);
        value.setParent(this);
        return this;
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
        RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(value, this);
        if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
            if (result.getYAPIONAny() == null) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            if (!(result.getYAPIONAny() instanceof YAPIONObject)) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            add(key, new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
            return this;
        }
        add(key, value);
        return this;
    }

    @Override
    public YAPIONArray addOrPointer(@NonNull YAPIONAnyType value) {
        discardReferenceValue();
        RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(value, this);
        if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
            if (result.getYAPIONAny() == null) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            if (!(result.getYAPIONAny() instanceof YAPIONObject)) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            add(new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
            return this;
        }
        add(value);
        return this;
    }

    @Override
    public YAPIONArray setOrPointer(@NonNull Integer key, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(value, this);
        if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
            if (result.getYAPIONAny() == null) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            if (!(result.getYAPIONAny() instanceof YAPIONObject)) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            set(key, new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
            return this;
        }
        set(key, value);
        return this;
    }

    @Override
    public YAPIONArray remove(@NonNull Integer key) {
        checkIndex(key);
        discardReferenceValue();
        array.get(key).removeParent();
        array.remove((int) key);
        return this;
    }

    @Override
    public YAPIONAnyType removeAndGet(@NonNull Integer key) {
        checkIndex(key);
        discardReferenceValue();
        YAPIONAnyType yapionAnyType = array.get(key);
        array.remove((int) key);
        yapionAnyType.removeParent();
        return yapionAnyType;
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
    public YAPIONArray addIfAbsent(@NonNull Integer key, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public YAPIONArray addIfAbsent(@NonNull Integer key, @NonNull YAPIONType yapionType, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> YAPIONArray addIfAbsent(@NonNull Integer key, @NonNull Class<T> type, @NonNull YAPIONAnyType value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends YAPIONAnyType> YAPIONArray computeIfAbsent(@NonNull Integer key, @NonNull Function<Integer, T> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends YAPIONAnyType> YAPIONArray compute(@NonNull Integer key, @NonNull BiFunction<Integer, T, T> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public long deepSize() {
        long size = size();
        for (YAPIONAnyType yapionAnyType : array) {
            if (yapionAnyType instanceof YAPIONDataType) size += ((YAPIONDataType) yapionAnyType).deepSize();
        }
        return size;
    }

    @Override
    public int length() {
        return array.size();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    @Override
    public List<YAPIONAnyType> getAllValues() {
        return array;
    }
    
    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(@NonNull String key) {
        try {
            return Optional.of(new YAPIONSearchResult<>(getYAPIONAnyType(Integer.parseInt(key))));
        } catch (NumberFormatException | YAPIONArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
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
}
