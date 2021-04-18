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
import yapion.annotations.DeprecationInfo;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.api.ObjectOutput;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.groups.YAPIONMappingType;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.utils.RecursionUtils;
import yapion.utils.ReferenceFunction;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static yapion.hierarchy.types.value.ValueUtils.EscapeCharacters.KEY;
import static yapion.hierarchy.types.value.ValueUtils.stringToUTFEscapedString;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONObject extends YAPIONMappingType<YAPIONObject, String> {

    private final Map<String, YAPIONAnyType> variables = new LinkedHashMap<>();

    @Override
    public YAPIONType getType() {
        return YAPIONType.OBJECT;
    }

    @Override
    protected long referenceValueProvider(ReferenceFunction referenceFunction) {
        long referenceValue = 0;
        referenceValue += getDepth();
        referenceValue ^= getType().getReferenceValue();
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            referenceValue ^= (long) entry.getKey().length() & 0x7FFFFFFFFFFFFFFFL;
            referenceValue ^= referenceFunction.stringToReferenceValue(entry.getKey()) & 0x7FFFFFFFFFFFFFFFL;
            referenceValue ^= entry.getValue().referenceValue(referenceFunction) & 0x7FFFFFFFFFFFFFFFL;
        }
        return referenceValue;
    }

    private <T extends AbstractOutput> void outputSystem(T abstractOutput, Consumer<T> commaConsumer, BiConsumer<String, T> nameConsumer, BiConsumer<YAPIONAnyType, T> valueConsumer) {
        abstractOutput.consume("{");
        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + 1);
        boolean b = false;
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            if (b) commaConsumer.accept(abstractOutput);
            b = true;

            abstractOutput.consumePrettified(indent);
            nameConsumer.accept(entry.getKey(), abstractOutput);
            valueConsumer.accept(entry.getValue(), abstractOutput);
        }
        if (!variables.isEmpty()) abstractOutput.consumePrettified("\n").consumeIndent(getDepth());
        abstractOutput.consume("}");
    }

    @Override
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        outputSystem(abstractOutput, t -> {}, (s, t) -> {
            if (s.startsWith(" ") || s.startsWith(",")) t.consume("\\");
            t.consume(stringToUTFEscapedString(s, KEY));
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

    public boolean containsKey(@NonNull String key, YAPIONType yapionType) {
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (yapionType == YAPIONType.ANY) return true;
        return yapionType == yapionAnyType.getType();
    }

    public <T> boolean containsKey(@NonNull String key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return false;
        }
        YAPIONAnyType yapionAnyType = getYAPIONAnyType(key);
        if (yapionAnyType == null) return false;
        if (!(yapionAnyType instanceof YAPIONValue)) return false;
        return ((YAPIONValue) yapionAnyType).isValidCastType(type.getTypeName());
    }

    public YAPIONAnyType getYAPIONAnyType(@NonNull String key) {
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

    public YAPIONObject add(@NonNull String key, @NonNull YAPIONAnyType value) {
        check(value);
        discardReferenceValue();
        if (variables.containsKey(key)) {
            variables.get(key).removeParent();
        }
        variables.put(key, value);
        value.setParent(this);
        return this;
    }

    @DeprecationInfo(since = "", alternative = "Use YAPIONObject#add(String, YAPIONAnyType) instead, never use this")
    public YAPIONObject addUnsafe(@NonNull String key, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        if (variables.containsKey(key)) {
            variables.get(key).removeParent();
        }
        variables.put(key, value);
        value.setParent(this);
        return this;
    }

    @Override
    public YAPIONObject addOrPointer(@NonNull String key, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        if (value.getType() != YAPIONType.VALUE && value.getType() != YAPIONType.POINTER) {
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
        }
        add(key, value);
        return this;
    }

    public YAPIONObject remove(@NonNull String key) {
        if (variables.containsKey(key)) {
            discardReferenceValue();
            variables.remove(key).removeParent();
        }
        return this;
    }

    @Override
    public YAPIONAnyType removeAndGet(@NonNull String key) {
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
    public YAPIONObject itself() {
        return this;
    }

    @Override
    public Iterator<YAPIONAnyType> iterator() {
        return variables.values().iterator();
    }

    @Override
    public Set<String> allKeys() {
        return new HashSet<>(variables.keySet());
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

    /**
     * Modifying this is an unsafe operation. When you edit this you are on your own!
     *
     * @return the internal backed map
     */
    public Map<String, YAPIONAnyType> getBackedMap() {
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
        if (!(o instanceof YAPIONObject)) return false;
        YAPIONObject that = (YAPIONObject) o;
        if (!variables.keySet().equals(that.variables.keySet())) {
            return false;
        }
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            if (!entry.getValue().equals(that.variables.get(entry.getKey()))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (int) referenceValue();
    }

}
