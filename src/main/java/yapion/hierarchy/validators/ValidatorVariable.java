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

package yapion.hierarchy.validators;

import yapion.annotations.api.DeprecationInfo;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
@DeprecationInfo(since = "0.25.3")
public class ValidatorVariable {

    List<String> path;
    Validation validation;
    ValidatorType type;

    /**
     * Create an {@link ValidatorVariable} used by the {@link Validator}.
     * This constructor is used when you want to set the {@link ValidatorType}
     * of this {@link ValidatorVariable} to {@link ValidatorType#VALUE}.
     * If the type is {@link ValidatorType#VALUE} than the {@link Validation}
     * will be executed.
     *
     * @param validation a implementation specific {@link Validation}
     * @param path the path to go along to find the {@link YAPIONAnyType} that should be validated
     */
    public ValidatorVariable(Validation validation, String... path) {
        this.validation = validation;
        this.path = new ArrayList<>(Arrays.asList(path));
    }

    /**
     * Create an {@link ValidatorVariable} used by the {@link Validator}.
     * This constructor is used when you want to just check the type
     * of the {@link YAPIONAnyType} denoted by the path.
     *
     * @param path the path to go along to find the {@link YAPIONAnyType} that should be validated
     */
    public ValidatorVariable(String... path) {
        this.path = new ArrayList<>(Arrays.asList(path));
    }

    /**
     * @return the path of the searched {@link YAPIONAnyType}
     */
    String[] getPath() {
        return path.toArray(new String[0]);
    }

    /**
     * @param type set the {@link ValidatorType} for this {@link ValidatorVariable}
     */
    public ValidatorVariable setType(ValidatorType type) {
        if (this.type != null) return this;
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "ValidatorVariable{" +
                "path=" + path +
                ", type=" + type +
                ", hasValidation=" + (validation != null) +
                '}';
    }

    boolean validate(YAPIONAnyType yapionAnyType) {
        if (type == null) return false;
        if (yapionAnyType.getType() != type.getType()) return false;
        if (type != ValidatorType.VALUE) return true;
        if (validation == null) return true;
        Object o = ((YAPIONValue<?>) yapionAnyType).get();
        return validation.validate(o);
    }

}
