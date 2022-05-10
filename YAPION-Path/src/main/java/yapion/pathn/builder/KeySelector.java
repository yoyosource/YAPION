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

import yapion.pathn.impl.KeySelection;
import yapion.pathn.impl.value.ValueAnd;
import yapion.pathn.impl.value.ValueElement;

import java.util.ArrayList;
import java.util.List;

public class KeySelector<R extends SelectorBuilder<?, ?>> implements ValueSelectorBuilder<KeySelector<R>, R> {

    private R parent;
    private List<ValueElement> valueElementList = new ArrayList<>();

    public KeySelector(R parent) {
        this.parent = parent;
    }

    @Override
    public KeySelector<R> add(ValueElement valueElement) {
        valueElementList.add(valueElement);
        return this;
    }

    @Override
    public KeySelector<R> itself() {
        return this;
    }

    @Override
    public R build() {
        if (valueElementList.size() == 1) {
            parent.add(new KeySelection(valueElementList.get(0)));
        } else {
            parent.add(new KeySelection(new ValueAnd(valueElementList)));
        }
        return parent;
    }
}
