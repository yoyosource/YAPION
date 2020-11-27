// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.utils.YAPIONArrayIndexOutOfBoundsException;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typegroups.YAPIONDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONArray extends YAPIONDataType {

    private final List<YAPIONAnyType> array = new ArrayList<>();

    @Override
    public YAPIONType getType() {
        return YAPIONType.ARRAY;
    }

    @Override
    public long referenceValue() {
        if (hasReferenceValue()) {
            return getReferenceValue();
        }
        long referenceValue = 0;
        referenceValue += getDepth();
        referenceValue ^= getType().getReferenceValue();
        for (int i = 0; i < array.size(); i++) {
            referenceValue += i;
            referenceValue ^= (array.get(i).referenceValue()) & 0x7FFFFFFFFFFFFFFFL;
        }
        cacheReferenceValue(referenceValue);
        return referenceValue;
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

    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        abstractOutput.consume("[");

        final String indent = "\n" + indent();
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
            abstractOutput.consumePrettified(",\n").consumePrettified(reducedIndent());
        }

        abstractOutput.consume("]");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        abstractOutput.consume("[");
        boolean b = false;
        for (YAPIONAnyType yapionAnyType : array) {
            if (b) abstractOutput.consume(",");
            yapionAnyType.toJSON(abstractOutput);
            b = true;
        }
        abstractOutput.consume("]");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
        abstractOutput.consume("[");
        boolean b = false;
        for (YAPIONAnyType yapionAnyType : array) {
            if (b) abstractOutput.consume(",");
            yapionAnyType.toJSONLossy(abstractOutput);
            b = true;
        }
        abstractOutput.consume("]");
        return abstractOutput;
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

    public YAPIONAnyType get(int index) {
        if (index < 0 || index >= length()) {
            throw new YAPIONArrayIndexOutOfBoundsException("Index " + index + " out of bounds for length " + length());
        }
        return array.get(index);
    }

    public YAPIONArray add(YAPIONAnyType yapionAnyType) {
        discardReferenceValue();
        array.add(yapionAnyType);
        if (yapionAnyType != null) {
            yapionAnyType.setParent(this);
        }
        return this;
    }

    public YAPIONArray set(int index, YAPIONAnyType yapionAnyType) {
        discardReferenceValue();
        if (index < 0 || index >= length()) {
            return this;
        }
        array.set(index, yapionAnyType);
        return this;
    }
    
    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(String key) {
        try {
            return Optional.of(new YAPIONSearchResult<>(get(Integer.parseInt(key))));
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
        return array.equals(that.array);
    }

    @Override
    public int hashCode() {
        return Objects.hash(array);
    }
}