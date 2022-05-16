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

import yapion.path.PathElement;
import yapion.path.impl.AllOf;

import java.util.ArrayList;
import java.util.List;

public class AllOfSelector<R extends SelectorBuilder<?, ?>> implements SelectorBuilder<AllOfSelector<R>, R> {

    private R parent;
    private List<PathElement> pathElementList = new ArrayList<>();

    protected AllOfSelector(R parent) {
        this.parent = parent;
    }

    @Override
    public AllOfSelector<R> add(PathElement pathElement) {
        pathElementList.add(pathElement);
        return this;
    }

    @Override
    public AllOfSelector<R> itself() {
        return this;
    }

    @Override
    public R build() {
        parent.add(new AllOf(pathElementList));
        return parent;
    }
}

