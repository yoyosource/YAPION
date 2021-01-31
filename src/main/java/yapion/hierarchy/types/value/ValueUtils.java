// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

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
        byte[] bytes = s.getBytes(StandardCharsets.UTF_16BE);
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < bytes.length; i += 2) {
            st.append(charToUTFEscape((char)((bytes[i] << 8) | (bytes[i + 1] & 0xFF)), escapeCharacters));
        }
        return st.toString();
    }

    public static String charToUTFEscape(char c, EscapeCharacters escapeCharacters) {
        if (c < 0x20) {
            return "\\u" + (String.format("%04X", (short) c));
        } else if (c >= 0x7F && c < 0xA1) {
            return "\\u" + (String.format("%04X", (short) c));
        } else if (c > 0xFF) {
            return "\\u" + (String.format("%04X", (short) c));
        } else if (escapeCharacters.contains(c)) {
            return "\\" + c;
        } else {
            return c + "";
        }
    }

}