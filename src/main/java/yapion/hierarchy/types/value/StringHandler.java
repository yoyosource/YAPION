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

import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

import static yapion.hierarchy.types.value.NumberSuffix.*;
import static yapion.hierarchy.types.value.ValueUtils.EscapeCharacters.VALUE;
import static yapion.hierarchy.types.value.ValueUtils.stringToUTFEscapedString;

public final class StringHandler implements ValueHandler<String> {

    @Override
    public boolean allowed(char c, int length) {
        return true;
    }

    @Override
    public String output(String s) {
        if (s.equals("\"") || (s.startsWith("\"") && s.endsWith("\""))) {
            return '"' + s + '"';
        }
        s = stringToUTFEscapedString(s, VALUE);
        if (s.equals("true") || s.equals("false") || s.equals("null")) {
            return '"' + s + '"';
        }

        if (s.matches(NUMBER_HEX)) {
            return '"' + s + '"';
        }
        if (s.matches(NUMBER_NORMAL)) {
            return '"' + s + '"';
        }
        if (s.matches(NUMBER_FLOAT)) {
            return '"' + s + '"';
        }

        if (NumberSuffix.tryValueAssemble(s, BYTE)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, SHORT)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, INTEGER)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, LONG)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, BIG_INTEGER)) {
            return '"' + s + '"';
        }

        if (NumberSuffix.tryValueAssemble(s, FLOAT)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, DOUBLE)) {
            return '"' + s + '"';
        }
        if (NumberSuffix.tryValueAssemble(s, BIG_DECIMAL)) {
            return '"' + s + '"';
        }

        return s;
    }

    @Override
    public MethodReturnValue<String> preParse(String s) {
        return MethodReturnValue.empty();
    }

    @Override
    public MethodReturnValue<String> parse(String s) {
        if (s.startsWith("'") && s.endsWith("'") && s.length() > 3) {
            return MethodReturnValue.of(s.substring(1, s.length() - 1));
        }
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() > 1) {
            return MethodReturnValue.of(s.substring(1, s.length() - 1));
        }
        return MethodReturnValue.of(s);
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.lang.String");
    }

}
