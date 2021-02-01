// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.hierarchy.api.groups.YAPIONAnyType;

public interface ObjectRemove<I, K> {

    I remove(@NonNull K key);

    YAPIONAnyType removeAndGet(@NonNull K key);
    
}