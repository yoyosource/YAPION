// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class YAPIONAny {

    public Type getType() {
        return Type.ANY;
    }

    public long referenceValue() {
        return getType().getReferenceValue() & 0x7FFFFFFFFFFFFFFFL;
    }

    public final int getDepth() {
        int depth = 0;
        YAPIONAny yapionAny = this;
        while ((yapionAny = yapionAny.getParent()) != null){
            depth++;
        }
        return depth;
    }

    public final String[] getPath() {
        List<String> path = new ArrayList<>();
        YAPIONAny last = this;
        YAPIONAny yapionAny = this;
        while ((yapionAny = yapionAny.getParent()) != null) {
            path.add(yapionAny.getPath(last));
            last = yapionAny;
        }
        return path.toArray(new String[0]);
    }

    public String getPath(YAPIONAny yapionAny) {
        return "";
    }

    private YAPIONAny parent = null;
    private boolean valuePresent = false;

    public final void setParent(YAPIONAny yapionAny) {
        if (valuePresent) {
            return;
        }
        this.parent = yapionAny;
        valuePresent = true;
    }

    public final void removeParent() {
        parent = null;
        valuePresent = false;
    }

    public final boolean isValuePresent() {
        return valuePresent;
    }

    public final YAPIONAny getParent() {
        return parent;
    }

    public void toOutputStream(OutputStream outputStream) throws IOException {}

}