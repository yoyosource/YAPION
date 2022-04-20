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

package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.internal.InternalRemove;

public interface ObjectRemove<I, K> extends InternalRemove<I, K> {

    default I remove(@NonNull K key) {
        return internalRemove(key);
    }

    default YAPIONAnyType removeAndGet(@NonNull K key) {
        return internalRemoveAndGet(key);
    }

    default I clear() {
        return internalClear();
    }
}
