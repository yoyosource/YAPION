// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
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
        return ((long) name.length() ^ calc(name) ^ value.referenceValue()) & 0x7FFFFFFFFFFFFFFFL;
    }

    @Override
    public String toString() {
        StringOutput stringOutput = new StringOutput();
        toYAPION(stringOutput);
        return stringOutput.getResult();
    }

    @Override
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        if (name.startsWith(" ")) {
            abstractOutput.consume("\\");
        }
        abstractOutput.consume(name.replaceAll(PATTERN, REPLACEMENT));
        value.toYAPION(abstractOutput);
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toYAPIONPrettified(T abstractOutput) {
        if (name.startsWith(" ")) {
            abstractOutput.consume("\\");
        }
        abstractOutput.consume(name.replaceAll(PATTERN, REPLACEMENT));
        value.toYAPIONPrettified(abstractOutput);
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        abstractOutput.consume("\"").consume(name).consume("\":");
        value.toJSON(abstractOutput);
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
        abstractOutput.consume("\"").consume(name).consume("\":");
        value.toJSONLossy(abstractOutput);
        return abstractOutput;
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