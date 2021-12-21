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
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.api.ObjectOutput;
import yapion.hierarchy.api.groups.SerializingType;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.storage.ObjectAdd;
import yapion.hierarchy.api.storage.ObjectRemove;
import yapion.hierarchy.api.storage.ObjectRetrieve;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.output.flavours.Flavour;
import yapion.utils.IdentifierUtils;
import yapion.utils.RecursionUtils;
import yapion.utils.ReferenceFunction;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

public class YAPIONObject extends YAPIONDataType<YAPIONObject, String> implements ObjectRetrieve<String>, ObjectAdd<YAPIONObject, String>, ObjectRemove<YAPIONObject, String>, SerializingType {

    private final Map<String, YAPIONAnyType> variables = new LinkedHashMap<>();

    public YAPIONObject() {

    }

    /**
     * Convenient Constructor to create an YAPIONObject with a {@link IdentifierUtils#TYPE_IDENTIFIER}.
     *
     * @param type the type this YAPIONObject holds as initial state
     */
    public YAPIONObject(Class<?> type) {
        add(TYPE_IDENTIFIER, type);
    }

    @Override
    public YAPIONType getType() {
        return YAPIONType.OBJECT;
    }

    @Override
    protected long referenceValueProvider(ReferenceFunction referenceFunction) {
        ReferenceValue referenceValue = new ReferenceValue();
        referenceValue.update(getType().getReferenceValue());
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            referenceValue.update(entry.getKey().length());
            referenceValue.update(referenceFunction.stringToReferenceValue(entry.getKey()));
            referenceValue.update(entry.getValue().referenceValue(referenceFunction));
        }
        return referenceValue.referenceValue;
    }

    private <T extends AbstractOutput> void outputSystem(T abstractOutput, Consumer<T> commaConsumer, BiConsumer<String, T> nameConsumer, BiConsumer<YAPIONAnyType, T> valueConsumer) {
        abstractOutput.consume("{");
        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + 1);
        boolean b = false;
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            if (b) commaConsumer.accept(abstractOutput);
            b = true;

            outputComments(abstractOutput, entry.getValue().getComments(), indent);
            abstractOutput.consumePrettified(indent);
            nameConsumer.accept(entry.getKey(), abstractOutput);
            valueConsumer.accept(entry.getValue(), abstractOutput);
        }
        outputComments(abstractOutput, getEndingComments(), indent);
        if (!variables.isEmpty() || hasEndingComments()) abstractOutput.consumePrettified("\n").consumeIndent(getDepth());
        abstractOutput.consume("}");
    }

    @Override
    public <T extends AbstractOutput> T output(T abstractOutput, Flavour flavour) {
        if (!hasParent()) {
            abstractOutput.consume(flavour.header());
        }
        if (flavour.removeRootObject()) {
            if (hasParent()) {
                abstractOutput.consume(flavour.beginObject());
            }
        } else {
            abstractOutput.consume(flavour.beginObject());
        }
        final String indent = "\n" + abstractOutput.getIndentator().indent(getDepth() + (flavour.removeRootObject() ? 0 : 1));
        Flavour.PrettifyBehaviour prettifyBehaviour = flavour.getPrettifyBehaviour();
        boolean b = false;
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            if (b) {
                abstractOutput.consume(flavour.objectFullKeyPairSeparator());
            }
            b = true;

            outputComments(abstractOutput, flavour, prettifyBehaviour, entry.getValue().getComments(), indent);
            if (prettifyBehaviour == Flavour.PrettifyBehaviour.CHOOSEABLE) {
                abstractOutput.consumePrettified(indent);
            } else if (prettifyBehaviour == Flavour.PrettifyBehaviour.ALWAYS) {
                abstractOutput.consume(indent);
            }
            abstractOutput.consume(flavour.objectKeyPairStart(entry.getKey()));
            entry.getValue().output(abstractOutput, flavour);
            abstractOutput.consume(flavour.objectKeyPairEnd(entry.getKey()));
        }
        outputComments(abstractOutput, flavour, prettifyBehaviour, getEndingComments(), indent);
        if (!variables.isEmpty() || hasEndingComments()) {
            if (prettifyBehaviour == Flavour.PrettifyBehaviour.CHOOSEABLE) {
                abstractOutput.consumePrettified("\n").consumeIndent(getDepth() - (flavour.removeRootObject() ? 1 : 0));
            } else if (prettifyBehaviour == Flavour.PrettifyBehaviour.ALWAYS) {
                abstractOutput.consume("\n").consumeIndent(getDepth() - (flavour.removeRootObject() ? 1 : 0));
            }
        }
        if (flavour.removeRootObject()) {
            if (hasParent()) {
                abstractOutput.consume(flavour.endObject());
            }
        } else {
            abstractOutput.consume(flavour.endObject());
        }
        if (!hasParent()) {
            abstractOutput.consume(flavour.footer());
        }
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        outputSystem(abstractOutput, t -> t.consume(","), (s, t) -> {
            t.consume("\"").consume(s).consume("\":");
            t.consumePrettified(" ");
        }, ObjectOutput::toJSON);
        return abstractOutput;
    }

    @Override
    public Map<String, Object> unwrap() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, YAPIONAnyType> entry : variables.entrySet()) {
            result.put(entry.getKey(), entry.getValue().unwrap());
        }
        return result;
    }

    @Override
    public String getPath(YAPIONAnyType yapionAnyType) {
        for (String s : getKeys()) {
            if (getAnyType(s) == yapionAnyType) {
                return s;
            }
        }
        return "";
    }

    public List<String> getKeys() {
        return new ArrayList<>(variables.keySet());
    }

    @Override
    public boolean internalContainsKey(@NonNull String key, YAPIONType yapionType) {
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return false;
        if (yapionType == YAPIONType.ANY) return true;
        return yapionType == yapionAnyType.getType();
    }

    @Override
    public <T> boolean internalContainsKey(@NonNull String key, Class<T> type) {
        if (!YAPIONValue.validType(type)) {
            return false;
        }
        YAPIONAnyType yapionAnyType = getAnyType(key);
        if (yapionAnyType == null) return false;
        if (yapionAnyType instanceof YAPIONValue yapionValue) {
            return yapionValue.isValidCastType(type);
        }
        return false;
    }

    @Override
    public boolean internalContainsValue(@NonNull YAPIONAnyType yapionAnyType) {
        return variables.containsValue(yapionAnyType);
    }

    public YAPIONAnyType internalGetAnyType(@NonNull String key) {
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

    public YAPIONObject internalAdd(@NonNull String key, @NonNull YAPIONAnyType value) {
        check(value);
        discardReferenceValue();
        if (variables.containsKey(key)) {
            variables.get(key).removeParent();
        }
        variables.put(key, value);
        value.setParent(this);
        return this;
    }

    public YAPIONObject add(@NonNull String key, @NonNull Class<?> value) {
        return internalAdd(key, new YAPIONValue<>(value.getTypeName()));
    }

    @Override
    public YAPIONAnyType internalAddAndGetPrevious(@NonNull String key, @NonNull YAPIONAnyType value) {
        check(value);
        discardReferenceValue();
        if (variables.containsKey(key)) {
            variables.get(key).removeParent();
        }
        YAPIONAnyType yapionAnyType = variables.put(key, value);
        value.setParent(this);
        return yapionAnyType;
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
                add(key, new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
                return this;
            }
        }
        add(key, value);
        return this;
    }

    @Override
    public YAPIONAnyType addOrPointerAndGetPrevious(@NonNull String key, @NonNull YAPIONAnyType value) {
        discardReferenceValue();
        if (value.getType() != YAPIONType.VALUE && value.getType() != YAPIONType.POINTER) {
            RecursionUtils.RecursionResult result = RecursionUtils.checkRecursion(value, this);
            if (result.getRecursionType() != RecursionUtils.RecursionType.NONE) {
                if (result.getYAPIONAny() == null) {
                    throw new YAPIONRecursionException("Pointer creation failure.");
                }
                return addAndGetPrevious(key, new YAPIONPointer((YAPIONObject) result.getYAPIONAny()));
            }
        }
        return addAndGetPrevious(key, value);
    }

    public YAPIONObject internalRemove(@NonNull String key) {
        if (variables.containsKey(key)) {
            discardReferenceValue();
            variables.remove(key).removeParent();
        }
        return this;
    }

    @Override
    public YAPIONAnyType internalRemoveAndGet(@NonNull String key) {
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
    public YAPIONObject internalClear() {
        discardReferenceValue();
        variables.forEach((s, yapionAnyType) -> yapionAnyType.removeParent());
        variables.clear();
        return this;
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
    public Stream<Map.Entry<String, YAPIONAnyType>> entryStream() {
        return variables.entrySet().stream();
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
        return size() + stream().filter(YAPIONDataType.class::isInstance)
                .map(YAPIONDataType.class::cast)
                .map(YAPIONDataType::deepSize)
                .mapToLong(value -> value)
                .sum();
    }

    @Override
    public List<YAPIONAnyType> getAllValues() {
        return new ArrayList<>(variables.values());
    }

    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(@NonNull String key) {
        if (getAnyType(key) == null) return Optional.empty();
        return Optional.of(new YAPIONSearchResult<>(getAnyType(key)));
    }

    /**
     * Modifying this is an unsafe operation. When you edit this you are on your own!
     *
     * @return the internal backed map
     */
    @InternalAPI
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

    public YAPIONObject copy() {
        return (YAPIONObject) internalCopy();
    }
}
