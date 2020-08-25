// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.value.YAPIONRecursionException;
import yapion.hierarchy.Type;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.utils.RecursionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@YAPIONSave(context = "*")
public class YAPIONObject extends YAPIONAny {

    private final List<YAPIONVariable> variables = new ArrayList<>();

    @Override
    public Type getType() {
        return Type.OBJECT;
    }

    @Override
    public long referenceValue() {
        long referenceValue = 0;
        referenceValue += getDepth();
        for (YAPIONVariable variable : variables) {
            referenceValue ^= variable.referenceValue() & 0x7FFFFFFFFFFFFFFFL;
        }
        return referenceValue;
    }

    @Override
    public void toOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write("{".getBytes(StandardCharsets.UTF_8));
        for (YAPIONVariable variable : variables) {
            variable.toOutputStream(outputStream);
        }
        outputStream.write("}".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getPath(YAPIONAny yapionAny) {
        for (String s : getKeys()) {
            if (getVariable(s).getValue() == yapionAny) {
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
        YAPIONAny yapionAny = yapionVariable.getValue();
        if (yapionAny instanceof YAPIONObject) {
            return (YAPIONObject) yapionAny;
        }
        return null;
    }

    public YAPIONArray getArray(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAny yapionAny = yapionVariable.getValue();
        if (yapionAny instanceof YAPIONArray) {
            return (YAPIONArray) yapionAny;
        }
        return null;
    }

    public YAPIONMap getMap(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAny yapionAny = yapionVariable.getValue();
        if (yapionAny instanceof YAPIONMap) {
            return (YAPIONMap) yapionAny;
        }
        return null;
    }

    public YAPIONPointer getPointer(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAny yapionAny = yapionVariable.getValue();
        if (yapionAny instanceof YAPIONPointer) {
            return (YAPIONPointer) yapionAny;
        }
        return null;
    }

    @SuppressWarnings({"java:S3740"})
    public YAPIONValue getValue(String key) {
        YAPIONVariable yapionVariable = getVariable(key);
        if (yapionVariable == null) {
            return null;
        }
        YAPIONAny yapionAny = yapionVariable.getValue();
        if (yapionAny instanceof YAPIONValue) {
            return (YAPIONValue) yapionAny;
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
        YAPIONAny yapionAny = yapionVariable.getValue();
        if (!(yapionAny instanceof YAPIONValue)) {
            return null;
        }
        if (!((YAPIONValue)yapionAny).type.equalsIgnoreCase(type.getClass().getTypeName())) {
            return null;
        }
        return (YAPIONValue<T>) yapionAny;
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
        for (int i = variables.size() - 1; i >= 0; i--) {
            if (variables.get(i).getName().equals(variable.getName())) {
                variables.remove(i).getValue().removeParent();
            }
        }
        variables.add(variable);
        variable.getValue().setParent(this);
        return this;
    }

    public YAPIONObject add(String name, YAPIONAny value) {
        return add(new YAPIONVariable(name, value));
    }

    public YAPIONObject addOrPointer(YAPIONVariable variable) {
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
    protected Optional<YAPIONSearch<? extends YAPIONAny>> get(String key) {
        if (getVariable(key) == null) return Optional.empty();
        return Optional.of(new YAPIONSearch<>(getVariable(key).getValue()));
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("{");
        for (YAPIONVariable yapionVariable : variables) {
            st.append(yapionVariable.toString());
        }
        return st.append("}").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YAPIONObject)) return false;
        YAPIONObject that = (YAPIONObject) o;
        return Objects.equals(variables, that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables);
    }

}