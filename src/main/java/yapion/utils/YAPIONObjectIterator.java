// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YAPIONObjectIterator implements Iterator<YAPIONAnyType>, Closeable {

    private List<YAPIONAnyType> yapionAnyTypeList = new ArrayList<>();

    public YAPIONObjectIterator(YAPIONObject yapionObject) {
        add(yapionObject);
    }

    private void add(YAPIONObject yapionObject) {
        yapionObject.getKeys().parallelStream().forEach(s -> yapionObject.add(yapionObject.getVariable(s)));
    }

    private void add(YAPIONMap yapionMap) {
        yapionMap.getKeys().parallelStream().forEach(s -> yapionAnyTypeList.add(s));
    }

    @Override
    public boolean hasNext() {
        return !yapionAnyTypeList.isEmpty();
    }

    @Override
    public YAPIONAnyType next() {
        if (!hasNext()) return null;
        YAPIONAnyType yapionAnyType = yapionAnyTypeList.get(0);
        if (yapionAnyType instanceof YAPIONObject) {
            add((YAPIONObject) yapionAnyType);
        }
        if (yapionAnyType instanceof YAPIONMap) {
            add((YAPIONMap) yapionAnyType);
        }
        return yapionAnyType;
    }

    @Override
    public void close() {
        yapionAnyTypeList.clear();
    }

}