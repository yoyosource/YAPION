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

package yapion.pathn.builder.value;

import yapion.pathn.builder.ValueSelectorBuilder;
import yapion.pathn.impl.value.ValueElement;
import yapion.pathn.impl.value.ValueOr;

import java.util.ArrayList;
import java.util.List;

public class OrValueImplSelector<R extends ValueSelectorBuilder<?, ?>> implements ValueSelectorBuilder<OrValueImplSelector<R>, R> {

    private R parent;
    private List<ValueElement> valueElementList = new ArrayList<>();

    public OrValueImplSelector(R parent) {
        this.parent = parent;
    }

    @Override
    public OrValueImplSelector<R> add(ValueElement valueElement) {
        valueElementList.add(valueElement);
        return this;
    }

    @Override
    public OrValueImplSelector<R> itself() {
        return this;
    }

    @Override
    public R build() {
        parent.add(new ValueOr(valueElementList));
        return parent;
    }
}
