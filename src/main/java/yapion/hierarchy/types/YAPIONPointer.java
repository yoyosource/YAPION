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
import yapion.exceptions.value.YAPIONPointerException;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.api.groups.YAPIONValueType;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.utils.ReferenceFunction;
import yapion.utils.ReferenceIDUtils;

import java.util.Objects;
import java.util.Optional;

import static yapion.utils.IdentifierUtils.POINTER_IDENTIFIER;

public class YAPIONPointer extends YAPIONValueType {

    private long pointerID;
    private ReferenceFunction referenceFunction;
    private YAPIONDataType<?, ?> object = null;

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

    @Override
    public <T extends AbstractOutput> T toThunderFile(T abstractOutput) {
        throw new UnsupportedOperationException("Thunder file format does not support pointers.");
    }

    @Override
    public <U> U unwrap() {
        throw new UnsupportedOperationException("Pointers can not be unwrapped.");
    }

    public <T extends YAPIONDataType<?, ?>> YAPIONPointer(T object) {
        referenceFunction = ReferenceIDUtils.REFERENCE_FUNCTION;
        this.object = object;
    }

    public <T extends YAPIONDataType<?, ?>> YAPIONPointer(T object, ReferenceFunction referenceFunction) {
        this.referenceFunction = referenceFunction;
        this.object = object;
    }

    public YAPIONPointer(String pointerID) {
        if (!pointerID.matches("[0-9A-Fa-f]{16}")) {
            throw new YAPIONPointerException("Invalid pointer id " + pointerID + " needs to be a HEX number");
        }
        this.pointerID = Long.parseLong(pointerID.toUpperCase(), 16);
    }

    public <T extends YAPIONDataType<?, ?>> void set(T object) {
        if (this.object != null || object == null) return;
        this.object = object;
    }

    public <T extends YAPIONDataType<?, ?>> T get() {
        return (T) object;
    }

    public long getPointerID() {
        if (object != null) {
            if (referenceFunction == null) referenceFunction = ReferenceIDUtils.REFERENCE_FUNCTION;
            pointerID = object.referenceValue(referenceFunction);
        }
        return pointerID & 0x7FFFFFFFFFFFFFFFL;
    }

    public String getPointerIDString() {
        return ReferenceIDUtils.format(getPointerID());
    }

    @Override
    public Optional<YAPIONSearchResult<?>> get(String key) {
        if (object == null) return Optional.empty();
        if (key.equals("@reference")) return Optional.of(new YAPIONSearchResult<>(object));
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

    public YAPIONPointer copy() {
        return (YAPIONPointer) internalCopy();
    }
}
