// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.utils;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.groups.YAPIONValueType;
import yapion.hierarchy.types.YAPIONObject;

import java.io.Closeable;
import java.util.*;

public class YAPIONTreeIterator implements Iterator<YAPIONAnyType>, Closeable {

    public enum YAPIONTreeIteratorOption {
        TRAVERSE_ALL,
        TRAVERSE_VALUE_TYPES,
        TRAVERSE_DATA_TYPES
    }

    private static final Comparator<YAPIONDataType> comparator = Comparator.comparingLong(YAPIONDataType::size);
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
        if (yapionDataType == null) return;
        List<YAPIONAnyType> allValues = yapionDataType.getAllValues();
        List<YAPIONDataType> dataTypeList = new ArrayList<>();
        int indexValue = 0;
        for (YAPIONAnyType yapionAnyType : allValues) {
            if (yapionAnyType instanceof YAPIONValueType) {
                if (option == YAPIONTreeIteratorOption.TRAVERSE_DATA_TYPES) continue;
                yapionAnyTypes.add(indexValue++, yapionAnyType);
            } else {
                dataTypeList.add((YAPIONDataType) yapionAnyType);
            }
        }
        if (dataTypeList.isEmpty()) return;
        if (dataTypeList.size() > 1) dataTypeList.sort(comparator);
        yapionAnyTypes.addAll(indexValue, dataTypeList);
    }

    @Override
    public boolean hasNext() {
        return !yapionAnyTypes.isEmpty();
    }

    public int available() {
        return yapionAnyTypes.size();
    }

    public int availableValues() {
        return (int) yapionAnyTypes.stream().filter(yapionAnyType -> yapionAnyType instanceof YAPIONValueType).count();
    }

    public int availableDatas() {
        return (int) yapionAnyTypes.stream().filter(yapionAnyType -> yapionAnyType instanceof YAPIONDataType).count();
    }

    // private int max = 0;

    @Override
    public YAPIONAnyType next() {
        while (true) {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            // max = Math.max(max, yapionAnyTypes.size());
            // System.out.println("MAX: " + max + "   VALUES: " + availableValues() + "   DATAS: " + availableDatas() + "   AVAILABLE: " + available());
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