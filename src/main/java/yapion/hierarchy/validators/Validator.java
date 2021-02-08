// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.validators;

import yapion.YAPIONUtils;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.ObjectSearch;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Validator {

    private List<ValidatorVariable> variables = new ArrayList<>();
    private ValidationHook validationHook = path -> path;

    /**
     * JavaDoc found under {@link ValidationHook}
     *
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
     * in the {@link ValidatorVariable} is {@code null} or the
     * type is {@code null}.
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
     * {@link ValidatorVariable}'s given by the user.
     *
     * @param yapionObject the {@link YAPIONObject} to validate
     * @return {@code true} if the {@link YAPIONObject} was valid, {@code false} otherwise
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
        /**
         * Only validate the structure.
         */
        STRUCTURE,
        /**
         * Only validate the values.
         */
        VALUE,
        /**
         * Only validate the type of values.
         */
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

    /**
     * Creates an {@link Validator} for the given {@link YAPIONObject} with just the
     * {@link ValidationOption#STRUCTURE}.
     *
     * @param yapionObject the example {@link YAPIONObject}
     * @return the {@link Validator} for the given {@link YAPIONObject}
     */
    public static Validator validator(YAPIONObject yapionObject) {
        return validator(yapionObject, ValidationOption.STRUCTURE);
    }

    /**
     * Creates an {@link Validator} for the given {@link YAPIONObject} with the {@link ValidationOption}'s.
     *
     * @param yapionObject the example {@link YAPIONObject}
     * @param validationOptions what the validator should validate
     * @return the {@link Validator} for the given {@link YAPIONObject}
     */
    public static Validator validator(YAPIONObject yapionObject, ValidationOption... validationOptions) {
        Validator validator = new Validator();
        if (validationOptions == null) return validator;
        if (validationOptions.length == 0) return validator;

        ValidationTypes types = new ValidationTypes(validationOptions);
        YAPIONUtils.walk(yapionObject).forEach(s -> {
            ValidatorType validatorType = ValidatorType.getByYAPIONType(s.getType());
            String[] path = s.getPath().getPath();
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