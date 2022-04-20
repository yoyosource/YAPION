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

package yapion.serializing;

import lombok.ToString;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.serializing.YAPIONClassTypeException;

import java.util.HashMap;
import java.util.Map;

@ToString
public class TypeReMapper {

    Map<String, String> typeMappings = new HashMap<>();

    @InternalAPI
    public String remap(String type) {
        if (typeMappings.containsKey(type)) {
            return typeMappings.get(type);
        }
        String copiedType = type;
        while (!typeMappings.containsKey(type + ".")) {
            if (type.isEmpty()) {
                return copiedType;
            }
            if (!type.contains(".")) {
                return copiedType;
            }
            type = type.substring(0, type.lastIndexOf('.'));
        }
        if (!type.isEmpty() && typeMappings.containsKey(type + ".")) {
            return typeMappings.get(type + ".") + copiedType.substring(type.length() + 1);
        }
        return copiedType;
    }

    private void checkClassTypeValidity(String type) {
        if (type.endsWith(".")) {
            throw new YAPIONClassTypeException("The specified class '" + type + "' is no class");
        }
        if (type.substring(0, type.lastIndexOf('.')).contains("$")) {
            throw new YAPIONClassTypeException("The specified class '" + type + "' is no class");
        }
        if (type.equals("java.lang.Enum")) {
            throw new YAPIONClassTypeException("'java.lang.Enum' cannot be remapped");
        }
    }

    private void putClass(String fromType, String toType) {
        checkClassTypeValidity(fromType);
        checkClassTypeValidity(toType);
        typeMappings.put(fromType, toType);
    }

    /**
     * Add a class remapping from one {@link Class} type to another.
     *
     * @param fromType {@link Class} to map from
     * @param toType {@link Class} to map to
     */
    public TypeReMapper addClassMapping(String fromType, String toType) {
        putClass(fromType, toType);
        return this;
    }

    /**
     * Add a class remapping from one {@link Class} type to another.
     *
     * @param fromType {@link Class} to map from
     * @param toType {@link Class} to map to
     */
    public TypeReMapper addClassMapping(Class<?> fromType, String toType) {
        putClass(fromType.getTypeName(), toType);
        return this;
    }

    /**
     * Add a class remapping from one {@link Class} type to another.
     *
     * @param fromType {@link Class} to map from
     * @param toType {@link Class} to map to
     */
    public TypeReMapper addClassMapping(String fromType, Class<?> toType) {
        putClass(fromType, toType.getTypeName());
        return this;
    }

    /**
     * Add a class remapping from one {@link Class} type to another.
     *
     * @param fromType {@link Class} to map from
     * @param toType {@link Class} to map to
     */
    public TypeReMapper addClassMapping(Class<?> fromType, Class<?> toType) {
        putClass(fromType.getTypeName(), toType.getTypeName());
        return this;
    }

    private void checkPackageTypeValidity(String type) {
        if (!type.endsWith(".")) {
            throw new YAPIONClassTypeException("The specified package '" + type + "' is no package");
        }
        if (type.contains("$")) {
            throw new YAPIONClassTypeException("The specified package '" + type + "' is no package");
        }
    }

    private void putPackage(String fromType, String toType) {
        checkPackageTypeValidity(fromType);
        checkPackageTypeValidity(toType);
        typeMappings.put(fromType, toType);
    }

    /**
     * Add a type remapping from one {@link Package} to another.
     *
     * @param fromType {@link Package} to map from
     * @param toType {@link Package} to map to
     */
    public TypeReMapper addPackageMapping(String fromType, String toType) {
        putPackage(fromType, toType);
        return this;
    }

    /**
     * Add a type remapping from one {@link Package} to another.
     *
     * @param fromType {@link Package} to map from
     * @param toType {@link Package} to map to
     */
    public TypeReMapper addPackageMapping(Package fromType, String toType) {
        putPackage(fromType.getName() + ".", toType);
        return this;
    }

    /**
     * Add a type remapping from one {@link Package} to another.
     *
     * @param fromType {@link Package} to map from
     * @param toType {@link Package} to map to
     */
    public TypeReMapper addPackageMapping(String fromType, Package toType) {
        putPackage(fromType, toType.getName() + ".");
        return this;
    }

    /**
     * Add a type remapping from one {@link Package} to another.
     *
     * @param fromType {@link Package} to map from
     * @param toType {@link Package} to map to
     */
    public TypeReMapper addPackageMapping(Package fromType, Package toType) {
        putPackage(fromType.getName() + ".", toType.getName() + ".");
        return this;
    }

    /**
     * Clone this {@link TypeReMapper} and internal state.
     */
    public TypeReMapper copy() {
        TypeReMapper typeReMapper = new TypeReMapper();
        typeReMapper.typeMappings.putAll(typeMappings);
        return typeReMapper;
    }

    public static class FinalTypeReMapper extends TypeReMapper {

        public FinalTypeReMapper(TypeReMapper typeReMapper) {
            this.typeMappings = new HashMap<>(typeReMapper.typeMappings);
        }

        @Override
        public TypeReMapper addClassMapping(String fromType, String toType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeReMapper addClassMapping(Class<?> fromType, String toType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeReMapper addClassMapping(String fromType, Class<?> toType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeReMapper addClassMapping(Class<?> fromType, Class<?> toType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeReMapper addPackageMapping(String fromType, String toType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeReMapper addPackageMapping(Package fromType, String toType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeReMapper addPackageMapping(String fromType, Package toType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeReMapper addPackageMapping(Package fromType, Package toType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeReMapper copy() {
            return new FinalTypeReMapper(this);
        }
    }
}
