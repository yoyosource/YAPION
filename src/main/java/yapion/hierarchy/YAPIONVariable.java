// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy;

import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static yapion.utils.ReferenceIDUtils.calc;

public class YAPIONVariable {

    private String name;
    private YAPIONAny value;

    private String pattern = "[({\\[<)}\\]>]";
    private String replacement = "\\\\$0";

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
            return "\\" + name.replaceAll(pattern, replacement) + value.toString();
        }
        return name.replaceAll(pattern, replacement) + value.toString();
    }

    public void toOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write(name.getBytes(StandardCharsets.UTF_8));
        value.toOutputStream(outputStream);
    }

}