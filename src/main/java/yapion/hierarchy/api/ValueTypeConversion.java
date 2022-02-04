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

package yapion.hierarchy.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public interface ValueTypeConversion {

    default Optional<String> asString() {
        return Optional.empty();
    }

    default Optional<Character> asCharacter() {
        return Optional.empty();
    }

    default Optional<Character> asChar() {
        return asCharacter();
    }

    default Optional<Boolean> asBoolean() {
        return Optional.empty();
    }

    default Optional<Byte> asByte() {
        return Optional.empty();
    }

    default Optional<Short> asShort() {
        return Optional.empty();
    }

    default Optional<Integer> asInteger() {
        return Optional.empty();
    }

    default Optional<Integer> asInt() {
        return asInteger();
    }

    default Optional<Long> asLong() {
        return Optional.empty();
    }

    default Optional<BigInteger> asBigInteger() {
        return Optional.empty();
    }

    default Optional<Float> asFloat() {
        return Optional.empty();
    }

    default Optional<Double> asDouble() {
        return Optional.empty();
    }

    default Optional<BigDecimal> asBigDecimal() {
        return Optional.empty();
    }
}
