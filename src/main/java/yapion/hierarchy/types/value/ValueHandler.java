// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import java.util.Optional;

public interface ValueHandler<T> {

    String output(T t);

    Optional<T> preParse(String s);

    Optional<T> parse(String s);

    long referenceValue();

    default String charToUTFEscape(char c) {
        if (c < 0x20) {
            return "\\u" + (String.format("%04X", (short) c));
        } else if (c > 0x7F) {
            return "\\u" + (String.format("%04X", (short) c));
        } else if (c == '(' || c == ')') {
            return "\\" + c;
        } else {
            return c + "";
        }
    }

}