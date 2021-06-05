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

package yapion.hierarchy.output;

import yapion.annotations.api.InternalAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayOutput<T> extends AbstractOutput implements InstantiableOutput {

    protected List<T> internalList = new ArrayList<>();

    @Override
    protected void internalConsume(String s) {
        internalList.addAll(Arrays.asList(convert(s)));
    }

    protected abstract T[] convert(String s);

    @InternalAPI
    public List<T> getInternalList() {
        return internalList;
    }
}
