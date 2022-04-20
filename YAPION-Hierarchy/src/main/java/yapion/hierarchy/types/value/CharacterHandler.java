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

import yapion.hierarchy.types.YAPIONType;
import yapion.utils.IdentifierUtils;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

import static yapion.hierarchy.types.value.ValueUtils.EscapeCharacters.ARRAY_VALUE;
import static yapion.hierarchy.types.value.ValueUtils.charToUTFEscape;

public final class CharacterHandler implements ValueHandler<Character> {

    @Override
    public String type() {
        return Character.class.getTypeName();
    }

    @Override
    public String typeIdentifier() {
        return IdentifierUtils.CHAR_IDENTIFIER;
    }

    @Override
    public int index() {
        return 14;
    }

    @Override
    public boolean allowed(char c, int length) {
        switch (length) {
            case 0:
            case 2:
                return c == '\'';
            case 1:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String output(Character character, YAPIONType parent) {
        return "'" + charToUTFEscape(character, ARRAY_VALUE) + "'";
    }

    @Override
    public String outputJSON(Character character, YAPIONType parent) {
        return "\"" + charToUTFEscape(character, ARRAY_VALUE) + "\"";
    }

    @Override
    public MethodReturnValue<Character> preParse(String s) {
        return MethodReturnValue.empty();
    }

    @Override
    public MethodReturnValue<Character> parse(String s) {
        if (s.startsWith("'") && s.endsWith("'") && s.length() == 3) {
            return MethodReturnValue.of(s.charAt(1));
        }
        return MethodReturnValue.empty();
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.lang.Character");
    }
}
