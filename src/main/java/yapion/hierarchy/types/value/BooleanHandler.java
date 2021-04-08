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

public final class BooleanHandler implements ValueHandler<Boolean> {

    @Override
    public boolean allowed(char c, int length) {
        switch (length) {
            case 0:
                return c == 't' || c == 'f';
            case 1:
                return c == 'r' || c == 'a';
            case 2:
                return c == 'u' || c == 'l';
            case 3:
                return c == 'e' || c == 's';
            case 4:
                return c == 'e';
            default:
                return false;
        }
    }

    @Override
    @SuppressWarnings("java:S5411")
    public String output(Boolean aBoolean) {
        if (aBoolean) {
            return "true";
        } else {
            return "false";
        }
    }

    @Override
    public MethodReturnValue<Boolean> preParse(String s) {
        return MethodReturnValue.empty();
    }

    @Override
    public MethodReturnValue<Boolean> parse(String s) {
        if (s.equals("true")) {
            return MethodReturnValue.of(true);
        }
        if (s.equals("false")) {
            return MethodReturnValue.of(false);
        }
        return MethodReturnValue.empty();
    }

    @Override
    public long referenceValue(ReferenceFunction referenceFunction) {
        return referenceFunction.stringToReferenceValue("java.lang.Boolean");
    }

}
