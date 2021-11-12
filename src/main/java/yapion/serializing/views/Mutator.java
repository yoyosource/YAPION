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

/**
 * A mutator is used to change the value of a serialized object while deserializing.
 * The mutator itself is only called when the view allows it. That is why a Mutator is a special {@link View}.
 * To create a mutation you need to create a method inside your mutator class that has the name of the field to mutate,
 * has a {@link yapion.serializing.data.MutationContext} as parameter and either return {@code void} or another {@link yapion.serializing.data.MutationContext}.
 * When you want to change the field into which the value should be written the returned field should be inside the {@link View} constraints, otherwise nothing will be deserialized.
 */
public interface Mutator extends View {
}
