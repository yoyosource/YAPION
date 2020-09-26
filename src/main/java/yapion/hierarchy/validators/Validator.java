// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.validators;

import test.Test;
import test.TestData;
import test.TestEnum;
import yapion.YAPIONUtils;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typeinterfaces.ObjectSearch;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;
import yapion.utils.YAPIONTreeIterator;

import java.nio.file.Files;
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

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new Test());
        System.out.println(yapionObject);
        validatorStructure(yapionObject);
    }

    public static Validator validatorStructure(YAPIONObject yapionObject) {
        YAPIONUtils.walk(yapionObject, YAPIONTreeIterator.YAPIONTreeIteratorOption.TRAVERSE_VALUE_TYPES).forEach(s -> {
            System.out.println(s + "   " + Arrays.toString(s.getPath()));
        });
        return null;
    }

    public static Validator validatorEquality(YAPIONObject yapionObject) {
        return null;
    }

}