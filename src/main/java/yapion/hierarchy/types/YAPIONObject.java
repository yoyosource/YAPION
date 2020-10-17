// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typegroups.YAPIONDataType;
import yapion.hierarchy.typegroups.YAPIONMappingType;
import yapion.utils.RecursionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONObject extends YAPIONMappingType {

    private final List<YAPIONVariable> variables = new ArrayList<>();

    @Override
    public YAPIONType getType() {
        return YAPIONType.OBJECT;
    }

    @Override
    public long referenceValue() {
        if (hasReferenceValue()) {
            return getReferenceValue();
        }
        long referenceValue = 0;
        referenceValue += getDepth();
        referenceValue ^= getType().getReferenceValue();
        for (YAPIONVariable variable : variables) {
            referenceValue ^= variable.referenceValue() & 0x7FFFFFFFFFFFFFFFL;
        }
        cacheReferenceValue(referenceValue);
        return referenceValue;
    }

    @Override
    public String toYAPIONString() {
        StringBuilder st = new StringBuilder();
        st.append("{");
        for (YAPIONVariable yapionVariable : variables) {
            st.append(yapionVariable.toYAPIONString());
        }
        return st.append("}").toString();
    }

    @Override
    public String toYAPIONStringPrettified() {
        final String indent = indent();
        StringBuilder st = new StringBuilder();
        st.append("{");
        for (YAPIONVariable yapionVariable : variables) {
            st.append("\n").append(indent);
            st.append(yapionVariable.toYAPIONStringPrettified());
        }
        if (!variables.isEmpty()) {
            st.append("\n").append(reducedIndent());
        }
        return st.append("}").toString();
    }

    @Override
    public String toJSONString() {
        return "{" + variables.stream().map(YAPIONVariable::toJSONString).collect(Collectors.joining(",")) + "}";
    }

    @Override
    public String toLossyJSONString() {
        return "{" + variables.stream().map(YAPIONVariable::toLossyJSONString).collect(Collectors.joining(",")) + "}";
    }

    @Override
    public void toOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write(bytes("{"));
        for (YAPIONVariable variable : variables) {
            variable.toOutputStream(outputStream);
        }
        outputStream.write(bytes("}"));
    }

    @Override
    public void toOutputStreamPrettified(OutputStream outputStream) throws IOException {
        final byte[] indent = bytes(indent());
        outputStream.write(bytes("{\n"));
        for (YAPIONVariable variable : variables) {
            outputStream.write(indent);
            variable.toOutputStreamPrettified(outputStream);
        }
        if (!variables.isEmpty()) {
            outputStream.write(bytes("\n" + reducedIndent()));
        }
        outputStream.write(bytes("}"));
    }

    @Override
    public String getPath(YAPIONAnyType yapionAnyType) {
        for (String s : getKeys()) {
            if (getVariable(s).getValue() == yapionAnyType) {
                return s;
            }
        }
        return "";
    }

    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (YAPIONVariable yapionVariable : variables) {
            keys.add(yapionVariable.getName());
        }
        return keys;
    }

    public YAPIONVariable getVariable(String key) {
        for (YAPIONVariable yapionVariable : variables) {
            if (yapionVariable.getName().equals(key)) {
                return yapionVariable;
            }
        }
        return null;
    }

    public YAPIONObject getObject(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAnyType yapionAnyType = yapionVariable.getValue();
        if (yapionAnyType instanceof YAPIONObject) {
            return (YAPIONObject) yapionAnyType;
        }
        return null;
    }

    public YAPIONArray getArray(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAnyType yapionAnyType = yapionVariable.getValue();
        if (yapionAnyType instanceof YAPIONArray) {
            return (YAPIONArray) yapionAnyType;
        }
        return null;
    }

    public YAPIONMap getMap(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAnyType yapionAnyType = yapionVariable.getValue();
        if (yapionAnyType instanceof YAPIONMap) {
            return (YAPIONMap) yapionAnyType;
        }
        return null;
    }

    public YAPIONPointer getPointer(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAnyType yapionAnyType = yapionVariable.getValue();
        if (yapionAnyType instanceof YAPIONPointer) {
            return (YAPIONPointer) yapionAnyType;
        }
        return null;
    }

    @SuppressWarnings({"java:S3740"})
    public YAPIONValue getValue(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAnyType yapionAnyType = yapionVariable.getValue();
        if (yapionAnyType instanceof YAPIONValue) {
            return (YAPIONValue) yapionAnyType;
        }
        return null;
    }

    public <T> YAPIONValue<T> getValue(String key, T type) {
        if (!YAPIONValue.validType(type)) {
            return null;
        }
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAnyType yapionAnyType = yapionVariable.getValue();
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return null;
        }
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getClass().getTypeName())) {
            return null;
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    private void check(YAPIONVariable variable) {
        RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(variable, this);
        if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
            if (result.getRecursionType() == RecursionUtils.RecursionType.DIRECT) {
                throw new YAPIONRecursionException("Direct Recursion failure. Use a pointer instead.");
            } else {
                throw new YAPIONRecursionException("Back Reference failure. Use a pointer instead.");
            }
        }
    }

    public YAPIONObject add(YAPIONVariable variable) {
        check(variable);
        discardReferenceValue();
        for (int i = variables.size() - 1; i >= 0; i--) {
            if (variables.get(i).getName().equals(variable.getName())) {
                variables.remove(i).getValue().removeParent();

                variables.add(i, variable);
                variable.getValue().setParent(this);
                return this;
            }
        }
        variables.add(variable);
        variable.getValue().setParent(this);
        return this;
    }

    public YAPIONObject add(String name, YAPIONAnyType value) {
        return add(new YAPIONVariable(name, value));
    }

    public YAPIONObject addOrPointer(YAPIONVariable variable) {
        discardReferenceValue();
        RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(variable.getValue(), this);
        if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
            if (result.getYAPIONAny() == null) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            if (!(result.getYAPIONAny() instanceof YAPIONObject)) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            variable = new YAPIONVariable(variable.getName(), new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
        }
        return add(variable);
    }

    @Override
    public int size() {
        return variables.size();
    }

    @Override
    public long deepSize() {
        long size = size();
        for (YAPIONVariable yapionVariable : variables) {
            YAPIONAnyType yapionAnyType = yapionVariable.getValue();
            if (yapionAnyType instanceof YAPIONDataType) size += ((YAPIONDataType) yapionAnyType).deepSize();
        }
        return size;
    }

    @Override
    public int length() {
        return variables.size();
    }

    @Override
    public boolean isEmpty() {
        return variables.isEmpty();
    }

    @Override
    public List<YAPIONAnyType> getAllValues() {
        List<YAPIONAnyType> yapionAnyTypes = new ArrayList<>();
        for (YAPIONVariable variable : variables) {
            yapionAnyTypes.add(variable.getValue());
        }
        return yapionAnyTypes;
    }

    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(String key) {
        if (getVariable(key) == null) return Optional.empty();
        return Optional.of(new YAPIONSearchResult<>(getVariable(key).getValue()));
    }

    @Override
    public String toString() {
        return toYAPIONString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YAPIONObject)) return false;
        YAPIONObject that = (YAPIONObject) o;
        return variables.equals(that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables);
    }

}