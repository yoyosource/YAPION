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

import yapion.utils.ReferenceFunction;

import java.util.Optional;

import static yapion.hierarchy.types.value.ValueUtils.EscapeCharacters.VALUE;
import static yapion.hierarchy.types.value.ValueUtils.charToUTFEscape;

public class CharacterHandler implements ValueHandler<Character> {

    @Override
    public String output(Character character) {
        return "'" + charToUTFEscape(character, VALUE) + "'";
    }

    @Override
    public Optional<Character> preParse(String s) {
        return Optional.empty();
    }

    @Override
    public Optional<Character> parse(String s) {
        if (s.startsWith("'") && s.endsWith("'") && s.length() == 3) {
            return Optional.of(s.charAt(1));
        }
        return Optional.empty();
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.lang.Character");
    }

}
