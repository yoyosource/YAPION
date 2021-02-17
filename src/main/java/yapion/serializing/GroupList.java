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

package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

import java.util.ArrayList;
import java.util.Iterator;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
final class GroupList extends ArrayList<InternalSerializerGroup> {

    private boolean done = false;

    public void build() {
        done = true;
    }

    public boolean contains(String s) {
        if (!done) return false;
        if (s == null) return false;
        Iterator<InternalSerializerGroup> groupIterator = groupIterator();
        while (groupIterator.hasNext()) {
            if (s.startsWith(groupIterator.next().group())) return true;
        }
        return false;
    }

    private Iterator<InternalSerializerGroup> groupIterator() {
        return super.iterator();
    }

    @Override
    public boolean add(InternalSerializerGroup internalSerializerGroup) {
        if (done) throw new UnsupportedOperationException();
        return super.add(internalSerializerGroup);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
