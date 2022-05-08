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

import yapion.pathn.impl.value.impl.ValueSet;

import java.util.ArrayList;
import java.util.List;

public class ValueSetSelector<R extends ValueSelectorBuilder<?, ?>> {

    private R parent;
    private List<Object> objectList = new ArrayList<>();

    protected ValueSetSelector(R parent) {
        this.parent = parent;
    }

    public <T> ValueSetSelector<R> add(T value) {
        objectList.add(value);
        return this;
    }

    public R build() {
        parent.add(new ValueSet<>(objectList));
        return parent;
    }
}
