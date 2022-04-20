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

package yapion.serializing.views;

import yapion.serializing.data.DeserializationMutationContext;
import yapion.serializing.data.SerializationContext;

/**
 * A mutator is used to change a value of a serialized object while deserializing or change it while serializing.
 * The mutator itself is only called when the view allows it. That is why a Mutator is a special {@link View}.
 * To create a mutation you need to create a method inside your mutator class that has the name of the field to mutate,
 * has a {@link DeserializationMutationContext} as parameter and either return {@code void} or another {@link DeserializationMutationContext}
 * for deserialization Mutation. Use {@link SerializationContext} as parameter for a mutation applied during serialization.
 * Methods with that parameter can return {@code void} or a {@link SerializationContext}.
 * When you want to change the field while deserialization the new field should be inside the {@link View} constraints used for deserialization.
 */
public interface Mutator extends View {
}
