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
import yapion.annotations.api.YAPIONEveryType;
import yapion.exceptions.serializing.YAPIONClassTypeException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.storage.internal.InternalRemove;
import yapion.hierarchy.types.YAPIONValue;

public interface MapRemove<I, K> extends InternalRemove<I, K> {

    default <@YAPIONEveryType T> I remove(@NonNull T key) {
        if (key instanceof YAPIONAnyType) {
            return internalRemove((K) key);
        }
        if (!YAPIONValue.validType(key)) {
            throw new YAPIONClassTypeException();
        }
        return internalRemove((K) new YAPIONValue<>(key));
    }

    default <@YAPIONEveryType T> YAPIONAnyType removeAndGet(@NonNull T key) {
        if (key instanceof YAPIONAnyType) {
            return internalRemoveAndGet((K) key);
        }
        if (!YAPIONValue.validType(key)) {
            throw new YAPIONClassTypeException();
        }
        return internalRemoveAndGet((K) new YAPIONValue<>(key));
    }

}
