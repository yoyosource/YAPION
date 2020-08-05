// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.exceptions.value.YAPIONPointerException;
import yapion.hierarchy.Type;
import yapion.hierarchy.YAPIONAny;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class YAPIONPointer extends YAPIONAny {

    @Override
    public Type getType() {
        return Type.POINTER;
    }

    @Override
    public long referenceValue() {
        return getType().getReferenceValue();
    }

    @Override
    public void toOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write(toString().getBytes(StandardCharsets.UTF_8));
    }

    private long pointerID;
    private YAPIONObject yapionObject;

    public YAPIONPointer(YAPIONObject yapionObject) {
        this.pointerID = yapionObject.referenceValue() & 0x7FFFFFFFFFFFFFFFL;
        this.yapionObject = yapionObject;
    }

    public YAPIONPointer(long pointerID) {
        this.pointerID = pointerID;
    }

    public YAPIONPointer(String pointerID) {
        if (!pointerID.matches("[0-9A-F]{16}")) {
            throw new YAPIONPointerException("Invalid pointer id " + pointerID + " needs to be a HEX number");
        }
        this.pointerID = Long.parseLong(pointerID, 16);
    }

    public long getPointerID() {
        if (yapionObject != null) {
            pointerID = yapionObject.referenceValue() & 0x7FFFFFFFFFFFFFFFL;
        }
        return pointerID;
    }

    public String getPointerIDString() {
        return String.format("%016X", getPointerID());
    }

    @Override
    public String toString() {
        return "->" + getPointerIDString();
    }

}