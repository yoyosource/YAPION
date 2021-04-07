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

package yapion.hierarchy.types.utils;

import yapion.hierarchy.types.value.ValueHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValueHandlerList {

    private ValueHandler<?>[] valueHandlers;
    private int index = 0;

    public ValueHandlerList(int size) {
        valueHandlers = new ValueHandler[size];
    }

    public void add(ValueHandler<?> valueHandler) {
        valueHandlers[index] = valueHandler;
        index++;
    }

    public List<ValueHandler<?>> toArrayList() {
        return new ArrayList<>(Arrays.asList(valueHandlers));
    }

    public void toArrayList(List<ValueHandler<?>> valueHandlers) {
        valueHandlers.addAll(Arrays.asList(this.valueHandlers));
    }

}
