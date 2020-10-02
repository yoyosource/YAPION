// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.hierarchy.typegroups.YAPIONAnyClosure;
import yapion.hierarchy.typegroups.YAPIONAnyType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static yapion.utils.ReferenceIDUtils.calc;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public final class YAPIONVariable extends YAPIONAnyClosure {

    private final String name;
    private final YAPIONAnyType value;

    private static final String PATTERN = "[({\\[<)}\\]>]";
    private static final String REPLACEMENT = "\\\\$0";

    public YAPIONVariable(String name, YAPIONAnyType value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public YAPIONAnyType getValue() {
        return value;
    }

    public long referenceValue() {
        return ((long)name.length() ^ calc(name) ^ value.referenceValue()) & 0x7FFFFFFFFFFFFFFFL;
    }

    @Override
    public String toString() {
        return toYAPIONString();
    }

    @Override
    public String toYAPIONString() {
        if (name.startsWith(" ")) {
            return "\\" + name.replaceAll(PATTERN, REPLACEMENT) + value.toYAPIONString();
        }
        return name.replaceAll(PATTERN, REPLACEMENT) + value.toYAPIONString();
    }

    @Override
    public String toYAPIONStringPrettified() {
        if (name.startsWith(" ")) {
            return "\\" + name.replaceAll(PATTERN, REPLACEMENT) + value.toYAPIONStringPrettified();
        }
        return name.replaceAll(PATTERN, REPLACEMENT) + value.toYAPIONStringPrettified();
    }

    public String toJSONString() {
        return "\"" + name + "\":" + value.toJSONString();
    }

    @Override
    public String toLossyJSONString() {
        return "\"" + name + "\":" + value.toLossyJSONString();
    }

    @Override
    public void toOutputStream(OutputStream outputStream) throws IOException {
        if (name.startsWith(" ")) outputStream.write("\\".getBytes(StandardCharsets.UTF_8));
        outputStream.write(name.replaceAll(PATTERN, REPLACEMENT).getBytes(StandardCharsets.UTF_8));
        value.toOutputStream(outputStream);
    }

    @Override
    public void toOutputStreamPrettified(OutputStream outputStream) throws IOException {
        if (name.startsWith(" ")) outputStream.write("\\".getBytes(StandardCharsets.UTF_8));
        outputStream.write(name.replaceAll(PATTERN, REPLACEMENT).getBytes(StandardCharsets.UTF_8));
        value.toOutputStreamPrettified(outputStream);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YAPIONVariable)) return false;
        YAPIONVariable variable = (YAPIONVariable) o;
        return name.equals(variable.name) && value.equals(variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}