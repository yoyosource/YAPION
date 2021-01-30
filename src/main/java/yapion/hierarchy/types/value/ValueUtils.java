// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

public final class ValueUtils {

    private ValueUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String stringToUTFEscapedString(String s) {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            st.append(charToUTFEscape(s.charAt(i)));
        }
        return st.toString();
    }

    public static String charToUTFEscape(char c) {
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