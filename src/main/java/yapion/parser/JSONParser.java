// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.parser;

import yapion.exceptions.YAPIONException;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.hierarchy.types.YAPIONVariable;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    private JSONParser() {
        throw new IllegalStateException();
    }

    static YAPIONObject parse(String json) {
        return parseObject(json.toCharArray());
    }

    private static YAPIONObject parseObject(char[] chars) {
        if (!(chars[0] == '{' && chars[chars.length - 1] == '}')) {
            throw new YAPIONException("No JSON Object");
        }
        YAPIONObject yapionObject = new YAPIONObject();

        boolean escaped = false;
        boolean inString = false;
        int lastSeperation = 0;

        int i = 1;
        while (i < chars.length) {
            if (chars[i] == '\\') {
                escaped = true;
                i++;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                inString = !inString;
                i++;
                continue;
            }

            if (!escaped && !inString && chars[i] == ':') {
                String name = getKey(chars, lastSeperation);
                while (chars[i + 1] == ' ' || chars[i + 1] == '\n' || chars[i + 1] == '\t') {
                    i++;
                }
                i++;
                if (chars[i] == '{') {
                    char[] object = getObject(chars, i);
                    i += object.length;
                    YAPIONObject subObject = parseObject(object);
                    yapionObject.add(new YAPIONVariable(name, subObject));
                    continue;
                }
                if (chars[i] == '[') {
                    char[] object = getArray(chars, i);
                    i += object.length;
                    if (!(object[0] == '[' && object[object.length - 1] == ']')) {
                        throw new YAPIONException("No JSON Array");
                    }
                    yapionObject.add(new YAPIONVariable(name, parseArray(object)));
                    continue;
                }

                yapionObject.add(new YAPIONVariable(name, YAPIONValue.parseValue(getValue(chars, i))));
                continue;
            }

            if (!escaped && !inString && chars[i] == ',') {
                lastSeperation = i;
            }

            escaped = false;
            i++;
        }
        return yapionObject;
    }

    private static YAPIONArray parseArray(char[] chars) {
        if (!(chars[0] == '[' && chars[chars.length - 1] == ']')) {
            throw new YAPIONException("No JSON Array");
        }
        boolean escaped = false;
        boolean inString = false;

        StringBuilder st = new StringBuilder();
        List<String> strings = new ArrayList<>();
        int bracket = 0;

        int i = 1;
        while (i < chars.length - 1) {
            if (chars[i] == '\\') {
                st.append(chars[i]);
                escaped = true;
                i++;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                st.append(chars[i]);
                inString = !inString;
                i++;
                continue;
            }

            if (!escaped && !inString && (chars[i] == '[' || chars[i] == '{')) {
                bracket++;
            }
            if (!escaped && !inString && (chars[i] == ']' || chars[i] == '}')) {
                bracket--;
            }

            if (!escaped && !inString && bracket == 0 && chars[i] == ',') {
                strings.add(st.toString());
                st = new StringBuilder();
                if (chars[i + 1] == ' ') {
                    i++;
                }
            } else {
                st.append(chars[i]);
            }

            escaped = false;
            i++;
        }
        if (st.length() != 0) {
            strings.add(st.toString());
        }

        YAPIONArray jsonArray = new YAPIONArray();

        for (String s : strings) {
            s = s.trim();
            if (s.startsWith("{") && s.endsWith("}")) {
                jsonArray.add(parseObject(s.toCharArray()));
            } else if (s.startsWith("[") && s.endsWith("]")) {
                jsonArray.add(parseArray(s.toCharArray()));
            } else {
                jsonArray.add(YAPIONValue.parseValue(s));
            }
        }

        return jsonArray;
    }

    private static String getKey(char[] chars, int index) {
        boolean escaped = false;

        boolean startOfString = false;
        StringBuilder st = new StringBuilder();

        for (int i = index; i < chars.length; i++) {
            if (chars[i] == '\\') {
                escaped = true;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                if (startOfString) {
                    return st.toString();
                } else {
                    startOfString = true;
                }
                continue;
            }

            if (startOfString) {
                st.append(chars[i]);
            }
            escaped = false;
        }
        return "";
    }

    private static String getValue(char[] chars, int index) {
        StringBuilder st = new StringBuilder();

        if (chars[index] == '\"') {
            boolean escaped = false;

            for (int i = index + 1; i < chars.length; i++) {
                if (chars[i] == '\\') {
                    st.append(chars[i]);
                    escaped = true;
                    continue;
                }

                if (!escaped && chars[i] == '\"') {
                    break;
                }
                st.append(chars[i]);
                escaped = false;
            }

            return "\"" + st.toString() + "\"";
        } else {
            for (int i = index; i < chars.length; i++) {
                if (chars[i] == ',' || chars[i] == '}' || chars[i] == ']' || chars[i] == '\n') {
                    break;
                }
                st.append(chars[i]);
            }

            return st.toString();
        }
    }

    private static char[] getArray(char[] chars, int index) {
        boolean escaped = false;
        boolean inString = false;
        int bracket = 0;

        StringBuilder st = new StringBuilder();

        for (int i = index; i < chars.length; i++) {
            if (chars[i] == '\\') {
                st.append(chars[i]);
                escaped = true;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                st.append(chars[i]);
                inString = !inString;
                continue;
            }

            st.append(chars[i]);
            if (!escaped && !inString && chars[i] == '[') {
                bracket++;
            }
            if (!escaped && !inString && chars[i] == ']') {
                bracket--;
            }

            escaped = false;
            if (bracket == 0) {
                return st.toString().toCharArray();
            }
        }
        return "[]".toCharArray();
    }

    private static char[] getObject(char[] chars, int index) {
        boolean escaped = false;
        boolean inString = false;
        int bracket = 0;

        StringBuilder st = new StringBuilder();

        for (int i = index; i < chars.length; i++) {
            if (chars[i] == '\\') {
                st.append(chars[i]);
                escaped = true;
                continue;
            }

            if (!escaped && chars[i] == '\"') {
                st.append(chars[i]);
                inString = !inString;
                continue;
            }

            st.append(chars[i]);
            if (!escaped && !inString && chars[i] == '{') {
                bracket++;
            }
            if (!escaped && !inString && chars[i] == '}') {
                bracket--;
            }

            escaped = false;
            if (bracket == 0) {
                return st.toString().toCharArray();
            }
        }
        return "{}".toCharArray();
    }

}