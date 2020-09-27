// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import yapion.annotations.object.YAPIONData;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typegroups.YAPIONDataType;
import yapion.hierarchy.typegroups.YAPIONValueType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;

import java.io.Closeable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class YAPIONTreeIterator implements Iterator<YAPIONAnyType>, Closeable {

    public enum YAPIONTreeIteratorOption {
        TRAVERSE_ALL,
        TRAVERSE_VALUE_TYPES,
        TRAVERSE_DATA_TYPES
    }

    private LinkedList<YAPIONAnyType> yapionAnyTypeList = new LinkedList<>();
    private YAPIONTreeIteratorOption option = YAPIONTreeIteratorOption.TRAVERSE_ALL;

    public YAPIONTreeIterator(YAPIONObject yapionObject) {
        add(yapionObject);
    }

    public YAPIONTreeIterator(YAPIONObject yapionObject, YAPIONTreeIteratorOption option) {
        add(yapionObject);
        this.option = option;
    }

    private void add(YAPIONDataType yapionDataType) {
        List<YAPIONAnyType> allValues = yapionDataType.getAllValues();
        int indexValue = 0;
        int indexOther = 0;
        for (YAPIONAnyType yapionAnyType : allValues) {
            if (yapionAnyType instanceof YAPIONValueType) {
                if (option == YAPIONTreeIteratorOption.TRAVERSE_DATA_TYPES) continue;
                yapionAnyTypeList.add(indexValue++, yapionAnyType);
                indexOther++;
            } else {
                yapionAnyTypeList.add(indexOther++, yapionAnyType);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !yapionAnyTypeList.isEmpty();
    }

    @Override
    public YAPIONAnyType next() {
        while (true) {
            if (!hasNext()) return null;
            YAPIONAnyType yapionAnyType = yapionAnyTypeList.removeFirst();
            if (yapionAnyType instanceof YAPIONDataType) {
                add((YAPIONDataType) yapionAnyType);
                if (option != YAPIONTreeIteratorOption.TRAVERSE_VALUE_TYPES) return yapionAnyType;
            } else {
                return yapionAnyType;
            }
        }
    }

    @Override
    public void close() {
        yapionAnyTypeList.clear();
    }

}