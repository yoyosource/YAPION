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
import yapion.annotations.api.YAPIONEveryType;
import yapion.exceptions.YAPIONException;
import yapion.exceptions.serializing.YAPIONClassTypeException;
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.storage.MapAdd;
import yapion.hierarchy.api.storage.MapRemove;
import yapion.hierarchy.api.storage.MapRetrieve;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.output.flavours.Flavour;
import yapion.utils.RecursionUtils;
import yapion.utils.ReferenceFunction;

import java.util.*;
import java.util.stream.Stream;

public class YAPIONMap extends YAPIONDataType<YAPIONMap, YAPIONAnyType> implements MapAdd<YAPIONMap, YAPIONAnyType>, MapRemove<YAPIONMap, YAPIONAnyType>, MapRetrieve<YAPIONAnyType> {

    private final Map<YAPIONAnyType, YAPIONAnyType> variables = new LinkedHashMap<>();

    private final List<YAPIONAnyType> yapionParserMapValues = new ArrayList<>();

    @Override
    public YAPIONType getType() {
        return YAPIONType.MAP;
    }

    @Override
    protected long referenceValueProvider(ReferenceFunction referenceFunction) {
        ReferenceValue referenceValue = new ReferenceValue();
        referenceValue.update(getType().getReferenceValue());
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> e : variables.entrySet()) {
            referenceValue.update((e.getKey().referenceValue(referenceFunction) * e.getValue().referenceValue(referenceFunction)));
        }
        return referenceValue.referenceValue;
    }

    @Override
    public <T extends AbstractOutput> T output(T abstractOutput, Flavour flavour) {
        flavour = convert(flavour);
        flavour.begin(Flavour.HierarchyTypes.MAP, abstractOutput);

        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + (flavour.removeRootObject() ? 0 : 1));
        Flavour.PrettifyBehaviour prettifyBehaviour = flavour.getPrettifyBehaviour();
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            outputComments(abstractOutput, flavour, prettifyBehaviour, entry.getKey().getComments(), indent);

            if (prettifyBehaviour == Flavour.PrettifyBehaviour.CHOOSEABLE) {
                abstractOutput.consumePrettified(indent);
            } else if (prettifyBehaviour == Flavour.PrettifyBehaviour.ALWAYS) {
                abstractOutput.consume(indent);
            }

            entry.getKey().output(abstractOutput, flavour);
            flavour.elementSeparator(Flavour.HierarchyTypes.MAP, abstractOutput, false);
            outputComments(abstractOutput, flavour, prettifyBehaviour, entry.getValue().getComments(), indent);
            if (entry.getValue().hasComments()) {
                if (prettifyBehaviour == Flavour.PrettifyBehaviour.CHOOSEABLE) {
                    abstractOutput.consumePrettified(indent);
                } else if (prettifyBehaviour == Flavour.PrettifyBehaviour.ALWAYS) {
                    abstractOutput.consume(indent);
                }
            }
            entry.getValue().output(abstractOutput, flavour);
        }

        outputComments(abstractOutput, flavour, prettifyBehaviour, getEndingComments(), indent);
        if (!variables.isEmpty() || hasEndingComments()) {
            abstractOutput.consumePrettified("\n").consumeIndent(getDepth() - (flavour.removeRootObject() ? 1 : 0));
        }

        flavour.end(Flavour.HierarchyTypes.MAP, abstractOutput);
        return abstractOutput;
    }

    // TODO: YAPIONConvertedJSONFlavour does not support Maps for the moment
    /*
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
    */

    @Override
    public <U> U unwrap() {
        throw new UnsupportedOperationException("Unwrapping maps is not supported.");
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
    public boolean internalContainsKey(@NonNull YAPIONAnyType key, YAPIONType yapionType) {
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return false;
        if (yapionType == YAPIONType.ANY) return true;
        return yapionType == yapionAnyType.getType();
    }

    @Override
    public <T> boolean internalContainsKey(@NonNull YAPIONAnyType key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return false;
        }
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return false;
        if (yapionAnyType instanceof YAPIONValue<?> yapionValue) {
            return yapionValue.isValidCastType(type);
        }
        return false;
    }

    @Override
    public boolean internalContainsValue(@NonNull YAPIONAnyType yapionAnyType) {
        return variables.containsValue(yapionAnyType);
    }

    public YAPIONAnyType internalGetAnyType(@NonNull YAPIONAnyType key) {
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

    public YAPIONMap internalAdd(@NonNull YAPIONAnyType key, @NonNull YAPIONAnyType value) {
        check(value);
        discardReferenceValue();
        variables.put(key, value);
        value.setParent(this);
        key.setParent(this);
        return this;
    }

    @Override
    public YAPIONAnyType internalAddAndGetPrevious(@NonNull YAPIONAnyType key, @NonNull YAPIONAnyType value) {
        check(value);
        discardReferenceValue();
        YAPIONAnyType yapionAnyType = variables.put(key, value);
        value.setParent(this);
        key.setParent(this);
        return yapionAnyType;
    }

    @Override
    public <@YAPIONEveryType C> YAPIONMap addOrPointer(@NonNull C key, @NonNull YAPIONAnyType value) {
        if (YAPIONValue.validType(key)) {
            return add(new YAPIONValue<>(key), value);
        } else if (!(key instanceof YAPIONAnyType)) {
            throw new YAPIONClassTypeException("The type '" + value.getClass().getTypeName() + "' is not a valid YAPIONEveryType");
        }
        YAPIONAnyType internalKey = (YAPIONAnyType) key;
        discardReferenceValue();
        if (internalKey.getType() != YAPIONType.VALUE && internalKey.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult resultKey = RecursionUtils.checkRecursion(internalKey, this);
            if (resultKey.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (resultKey.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                internalKey = new YAPIONPointer((YAPIONObject) resultKey.getYAPIONAny());
            }
        }
        if (value.getType() != YAPIONType.VALUE && value.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult resultValue = RecursionUtils.checkRecursion(value, this);
            if (resultValue.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (resultValue.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                value = new YAPIONPointer((YAPIONObject) resultValue.getYAPIONAny());
            }
        }
        add(internalKey, value);
        return this;
    }

    @Override
    public <@YAPIONEveryType C> YAPIONAnyType addOrPointerAndGetPrevious(@NonNull C key, @NonNull YAPIONAnyType value) {
        if (YAPIONValue.validType(key)) {
            return add(new YAPIONValue<>(key), value);
        } else if (!(key instanceof YAPIONAnyType)) {
            throw new YAPIONClassTypeException("The type '" + value.getClass().getTypeName() + "' is not a valid YAPIONEveryType");
        }
        YAPIONAnyType internalKey = (YAPIONAnyType) key;
        discardReferenceValue();
        if (internalKey.getType() != YAPIONType.VALUE && internalKey.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult resultKey = RecursionUtils.checkRecursion(internalKey, this);
            if (resultKey.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (resultKey.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                if (!(resultKey.getYAPIONAny() instanceof YAPIONObject)) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                internalKey = new YAPIONPointer((YAPIONObject) resultKey.getYAPIONAny());
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
        return addAndGetPrevious(internalKey, value);
    }

    public YAPIONMap internalRemove(@NonNull YAPIONAnyType key) {
        if (variables.containsKey(key)) {
            discardReferenceValue();
            variables.remove(key).removeParent();
        }
        return this;
    }

    @Override
    public YAPIONAnyType internalRemoveAndGet(@NonNull YAPIONAnyType key) {
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
    public YAPIONMap internalClear() {
        discardReferenceValue();
        variables.forEach((key, value) -> {
            key.removeParent();
            value.removeParent();
        });
        variables.clear();
        return this;
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
    public Stream<Map.Entry<YAPIONAnyType, YAPIONAnyType>> entryStream() {
        return variables.entrySet().stream();
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
            if (entry.getKey() instanceof YAPIONDataType yapionDataType) size += yapionDataType.deepSize();
            if (entry.getValue() instanceof YAPIONDataType yapionDataType) size += yapionDataType.deepSize();
        }
        return size;
    }

    @Override
    public List<YAPIONAnyType> getAllValues() {
        return new ArrayList<>(variables.values());
    }

    // Internal method for Parser
    @InternalAPI
    public void unsafeAdd(@NonNull YAPIONAnyType variable) {
        yapionParserMapValues.add(variable);
        variable.setParent(this);
    }

    // Internal method for Parser
    @InternalAPI
    public void finishMapping() {
        if (yapionParserMapValues.isEmpty()) {
            return;
        }
        discardReferenceValue();

        while (yapionParserMapValues.size() > 1) {
            YAPIONAnyType yapionParserMapValue1 = yapionParserMapValues.remove(0);
            YAPIONAnyType yapionParserMapValue2 = yapionParserMapValues.remove(0);
            check(yapionParserMapValue1);
            check(yapionParserMapValue2);
            variables.put(yapionParserMapValue1, yapionParserMapValue2);
        }
        if (!yapionParserMapValues.isEmpty()) {
            throw new YAPIONException("YAPIONMap was not fully qualified");
        }
    }

    // Internal method for Parser
    @InternalAPI
    public int parsedSize() {
        return yapionParserMapValues.size();
    }

    public YAPIONAnyType get(@NonNull YAPIONAnyType key) {
        return variables.get(key);
    }

    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> getViaPath(@NonNull String key) {
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            if (entry.getKey().toString().equals(key))
                return Optional.of(new YAPIONSearchResult<>(entry.getValue()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<YAPIONMap> asMap() {
        return Optional.of(this);
    }

    /**
     * Modifying this is an unsafe operation. When you edit this you are on your own!
     *
     * @return the internal backed map
     */
    @InternalAPI
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

    public YAPIONMap copy() {
        return (YAPIONMap) internalCopy();
    }
}
