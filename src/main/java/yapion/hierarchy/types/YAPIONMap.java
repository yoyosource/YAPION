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
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSave;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.groups.YAPIONMappingType;
import yapion.hierarchy.api.storage.MapAdd;
import yapion.hierarchy.api.storage.MapRemove;
import yapion.hierarchy.api.storage.MapRetrieve;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.parser.YAPIONParserMapValue;
import yapion.utils.RecursionUtils;
import yapion.utils.ReferenceFunction;

import java.util.*;

import static yapion.utils.IdentifierUtils.MAP_IDENTIFIER;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONMap extends YAPIONMappingType<YAPIONMap, YAPIONAnyType> implements MapAdd<YAPIONMap>, MapRemove<YAPIONMap>, MapRetrieve {

    private final Map<YAPIONAnyType, YAPIONAnyType> variables = new LinkedHashMap<>();

    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private final List<YAPIONParserMapValue> yapionParserMapValues = new ArrayList<>();

    @Override
    public YAPIONType getType() {
        return YAPIONType.MAP;
    }

    @Override
    protected long referenceValueProvider(ReferenceFunction referenceFunction) {
        long referenceValue = 0;
        referenceValue ^= getType().getReferenceValue();
        referenceValue += getDepth();
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> e : variables.entrySet()) {
            referenceValue ^= (e.getKey().referenceValue(referenceFunction) * e.getValue().referenceValue(referenceFunction)) & 0x7FFFFFFFFFFFFFFFL;
        }
        return referenceValue;
    }

    @Override
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        abstractOutput.consume("<");

        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + 1);
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            abstractOutput.consumePrettified(indent);
            entry.getKey().toYAPION(abstractOutput);
            abstractOutput.consume(":");
            entry.getValue().toYAPION(abstractOutput);
        }

        if (!variables.isEmpty()) {
            abstractOutput.consumePrettified("\n").consumeIndent(getDepth());
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
    public boolean containsKey(@NonNull YAPIONAnyType key, YAPIONType yapionType) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (yapionType == YAPIONType.ANY) return true;
        return yapionType == yapionAnyType.getType();
    }

    @Override
    public <T> boolean containsKey(@NonNull YAPIONAnyType key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return false;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (!(yapionAnyType instanceof YAPIONValue)) return false;
        return ((YAPIONValue) yapionAnyType).isValidCastType(type.getTypeName());
    }

    @Override
    public boolean containsValue(@NonNull YAPIONAnyType yapionAnyType) {
        return variables.containsValue(yapionAnyType);
    }

    public YAPIONAnyType getYAPIONAnyType(@NonNull YAPIONAnyType key) {
        return variables.get(key);
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

    public YAPIONMap add(@NonNull YAPIONAnyType key, @NonNull YAPIONAnyType value) {
        check(value);
        discardReferenceValue();
        variables.put(key, value);
        value.setParent(this);
        key.setParent(this);
        return this;
    }

    @Override
    public YAPIONAnyType addAndGetPrevious(@NonNull YAPIONAnyType key, @NonNull YAPIONAnyType value) {
        check(value);
        discardReferenceValue();
        value.setParent(this);
        key.setParent(this);
        return variables.put(key, value);
    }

    @Override
    public YAPIONMap addOrPointer(@NonNull YAPIONAnyType key, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        if (key.getType() != YAPIONType.VALUE && key.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult resultKey = RecursionUtils.checkRecursion(key, this);
            if (resultKey.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (resultKey.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                if (!(resultKey.getYAPIONAny() instanceof YAPIONObject)) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                key = new YAPIONPointer((YAPIONObject) resultKey.getYAPIONAny());
            }
        }
        if (value.getType() != YAPIONType.VALUE && value.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult resultValue = RecursionUtils.checkRecursion(value, this);
            if (resultValue.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (resultValue.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                if (!(resultValue.getYAPIONAny() instanceof YAPIONObject)) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                value = new YAPIONPointer((YAPIONObject) resultValue.getYAPIONAny());
            }
        }
        add(key, value);
        return this;
    }

    @Override
    public YAPIONAnyType addOrPointerAndGetPrevious(@NonNull YAPIONAnyType key, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        if (key.getType() != YAPIONType.VALUE && key.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult resultKey = RecursionUtils.checkRecursion(key, this);
            if (resultKey.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (resultKey.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                if (!(resultKey.getYAPIONAny() instanceof YAPIONObject)) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                key = new YAPIONPointer((YAPIONObject) resultKey.getYAPIONAny());
            }
        }
        if (value.getType() != YAPIONType.VALUE && value.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult resultValue = RecursionUtils.checkRecursion(value, this);
            if (resultValue.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (resultValue.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                if (!(resultValue.getYAPIONAny() instanceof YAPIONObject)) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                value = new YAPIONPointer((YAPIONObject) resultValue.getYAPIONAny());
            }
        }
        return addAndGetPrevious(key, value);
    }

    public YAPIONMap remove(@NonNull YAPIONAnyType key) {
        if (variables.containsKey(key)) {
            discardReferenceValue();
            variables.remove(key).removeParent();
        }
        return this;
    }

    @Override
    public YAPIONAnyType removeAndGet(@NonNull YAPIONAnyType key) {
        if (variables.containsKey(key)) {
            discardReferenceValue();
            YAPIONAnyType yapionAnyType = variables.remove(key);
            yapionAnyType.removeParent();
            return yapionAnyType;
        } else {
            return null;
        }
    }

    @Override
    public YAPIONMap itself() {
        return this;
    }

    @Override
    public Iterator<YAPIONAnyType> iterator() {
        return variables.values().iterator();
    }

    @Override
    public Set<YAPIONAnyType> allKeys() {
        return new HashSet<>(variables.keySet());
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

    // Internal method for Parser
    public void add(@NonNull YAPIONParserMapValue variable) {
        check(variable.value);
        discardReferenceValue();
        yapionParserMapValues.add(variable);
        variable.value.setParent(this);
    }

    // Internal method for Parser
    public void finishMapping() {
        discardReferenceValue();
        if (yapionParserMapValues.isEmpty()) {
            return;
        }

        while (yapionParserMapValues.size() > 1) {
            YAPIONParserMapValue yapionParserMapValue1 = yapionParserMapValues.remove(0);
            YAPIONParserMapValue yapionParserMapValue2 = yapionParserMapValues.remove(0);
            variables.put(yapionParserMapValue1.value, yapionParserMapValue2.value);
        }
        yapionParserMapValues.clear();
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

    /**
     * Modifying this is an unsafe operation. When you edit this you are on your own!
     *
     * @return the internal backed map
     */
    public Map<YAPIONAnyType, YAPIONAnyType> getBackedMap() {
        return variables;
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
        YAPIONMap that = (YAPIONMap) o;
        if (!variables.keySet().equals(that.variables.keySet())) {
            return false;
        }
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            if (!entry.getValue().equals(that.variables.get(entry.getKey()))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (int) referenceValue();
    }
}
