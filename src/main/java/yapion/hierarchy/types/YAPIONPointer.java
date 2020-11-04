// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.value.YAPIONPointerException;
import yapion.hierarchy.typegroups.YAPIONValueType;
import yapion.utils.ReferenceIDUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;

import static yapion.utils.IdentifierUtils.POINTER_IDENTIFIER;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONPointer extends YAPIONValueType {

    @Override
    public YAPIONType getType() {
        return YAPIONType.POINTER;
    }

    @Override
    public long referenceValue() {
        return getType().getReferenceValue();
    }

    @Override
    public String toYAPIONString() {
        return "->" + getPointerIDString();
    }

    @Override
    public String toYAPIONStringPrettified() {
        return toYAPIONString();
    }

    @Override
    public String toJSONString() {
        return "{\"" + POINTER_IDENTIFIER + "\":\"" + getPointerIDString() + "\"}";
    }

    @Override
    public String toLossyJSONString() {
        return "{\"" + POINTER_IDENTIFIER + "\":\"" + getPointerIDString() + "\"}";
    }

    @Override
    public void toOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write(bytes(toYAPIONString()));
    }

    @Override
    public void toOutputStreamPrettified(OutputStream outputStream) throws IOException {
        toOutputStream(outputStream);
    }

    private long pointerID;
    private YAPIONObject yapionObject;

    public YAPIONPointer(YAPIONObject yapionObject) {
        this.pointerID = yapionObject.referenceValue() & 0x7FFFFFFFFFFFFFFFL;
        this.yapionObject = yapionObject;
    }

    public YAPIONPointer(String pointerID) {
        if (!pointerID.matches("[0-9A-F]{16}")) {
            throw new YAPIONPointerException("Invalid pointer id " + pointerID + " needs to be a HEX number");
        }
        this.pointerID = Long.parseLong(pointerID, 16);
    }

    private void setYAPIONObject(YAPIONObject yapionObject) {
        this.yapionObject = yapionObject;
    }

    private YAPIONObject getYAPIONObject() {
        return yapionObject;
    }

    public long getPointerID() {
        if (yapionObject != null) {
            pointerID = yapionObject.referenceValue();
        }
        return pointerID & 0x7FFFFFFFFFFFFFFFL;
    }

    public String getPointerIDString() {
        return ReferenceIDUtils.format(getPointerID());
    }

    @Override
    public Optional<YAPIONSearchResult<?>> get(String key) {
        if (yapionObject == null) return Optional.empty();
        if (key.equals("@reference")) return Optional.of(new YAPIONSearchResult<>(yapionObject));
        return Optional.empty();
    }

    @Override
    public String toString() {
        return toYAPIONString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YAPIONPointer)) return false;
        YAPIONPointer that = (YAPIONPointer) o;
        return toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }
}