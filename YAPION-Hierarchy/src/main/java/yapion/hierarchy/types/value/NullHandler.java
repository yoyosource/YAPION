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
import yapion.utils.MethodReturnValue;
import yapion.utils.ReferenceFunction;

public final class NullHandler implements ValueHandler<Object> {

    @Override
    public String type() {
        return null;
    }

    @Override
    public String typeIdentifier() {
        return null;
    }

    @Override
    public int index() {
        return 5;
    }

    @Override
    public boolean allowed(char c, int length) {
        return switch (length) {
            case 0 -> c == 'n';
            case 1 -> c == 'u';
            case 2, 3 -> c == 'l';
            default -> false;
        };
    }

    @Override
    public String output(Object o, YAPIONType parent) {
        return "null";
    }

    @Override
    public String outputJSON(Object o, YAPIONType parent) {
        return "null";
    }

    @Override
    public MethodReturnValue<Object> preParse(String s) {
        if (s.equals("null")) {
            return MethodReturnValue.of(null);
        }
        return MethodReturnValue.empty();
    }

    @Override
    public MethodReturnValue<Object> parse(String s) {
        return MethodReturnValue.empty();
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("null");
    }
}
