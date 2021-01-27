// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.validators;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
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