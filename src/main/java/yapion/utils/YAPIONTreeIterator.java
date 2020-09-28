// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typegroups.YAPIONDataType;
import yapion.hierarchy.typegroups.YAPIONValueType;
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

    private LinkedList<YAPIONAnyType> yapionAnyTypes = new LinkedList<>();
    private YAPIONTreeIteratorOption option;

    public YAPIONTreeIterator(YAPIONObject yapionObject) {
        this(yapionObject, YAPIONTreeIteratorOption.TRAVERSE_ALL);
    }

    public YAPIONTreeIterator(YAPIONObject yapionObject, YAPIONTreeIteratorOption option) {
        this.option = option;
        add(yapionObject);
    }

    private void add(YAPIONDataType yapionDataType) {
        List<YAPIONAnyType> allValues = yapionDataType.getAllValues();
        int indexValue = 0;
        int indexOther = 0;
        for (YAPIONAnyType yapionAnyType : allValues) {
            if (yapionAnyType instanceof YAPIONValueType) {
                if (option == YAPIONTreeIteratorOption.TRAVERSE_DATA_TYPES) continue;
                yapionAnyTypes.add(indexValue++, yapionAnyType);
            } else {
                yapionAnyTypes.add(indexOther, yapionAnyType);
            }
            indexOther++;
        }
    }

    @Override
    public boolean hasNext() {
        return !yapionAnyTypes.isEmpty();
    }

    @Override
    public YAPIONAnyType next() {
        while (true) {
            if (!hasNext()) return null;
            YAPIONAnyType yapionAnyType = yapionAnyTypes.removeFirst();
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
        yapionAnyTypes.clear();
    }

}