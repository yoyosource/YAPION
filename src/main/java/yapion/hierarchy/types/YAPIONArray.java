// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.exceptions.utils.YAPIONArrayIndexOutOfBoundsException;
import yapion.hierarchy.Type;
import yapion.hierarchy.YAPIONAny;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class YAPIONArray extends YAPIONAny {

    private List<YAPIONAny> array = new ArrayList<>();

    @Override
    public Type getType() {
        return Type.ARRAY;
    }

    @Override
    public long referenceValue() {
        return getType().getReferenceValue();
    }

    @Override
    public String getPath(YAPIONAny yapionAny) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == yapionAny) {
                return i + "";
            }
        }
        return "";
    }

    @Override
    public void toOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write("[".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < array.size(); i++) {
            if (i != 0) {
                outputStream.write(",".getBytes(StandardCharsets.UTF_8));
            }
            YAPIONAny yapionAny = array.get(i);
            if (yapionAny instanceof YAPIONValue) {
                String s = yapionAny.toString();
                outputStream.write(s.substring(1, s.length() - 1).getBytes(StandardCharsets.UTF_8));
            } else {
                array.get(i).toOutputStream(outputStream);
            }
        }
        outputStream.write("]".getBytes(StandardCharsets.UTF_8));
    }

    public int size() {
        return array.size();
    }

    public int length() {
        return array.size();
    }

    public YAPIONAny get(int index) {
        if (index < 0 || index >= length()) {
            throw new YAPIONArrayIndexOutOfBoundsException("Index " + index + " out of bounds for length " + length());
        }
        return array.get(index);
    }

    public void add(YAPIONAny yapionAny) {
        array.add(yapionAny);
        yapionAny.setParent(this);
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("[");
        boolean b = false;
        for (YAPIONAny yapionAny : array) {
            if (b) {
                st.append(",");
            }
            if (yapionAny.getType() == Type.VALUE) {
                st.append(yapionAny.toString(), 1, yapionAny.toString().length() - 1);
            } else {
                st.append(yapionAny.toString());
            }
            b = true;
        }
        st.append("]");
        return st.toString();
    }

}