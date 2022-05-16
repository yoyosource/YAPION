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

package yapion.path.builder;

import yapion.path.impl.value.ValueElement;
import yapion.path.impl.value.ValueOr;

import java.util.ArrayList;
import java.util.List;

public class OrValueSelector<R extends ValueSelectorBuilder<?, ?>> implements ValueSelectorBuilder<OrValueSelector<R>, R> {

    private R parent;
    private List<ValueElement> valueElementList = new ArrayList<>();

    protected OrValueSelector(R parent) {
        this.parent = parent;
    }

    @Override
    public OrValueSelector<R> add(ValueElement valueElement) {
        valueElementList.add(valueElement);
        return this;
    }

    @Override
    public OrValueSelector<R> itself() {
        return this;
    }

    @Override
    public R build() {
        if (valueElementList.size() == 1) {
            parent.add(valueElementList.get(0));
        } else {
            parent.add(new ValueOr(valueElementList));
        }
        return parent;
    }
}