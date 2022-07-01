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

import yapion.annotations.api.InternalAPI;
import yapion.hierarchy.api.internal.InternalAdd;
import yapion.hierarchy.api.internal.InternalRemove;
import yapion.hierarchy.api.internal.InternalRetrieve;
import yapion.hierarchy.api.storage.AdvancedOperations;
import yapion.hierarchy.api.storage.CommentManipulation;
import yapion.hierarchy.api.storage.EndingCommentManipulation;

import java.util.ArrayList;
import java.util.List;

@InternalAPI
public abstract class YAPIONDataType<I extends YAPIONDataType<I, K>, K> extends YAPIONAnyType implements InternalAdd<I, K>, AdvancedOperations<I, K>, InternalRemove<I, K>, InternalRetrieve<K>, CommentManipulation<I>, EndingCommentManipulation<I> {

    private final List<String> endingComments = new ArrayList<>();

    public abstract int size();

    public abstract long deepSize();

    public int length() {
        return size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public abstract List<YAPIONAnyType> getAllValues();

    public boolean hasEndingComments() {
        return !endingComments.isEmpty();
    }

    public List<String> getEndingComments() {
        return endingComments;
    }

    @Override
    public I copy() {
        return (I) super.copy();
    }
}
