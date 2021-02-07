// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types;

import lombok.NonNull;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSave;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.groups.YAPIONMappingType;
import yapion.hierarchy.api.storage.ObjectAdd;
import yapion.hierarchy.api.storage.ObjectRemove;
import yapion.hierarchy.api.storage.ObjectRetrieve;
import yapion.parser.YAPIONParserMapMapping;
import yapion.parser.YAPIONParserMapObject;
import yapion.utils.RecursionUtils;

import java.util.*;

import static yapion.utils.IdentifierUtils.MAP_IDENTIFIER;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONMap extends YAPIONMappingType implements ObjectRetrieve<YAPIONAnyType>, ObjectAdd<YAPIONMap, YAPIONAnyType>, ObjectRemove<YAPIONMap, YAPIONAnyType> {

    private final Map<YAPIONAnyType, YAPIONAnyType> variables = new LinkedHashMap<>();
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private final List<YAPIONParserMapMapping> mappingList = new ArrayList<>();
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private final Map<String, YAPIONAnyType> mappingVariables = new LinkedHashMap<>();

    @Override
    public YAPIONType getType() {
        return YAPIONType.MAP;
    }

    @Override
    public long referenceValue() {
        if (hasReferenceValue()) {
            return getReferenceValue();
        }
        long referenceValue = 0;
        referenceValue ^= getType().getReferenceValue();
        referenceValue += getDepth();
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> e : variables.entrySet()) {
            referenceValue ^= (e.getKey().referenceValue() * e.getValue().referenceValue()) & 0x7FFFFFFFFFFFFFFFL;
        }
        cacheReferenceValue(referenceValue);
        return referenceValue;
    }

    @Override
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        abstractOutput.consume("<");

        long id = 0;
        final String indent = "\n" + indent();
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            abstractOutput.consumePrettified(indent);

            String id1 = String.format("%01X", id++);
            String id2 = String.format("%01X", id++);

            abstractOutput.consume(id1 + ":" + id2);
            abstractOutput.consumePrettified(indent);

            abstractOutput.consume("#" + id1);
            entry.getKey().toYAPION(abstractOutput);
            abstractOutput.consumePrettified(indent);

            abstractOutput.consume("#" + id2);
            entry.getValue().toYAPION(abstractOutput);
        }

        if (!variables.isEmpty()) {
            abstractOutput.consumePrettified("\n").consumePrettified(reducedIndent());
        }

        abstractOutput.consume(">");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.setParent(this);
        YAPIONArray mapping = new YAPIONArray();
        yapionObject.add(MAP_IDENTIFIER, mapping);

        long id = 0;
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            String id1 = String.format("%01X", id++);
            String id2 = String.format("%01X", id++);

            mapping.add(new YAPIONValue<>(id1 + ":" + id2));
            yapionObject.add("#" + id1, entry.getKey());
            yapionObject.add("#" + id2, entry.getValue());
        }
        yapionObject.toJSON(abstractOutput);
        yapionObject.removeParent();
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.setParent(this);
        YAPIONArray mapping = new YAPIONArray();
        yapionObject.add(MAP_IDENTIFIER, mapping);

        long id = 0;
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            String id1 = String.format("%01X", id++);
            String id2 = String.format("%01X", id++);

            mapping.add(new YAPIONValue<>(id1 + ":" + id2));
            yapionObject.add("#" + id1, entry.getKey());
            yapionObject.add("#" + id2, entry.getValue());
        }
        yapionObject.toJSONLossy(abstractOutput);
        yapionObject.removeParent();
        return abstractOutput;
    }

    @Override
    public String getPath(YAPIONAnyType yapionAnyType) {
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            if (entry.getValue() == yapionAnyType) {
                return entry.getKey().toString();
            }
            if (entry.getKey() == yapionAnyType) {
                return entry.getKey().toString();
            }
        }
        return "";
    }

    public List<YAPIONAnyType> getKeys() {
        return new ArrayList<>(variables.keySet());
    }

    @Override
    public boolean hasValue(@NonNull YAPIONAnyType key, YAPIONType yapionType) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (yapionType == YAPIONType.ANY) return true;
        return yapionType == yapionAnyType.getType();
    }

    @Override
    public <T> boolean hasValue(@NonNull YAPIONAnyType key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return false;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (!(yapionAnyType instanceof YAPIONValue)) return false;
        return ((YAPIONValue) yapionAnyType).getValueType().equalsIgnoreCase(type.getTypeName());
    }

    public YAPIONAnyType getYAPIONAnyType(@NonNull YAPIONAnyType key) {
        return variables.get(key);
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

    public YAPIONMap add(@NonNull YAPIONAnyType key, @NonNull YAPIONAnyType value) {
        check(value);
        discardReferenceValue();
        variables.put(key, value);
        value.setParent(this);
        key.setParent(this);
        return this;
    }

    @Override
    public YAPIONMap addOrPointer(@NonNull YAPIONAnyType key, @NonNull YAPIONAnyType value) {
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

    public YAPIONMap add(@NonNull YAPIONParserMapObject variable) {
        check(variable.value);
        discardReferenceValue();
        mappingVariables.put(variable.key.substring(1), variable.value);
        variable.value.setParent(this);
        return this;
    }

    public YAPIONMap add(@NonNull YAPIONParserMapMapping mapping) {
        discardReferenceValue();
        mappingList.add(mapping);
        return this;
    }

    public YAPIONMap remove(@NonNull YAPIONAnyType key) {
        discardReferenceValue();
        variables.remove(key).removeParent();
        return this;
    }

    @Override
    public YAPIONAnyType removeAndGet(@NonNull YAPIONAnyType key) {
        discardReferenceValue();
        YAPIONAnyType yapionAnyType = variables.remove(key);
        yapionAnyType.removeParent();
        return yapionAnyType;
    }

    @Override
    public int size() {
        return variables.size();
    }

    @Override
    public long deepSize() {
        long size = size();
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            if (entry.getKey() instanceof YAPIONDataType) size += ((YAPIONDataType) entry.getKey()).deepSize();
            if (entry.getValue() instanceof YAPIONDataType) size += ((YAPIONDataType) entry.getValue()).deepSize();
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

    public synchronized YAPIONMap finishMapping() {
        discardReferenceValue();
        if (mappingVariables.isEmpty()) {
            return this;
        }

        for (YAPIONParserMapMapping mapping : mappingList) {
            String[] strings = mapping.mapping.get().split(":");
            if (strings.length != 2) {
                continue;
            }

            variables.put(mappingVariables.get(strings[0]), mappingVariables.get(strings[1]));
        }

        mappingVariables.clear();
        mappingList.clear();
        return this;
    }

    public YAPIONAnyType get(@NonNull YAPIONAnyType key) {
        return variables.get(key);
    }

    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(@NonNull String key) {
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            if (entry.getKey().toString().equals(key))
                return Optional.of(new YAPIONSearchResult<>(entry.getValue()));
        }
        return Optional.empty();
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
        if (!(o instanceof YAPIONMap)) return false;
        YAPIONMap yapionMap = (YAPIONMap) o;
        return variables.equals(yapionMap.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables);
    }
}