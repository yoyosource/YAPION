// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

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