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

package yapion.hierarchy.api.groups;

import yapion.hierarchy.api.storage.ObjectAdd;
import yapion.hierarchy.api.storage.ObjectAdvancedOperations;
import yapion.hierarchy.api.storage.ObjectRemove;
import yapion.hierarchy.api.storage.ObjectRetrieve;

import java.util.List;

public abstract class YAPIONDataType<I, K> extends YAPIONAnyType implements ObjectRetrieve<K>, ObjectAdd<I, K>, ObjectRemove<I, K>, ObjectAdvancedOperations<I, K> {

    public abstract int size();

    public abstract long deepSize();

    public abstract int length();

    public abstract boolean isEmpty();

    public abstract List<YAPIONAnyType> getAllValues();

}
