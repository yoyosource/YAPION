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

package yapion.serializing.data;

import lombok.AllArgsConstructor;
import lombok.ToString;
import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
@ToString
public class DeserializationMutationContext {
    public final String fieldName;
    public final YAPIONAnyType value;

    public DeserializationMutationContext ignore() {
        return new DeserializationMutationContext(null, null);
    }

    public DeserializationMutationContext withFieldName(String fieldName) {
        return new DeserializationMutationContext(fieldName, value);
    }

    public DeserializationMutationContext withValue(YAPIONAnyType value) {
        return new DeserializationMutationContext(fieldName, value);
    }

    public <T extends YAPIONAnyType> DeserializationMutationContext valueMutator(Class<T> clazz, Consumer<T> valueMutator) {
        return valueMutator(clazz, t -> {
            valueMutator.accept(t);
            return t;
        });
    }

    public <T extends YAPIONAnyType> DeserializationMutationContext valueMutator(Class<T> clazz, Function<T, YAPIONAnyType> valueMutator) {
        if (value == null) return this;
        if (clazz.isInstance(value)) {
            YAPIONAnyType yapionAnyType = valueMutator.apply(clazz.cast(value));
            if (yapionAnyType == value) {
                return this;
            }
            return withValue(yapionAnyType);
        }
        return this;
    }
}
