// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types;

import lombok.NonNull;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typegroups.YAPIONDataType;
import yapion.hierarchy.typegroups.YAPIONMappingType;
import yapion.utils.RecursionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONObject extends YAPIONMappingType {

    // private final Map<String, YAPIONAnyType> variables = new LinkedHashMap<>();
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
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        abstractOutput.consume("{");

        final String indent = "\n" + indent();
        for (YAPIONVariable yapionVariable : variables) {
            abstractOutput.consumePrettified(indent);
            yapionVariable.toYAPION(abstractOutput);
        }
        
        if (!variables.isEmpty()) {
            abstractOutput.consumePrettified("\n").consumePrettified(reducedIndent());
        }
        
        abstractOutput.consume("}");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        abstractOutput.consume("{");

        final String indent = "\n" + indent();
        boolean b = false;
        for (YAPIONVariable yapionVariable : variables) {
            if (b) abstractOutput.consume(",");
            abstractOutput.consumePrettified(indent);
            yapionVariable.toJSON(abstractOutput);
            b = true;
        }

        if (!variables.isEmpty()) {
            abstractOutput.consumePrettified("\n").consumePrettified(reducedIndent());
        }

        abstractOutput.consume("}");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
        abstractOutput.consume("{");

        final String indent = "\n" + indent();
        boolean b = false;
        for (YAPIONVariable yapionVariable : variables) {
            if (b) abstractOutput.consume(",");
            abstractOutput.consumePrettified(indent);
            yapionVariable.toJSONLossy(abstractOutput);
            b = true;
        }

        if (!variables.isEmpty()) {
            abstractOutput.consumePrettified("\n").consumePrettified(reducedIndent());
        }

        abstractOutput.consume("}");
        return abstractOutput;
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

    public boolean hasValue(@NonNull String key) {
        return hasValue(key, YAPIONType.ANY);
    }

    public boolean hasValue(@NonNull String key, YAPIONType yapionType) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) return false;
        if (yapionType == YAPIONType.ANY) return true;
        return yapionType == yapionVariable.getValue().getType();
    }

    public <T> boolean hasYAPIONValue(@NonNull String key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return false;
        }
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) return false;
        YAPIONAnyType yapionAnyType = yapionVariable.getValue();
        if (!(yapionAnyType instanceof YAPIONValue)) return false;
        return ((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getTypeName());
    }

    public YAPIONVariable getVariable(@NonNull String key) {
        for (YAPIONVariable yapionVariable : variables) {
            if (yapionVariable.getName().equals(key)) {
                return yapionVariable;
            }
        }
        return null;
    }

    public YAPIONObject getObject(@NonNull String key) {
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

    public YAPIONArray getArray(@NonNull String key) {
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

    public YAPIONMap getMap(@NonNull String key) {
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

    public YAPIONPointer getPointer(@NonNull String key) {
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
    public YAPIONValue getValue(@NonNull String key) {
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

    public <T> YAPIONValue<T> getValue(@NonNull String key, Class<T> type) {
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
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getTypeName())) {
            return null;
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    public <T> YAPIONValue<T> getValue(@NonNull String key, T type) {
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

    public YAPIONObject add(@NonNull YAPIONVariable variable) {
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

    public YAPIONObject add(@NonNull String name, @NonNull YAPIONAnyType value) {
        return add(new YAPIONVariable(name, value));
    }

    public YAPIONObject add(@NonNull String name, String value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, char value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, boolean value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, byte value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, short value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, int value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, long value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, BigInteger value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, float value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, double value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject add(@NonNull String name, BigDecimal value) {
        return add(name, new YAPIONValue<>(value));
    }

    public YAPIONObject addOrPointer(@NonNull YAPIONVariable variable) {
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

    public YAPIONObject addOrPointer(@NonNull String name, @NonNull YAPIONAnyType value) {
        return addOrPointer(new YAPIONVariable(name, value));
    }

    public YAPIONObject remove(@NonNull YAPIONVariable yapionVariable) {
        for (int i = variables.size() - 1; i >= 0; i--) {
            if (variables.get(i) == yapionVariable) {
                variables.remove(i).getValue().removeParent();
                break;
            }
        }
        return this;
    }

    public YAPIONObject remove(@NonNull String name) {
        for (int i = variables.size() - 1; i >= 0; i--) {
            if (variables.get(i).getName().equals(name)) {
                variables.remove(i).getValue().removeParent();
                break;
            }
        }
        return this;
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
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(@NonNull String key) {
        if (getVariable(key) == null) return Optional.empty();
        return Optional.of(new YAPIONSearchResult<>(getVariable(key).getValue()));
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
        if (!(o instanceof YAPIONObject)) return false;
        YAPIONObject that = (YAPIONObject) o;
        return variables.equals(that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables);
    }

}