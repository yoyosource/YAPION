// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

public final class ValueUtils {

    private ValueUtils() {
        throw new IllegalStateException("Utility class");
    }

    public enum EscapeCharacters {

        NOTHING(),
        KEY('(', ')', '[', ']', '{', '}', '<', '>'),
        VALUE('(', ')');

        private char[] chars;

        EscapeCharacters(char... chars) {
            this.chars = chars;
        }

        private boolean contains(char c) {
            for (char check : chars) {
                if (check == c) return true;
            }
            return false;
        }

    }

    public static String stringToUTFEscapedString(String s, EscapeCharacters escapeCharacters) {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            st.append(charToUTFEscape(s.charAt(i), escapeCharacters));
        }
        return st.toString();
    }

    public static String charToUTFEscape(char c, EscapeCharacters escapeCharacters) {
        if (c < 0x20) {
            return "\\u" + (String.format("%04X", (short) c));
        } else if (c > 0x7F) {
            return "\\u" + (String.format("%04X", (short) c));
        } else if (escapeCharacters.contains(c)) {
            return "\\" + c;
        } else {
            return c + "";
        }
    }

}