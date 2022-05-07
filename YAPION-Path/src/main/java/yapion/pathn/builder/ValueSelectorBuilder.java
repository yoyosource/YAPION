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

package yapion.pathn.builder;

import yapion.pathn.builder.value.AndValueImplSelector;
import yapion.pathn.builder.value.OrValueImplSelector;
import yapion.pathn.impl.value.ValueElement;
import yapion.pathn.impl.value.impl.ValueEquals;

public interface ValueSelectorBuilder<I extends ValueSelectorBuilder<I, B>, B> {

    I add(ValueElement valueElement);

    I itself();

    default <T> I equalTo(T value) {
        return add(new ValueEquals<>(value));
    }

    default AndValueImplSelector<I> and() {
        return new AndValueImplSelector<>(itself());
    }
    default OrValueImplSelector<I> or() {
        return new OrValueImplSelector<>(itself());
    }

    B build();
}
