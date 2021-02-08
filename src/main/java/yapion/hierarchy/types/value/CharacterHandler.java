// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import java.util.Optional;
import java.util.function.ToLongFunction;

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
    public long referenceValue(ToLongFunction<String> referenceFunction) {
        return referenceFunction.applyAsLong("java.lang.Character");
    }

}