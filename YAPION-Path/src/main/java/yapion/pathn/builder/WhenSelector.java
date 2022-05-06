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

import yapion.pathn.PathElement;
import yapion.pathn.impl.When;

public class WhenSelector<R extends SelectorBuilder<?, ?>> implements SelectorBuilder<WhenSelector<R>, R> {

    private R parent;
    private PathElement pathElement = null;

    protected WhenSelector(R parent) {
        this.parent = parent;
    }

    @Override
    public WhenSelector<R> add(PathElement pathElement) {
        if (this.pathElement != null) {
            throw new IllegalStateException("You can only add one path element to a when selector.");
        }
        this.pathElement = pathElement;
        return this;
    }

    @Override
    public WhenSelector<R> itself() {
        return this;
    }

    @Override
    public R build() {
        parent.add(new When(pathElement));
        return parent;
    }
}
