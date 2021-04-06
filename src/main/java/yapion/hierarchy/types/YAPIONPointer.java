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

package yapion.hierarchy.types;

import lombok.NonNull;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.value.YAPIONPointerException;
import yapion.hierarchy.api.groups.YAPIONValueType;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.utils.ReferenceFunction;
import yapion.utils.ReferenceIDUtils;

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
    public long referenceValue(@NonNull ReferenceFunction referenceFunction) {
        return getType().getReferenceValue();
    }

    @Override
    public <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        abstractOutput.consume("->");
        abstractOutput.consume(getPointerIDString());
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSON(T abstractOutput) {
        abstractOutput.consume("{\"")
                .consume(POINTER_IDENTIFIER)
                .consume("\":\"")
                .consume(getPointerIDString())
                .consume("\"}");
        return abstractOutput;
    }

    @Override
    public <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
        return toJSON(abstractOutput);
    }

    private long pointerID;
    private ReferenceFunction referenceFunction;
    private YAPIONObject yapionObject = null;

    public YAPIONPointer(YAPIONObject yapionObject) {
        referenceFunction = ReferenceIDUtils.REFERENCE_FUNCTION;
        this.yapionObject = yapionObject;
    }

    public YAPIONPointer(YAPIONObject yapionObject, ReferenceFunction referenceFunction) {
        this.referenceFunction = referenceFunction;
        this.yapionObject = yapionObject;
    }

    public YAPIONPointer(String pointerID) {
        if (!pointerID.matches("[0-9A-F]{16}")) {
            throw new YAPIONPointerException("Invalid pointer id " + pointerID + " needs to be a HEX number");
        }
        this.pointerID = Long.parseLong(pointerID, 16);
    }

    public void setYAPIONObject(YAPIONObject yapionObject) {
        if (this.yapionObject != null || yapionObject == null) return;
        this.yapionObject = yapionObject;
    }

    public YAPIONObject getYAPIONObject() {
        return yapionObject;
    }

    public long getPointerID() {
        if (yapionObject != null) {
            if (referenceFunction == null) referenceFunction = ReferenceIDUtils.REFERENCE_FUNCTION;
            pointerID = yapionObject.referenceValue(referenceFunction);
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
        StringOutput stringOutput = new StringOutput();
        toYAPION(stringOutput);
        return stringOutput.getResult();
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
