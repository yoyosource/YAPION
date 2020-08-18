// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy;

import yapion.annotations.serialize.YAPIONSave;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static yapion.utils.ReferenceIDUtils.calc;

@YAPIONSave(context = "*")
public class YAPIONVariable {

    private final String name;
    private final YAPIONAny value;

    private static final String PATTERN = "[({\\[<)}\\]>]";
    private static final String REPLACEMENT = "\\\\$0";

    public YAPIONVariable(String name, YAPIONAny value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public YAPIONAny getValue() {
        return value;
    }

    public long referenceValue() {
        return ((long)name.length() ^ calc(name) ^ value.referenceValue()) & 0x7FFFFFFFFFFFFFFFL;
    }

    @Override
    public String toString() {
        if (name.startsWith(" ")) {
            return "\\" + name.replaceAll(PATTERN, REPLACEMENT) + value.toString();
        }
        return name.replaceAll(PATTERN, REPLACEMENT) + value.toString();
    }

    public void toOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write(name.getBytes(StandardCharsets.UTF_8));
        value.toOutputStream(outputStream);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YAPIONVariable)) return false;
        YAPIONVariable variable = (YAPIONVariable) o;
        return Objects.equals(name, variable.name) &&
                Objects.equals(value, variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}