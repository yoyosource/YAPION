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
import yapion.hierarchy.typeinterfaces.ObjectOutput;
import yapion.utils.RecursionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static yapion.utils.ReferenceIDUtils.calc;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONObject extends YAPIONMappingType {

    private static final String PATTERN = "[({\\[<)}\\]>]";
    private static final String REPLACEMENT = "\\\\$0";

    private final Map<String, YAPIONAnyType> variables = new LinkedHashMap<>();

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
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            referenceValue ^= ((long) entry.getKey().length() ^ calc(entry.getKey()) ^ entry.getValue().referenceValue()) & 0x7FFFFFFFFFFFFFFFL;
        }
        cacheReferenceValue(referenceValue);
        return referenceValue;
    }

    private <T extends AbstractOutput> void outputSystem(T abstractOutput, Consumer<T> commaConsumer, BiConsumer<String, T> nameConsumer, BiConsumer<YAPIONAnyType, T> valueConsumer) {
        abstractOutput.consume("{");
        final String indent = "\n" + indent();
        boolean b = false;
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            if (b) commaConsumer.accept(abstractOutput);
            b = true;

            abstractOutput.consumePrettified(indent);
            nameConsumer.accept(entry.getKey(), abstractOutput);
            valueConsumer.accept(entry.getValue(), abstractOutput);
        }
        if (!variables.isEmpty()) abstractOutput.consumePrettified("\n").consumePrettified(reducedIndent());
        abstractOutput.consume("}");
    }

    @Override
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        outputSystem(abstractOutput, t -> {}, (s, t) -> {
            if (s.startsWith(" ")) t.consume("\\");
            t.consume(s.replaceAll(PATTERN, REPLACEMENT));
        }, ObjectOutput::toYAPION);
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        outputSystem(abstractOutput, t -> t.consume(","), (s, t) -> {
            t.consume("\"").consume(s).consume("\":");
        }, ObjectOutput::toJSON);
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
        outputSystem(abstractOutput, t -> t.consume(","), (s, t) -> {
            t.consume("\"").consume(s).consume("\":");
        }, ObjectOutput::toJSONLossy);
        return abstractOutput;
    }

    @Override
    public String getPath(YAPIONAnyType yapionAnyType) {
        for (String s : getKeys()) {
            if (getYAPIONAnyType(s) == yapionAnyType) {
                return s;
            }
        }
        return "";
    }

    public List<String> getKeys() {
        return new ArrayList<>(variables.keySet());
    }

    public boolean hasValue(@NonNull String key) {
        return hasValue(key, YAPIONType.ANY);
    }

    public boolean hasValue(@NonNull String key, YAPIONType yapionType) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (yapionType == YAPIONType.ANY) return true;
        return yapionType == yapionAnyType.getType();
    }

    public <T> boolean hasYAPIONValue(@NonNull String key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return false;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (!(yapionAnyType instanceof YAPIONValue)) return false;
        return ((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getTypeName());
    }

    public YAPIONAnyType getYAPIONAnyType(@NonNull String key) {
        return variables.get(key);
    }

    public YAPIONObject getObject(@NonNull String key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONObject) {
            return (YAPIONObject) yapionAnyType;
        }
        return null;
    }

    public void getObject(@NonNull String key, Consumer<YAPIONObject> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONObject) {
            valueConsumer.accept((YAPIONObject) yapionAnyType);
        }
    }

    public YAPIONArray getArray(@NonNull String key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONArray) {
            return (YAPIONArray) yapionAnyType;
        }
        return null;
    }

    public void getArray(@NonNull String key, Consumer<YAPIONArray> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONArray) {
            valueConsumer.accept((YAPIONArray) yapionAnyType);
        }
    }

    public YAPIONMap getMap(@NonNull String key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONMap) {
            return (YAPIONMap) yapionAnyType;
        }
        return null;
    }

    public void getMap(@NonNull String key, Consumer<YAPIONMap> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONMap) {
            valueConsumer.accept((YAPIONMap) yapionAnyType);
        }
    }

    public YAPIONPointer getPointer(@NonNull String key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONPointer) {
            return (YAPIONPointer) yapionAnyType;
        }
        return null;
    }

    public void getPointer(@NonNull String key, Consumer<YAPIONPointer> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONPointer) {
            valueConsumer.accept((YAPIONPointer) yapionAnyType);
        }
    }

    @SuppressWarnings({"java:S3740"})
    public YAPIONValue getValue(@NonNull String key) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (yapionAnyType instanceof YAPIONValue) {
            return (YAPIONValue) yapionAnyType;
        }
        return null;
    }

    public void getValue(@NonNull String key, Consumer<YAPIONValue> valueConsumer, Runnable noValue) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (yapionAnyType instanceof YAPIONValue) {
            valueConsumer.accept((YAPIONValue) yapionAnyType);
        }
    }

    public <T> YAPIONValue<T> getValue(@NonNull String key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return null;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return null;
        }
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getTypeName())) {
            return null;
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    public <T> void getObject(@NonNull String key, Class<T> type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        if (!YAPIONValue.validType(type)) {
            return;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return;
        }
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getTypeName())) {
            return;
        }
        valueConsumer.accept((YAPIONValue<T>) yapionAnyType);
    }

    public <T> YAPIONValue<T> getValue(@NonNull String key, T type) {
        if (!YAPIONValue.validType(type)) {
            return null;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return null;
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return null;
        }
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getClass().getTypeName())) {
            return null;
        }
        return (YAPIONValue<T>) yapionAnyType;
    }

    public <T> void getObject(@NonNull String key, T type, Consumer<YAPIONValue<T>> valueConsumer, Runnable noValue) {
        if (!YAPIONValue.validType(type)) {
            return;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) {
            noValue.run();
            return;
        }
        if (!(yapionAnyType instanceof YAPIONValue)) {
            return;
        }
        if (!((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getClass().getTypeName())) {
            return;
        }
        valueConsumer.accept((YAPIONValue<T>) yapionAnyType);
    }

    private void check(YAPIONAnyType yapionAnyType) {
        RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(yapionAnyType, this);
        if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
            if (result.getRecursionType() == RecursionUtils.RecursionType.DIRECT) {
                throw new YAPIONRecursionException("Direct Recursion failure. Use a pointer instead.");
            } else {
                throw new YAPIONRecursionException("Back Reference failure. Use a pointer instead.");
            }
        }
    }

    public YAPIONObject add(@NonNull String name, @NonNull YAPIONAnyType value) {
        check(value);
        discardReferenceValue();
        if (variables.containsKey(name)) {
            variables.get(name).removeParent();
        }
        variables.put(name, value);
        value.setParent(this);
        return this;
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

    public YAPIONObject addOrPointer(@NonNull String name, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(value, this);
        if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
            if (result.getYAPIONAny() == null) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            if (!(result.getYAPIONAny() instanceof YAPIONObject)) {
                throw new YAPIONRecursionException("Pointer creation failure.");
            }
            add(name, new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
            return this;
        }
        add(name, value);
        return this;
    }

    public YAPIONObject remove(@NonNull String name) {
        variables.remove(name).removeParent();
        return this;
    }

    @Override
    public int size() {
        return variables.size();
    }

    @Override
    public long deepSize() {
        long size = size();
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            YAPIONAnyType yapionAnyType = entry.getValue();
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
        return new ArrayList<>(variables.values());
    }

    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(@NonNull String key) {
        if (getYAPIONAnyType(key) == null) return Optional.empty();
        return Optional.of(new YAPIONSearchResult<>(getYAPIONAnyType(key)));
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