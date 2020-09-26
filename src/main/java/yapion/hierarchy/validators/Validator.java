// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.validators;

import test.Test;
import yapion.YAPIONUtils;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.typeinterfaces.ObjectSearch;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.YAPIONSerializer;

import java.util.*;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public final class Validator {

    private List<ValidatorVariable> variables = new ArrayList<>();
    private ValidationHook validationHook = path -> path;

    /**
     * JavaDoc found under {@link ValidationHook}
     * @param validationHook the {@link ValidationHook} to use
     */
    public Validator setValidationHook(ValidationHook validationHook) {
        if (validationHook == null) return this;
        this.validationHook = validationHook;
        return this;
    }

    /**
     * Add a new {@link ValidatorVariable} which will validate a
     * small portion of any {@link YAPIONObject}.
     * This method throws an {@link YAPIONException} if the path
     * in the {@link ValidatorVariable} is {@see null} or the
     * type is {@see null}.
     *
     * @param variable the {@link ValidatorVariable} to add
     */
    public Validator add(ValidatorVariable variable) {
        if (variable == null) return this;
        if (variable.getPath() == null) throw new YAPIONException();
        if (variable.type == null) throw new YAPIONException();
        variable.path = validationHook.path(variable.path);
        variables.add(variable);
        return this;
    }

    /**
     * Add all {@link ValidatorVariable} of another {@link Validator}
     * to this {@link Validator}.
     *
     * @param validator the {@link Validator} to add
     */
    public Validator add(Validator validator) {
        if (validator == null) return this;
        for (ValidatorVariable validatorVariable : validator.variables) {
            add(validatorVariable);
        }
        return this;
    }

    /**
     * Validate a specified {@link YAPIONObject} with the specified
     * {@link ValidatorVariable's} given by the user.
     *
     * @param yapionObject the {@link YAPIONObject} to validate
     * @return {@see true} if the {@link YAPIONObject} was valid, {@see false} otherwise
     */
    public boolean validate(YAPIONObject yapionObject) {
        for (ValidatorVariable validatorVariable : variables) {
            Optional<ObjectSearch.YAPIONSearchResult<?>> searchResult = yapionObject.get(validatorVariable.getPath());
            if (!searchResult.isPresent()) return false;
            if (!validatorVariable.validate(searchResult.get().value)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Validator{" +
                "validationSteps=" + variables.size() +
                ", variables=" + variables +
                '}';
    }

    public enum ValidationOption {
        STRUCTURE,
        VALUE,
        TYPE
    }

    private static class ValidationTypes {

        private boolean structure = false;
        private boolean value = false;
        private boolean type = false;

        private ValidationTypes(ValidationOption... validationOptions) {
            for (ValidationOption validationOption : validationOptions) {
                switch (validationOption) {
                    case STRUCTURE:
                        structure = true;
                        break;
                    case VALUE:
                        value = true;
                        break;
                    case TYPE:
                        type = true;
                        break;
                }
            }
        }

    }

    public static Validator validator(YAPIONObject yapionObject) {
        return validator(yapionObject, ValidationOption.STRUCTURE);
    }

    public static Validator validator(YAPIONObject yapionObject, ValidationOption... validationOptions) {
        ValidationTypes types = new ValidationTypes(validationOptions);
        Validator validator = new Validator();
        YAPIONUtils.walk(yapionObject).forEach(s -> {
            ValidatorType validatorType = ValidatorType.getByYAPIONType(s.getType());
            String[] path = s.getPath();
            ValidatorVariable validatorVariable = null;
            if (s instanceof YAPIONValue) {
                Validation validation = null;
                if (types.value && !types.type) {
                    validation = o -> o.toString().equals(((YAPIONValue<?>) s).get().toString());
                }
                if (types.value && types.type) {
                    validation = o -> o.getClass().getTypeName().equals(((YAPIONValue<?>) s).getValueType()) && o.toString().equals(((YAPIONValue<?>) s).get().toString());
                }
                if (!types.value && types.type) {
                    validation = o -> o.getClass().getTypeName().equals(((YAPIONValue<?>) s).getValueType());
                }
                if (validation == null && types.structure) {
                    validatorVariable = new ValidatorVariable(path);
                } else if (validation != null) {
                    validatorVariable = new ValidatorVariable(validation, path);
                }
            } else if (types.structure) {
                validatorVariable = new ValidatorVariable(path);
            }
            if (validatorVariable == null) return;
            validator.add(validatorVariable.setType(validatorType));
        });
        return validator;
    }

}