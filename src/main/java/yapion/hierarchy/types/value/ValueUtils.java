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

package yapion.hierarchy.types.value;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public final class ValueUtils {

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

    public static boolean startsWith(String s, EscapeCharacters escapeCharacters) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_16BE);
        if (bytes.length <= 1) {
            return false;
        }
        char c = (char) ((bytes[0] << 8) | (bytes[1] & 0xFF));
        return escapeCharacters.contains(c);
    }

    public static boolean contains(String s, EscapeCharacters escapeCharacters) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_16BE);
        for (int i = 0; i < bytes.length; i += 2) {
            char c = (char) ((bytes[i] << 8) | (bytes[i + 1] & 0xFF));
            if (escapeCharacters.contains(c)) {
                return true;
            }
        }
        return false;
    }

    public static String stringToUTFEscapedString(String s, EscapeCharacters escapeCharacters) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_16BE);
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < bytes.length; i += 2) {
            st.append(charToUTFEscape((char) ((bytes[i] << 8) | (bytes[i + 1] & 0xFF)), escapeCharacters));
        }
        return st.toString();
    }

    public static String charToUTFEscape(char c, EscapeCharacters escapeCharacters) {
        if (c == '\n') return "\\n";
        if (c == '\t') return "\\t";
        if (c == '\r') return "\\r";
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
