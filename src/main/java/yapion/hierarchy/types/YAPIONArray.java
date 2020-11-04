// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.utils.YAPIONArrayIndexOutOfBoundsException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typegroups.YAPIONDataType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public String toYAPIONString() {
        StringBuilder st = new StringBuilder();
        st.append("[");
        boolean b = false;
        for (YAPIONAnyType yapionAnyType : array) {
            if (b) {
                st.append(",");
            }
            if (yapionAnyType == null) {
                st.append("null");
            } else {
                String s = yapionAnyType.toYAPIONString();
                if (yapionAnyType.getType() == YAPIONType.VALUE) {
                    st.append(s, 1, s.length() - 1);
                } else {
                    st.append(s);
                }
            }
            b = true;
        }
        st.append("]");
        return st.toString();
    }

    @Override
    public String toYAPIONStringPrettified() {
        final String indent = "\n" + indent();
        StringBuilder st = new StringBuilder();
        st.append("[");
        boolean b = false;
        for (YAPIONAnyType yapionAnyType : array) {
            if (b) {
                st.append(",");
            }
            st.append(indent);
            if (yapionAnyType == null) {
                st.append("null");
            } else {
                String s = yapionAnyType.toYAPIONStringPrettified();
                if (yapionAnyType.getType() == YAPIONType.VALUE) {
                    st.append(s, 1, s.length() - 1);
                } else {
                    st.append(s);
                }
            }
            b = true;
        }
        if (!array.isEmpty()) {
            st.append(",\n").append(reducedIndent());
        }
        st.append("]");
        return st.toString();
    }

    @Override
    public String toJSONString() {
        return "[" + array.stream().map(YAPIONAnyType::toJSONString).collect(Collectors.joining(",")) + "]";
    }

    @Override
    public String toLossyJSONString() {
        return "[" + array.stream().map(YAPIONAnyType::toLossyJSONString).collect(Collectors.joining(",")) + "]";
    }

    @Override
    public void toOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write(bytes("["));
        for (int i = 0; i < array.size(); i++) {
            if (i != 0) {
                outputStream.write(bytes(","));
            }
            YAPIONAnyType yapionAnyType = array.get(i);
            if (yapionAnyType instanceof YAPIONValue) {
                String s = yapionAnyType.toYAPIONString();
                outputStream.write(s.substring(1, s.length() - 1).getBytes(StandardCharsets.UTF_8));
            } else {
                array.get(i).toOutputStream(outputStream);
            }
        }
        outputStream.write(bytes("]"));
    }

    @Override
    public void toOutputStreamPrettified(OutputStream outputStream) throws IOException {
        final byte[] indent = bytes("\n" + indent());
        outputStream.write(bytes("["));
        for (int i = 0; i < array.size(); i++) {
            if (i != 0) {
                outputStream.write(",".getBytes(StandardCharsets.UTF_8));
            }
            outputStream.write(indent);
            YAPIONAnyType yapionAnyType = array.get(i);
            if (yapionAnyType instanceof YAPIONValue) {
                String s = yapionAnyType.toYAPIONString();
                outputStream.write(bytes(s.substring(1, s.length() - 1)));
            } else {
                array.get(i).toOutputStream(outputStream);
            }
        }
        if (!array.isEmpty()) {
            outputStream.write(bytes("\n" + reducedIndent()));
        }
        outputStream.write(bytes("]"));
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
        return toYAPIONString();
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