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

package yapion.hierarchy.api.groups;

import lombok.NonNull;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.*;
import yapion.hierarchy.types.YAPIONPath;
import yapion.utils.ReferenceFunction;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public abstract class YAPIONAnyType implements ObjectSearch, ObjectPath, ObjectType, ObjectOutput {

    // Reference Value System
    private AtomicReference<Long> referenceValue = new AtomicReference<>(null);
    private ReferenceFunction referenceFunction = null;

    protected final void cacheReferenceValue(long referenceValue) {
        this.referenceValue.set(referenceValue);
    }

    protected final long getReferenceValue() {
        return referenceValue.get();
    }

    protected final void discardReferenceValue() {
        referenceValue.set(null);
        YAPIONAnyType yapionAnyType = this;
        while ((yapionAnyType = yapionAnyType.getParent()) != null) {
            yapionAnyType.referenceValue.set(null);
        }
    }

    protected final boolean hasReferenceValue() {
        return referenceValue.get() != null;
    }

    @Override
    public long referenceValue(@NonNull ReferenceFunction referenceFunction) {
        if (hasReferenceValue() && this.referenceFunction == referenceFunction) {
            return getReferenceValue();
        }
        this.referenceFunction = referenceFunction;
        cacheReferenceValue(referenceValueProvider(referenceFunction) & 0x7FFFFFFFFFFFFFFFL);
        return getReferenceValue();
    }

    protected long referenceValueProvider(ReferenceFunction referenceFunction) {
        throw new YAPIONException();
    }

    /**
     * Optional API.
     */
    @OptionalAPI
    public YAPIONAnyType copy() {
        throw new UnsupportedOperationException();
    }

    // Depth System / Pretty YAPION String
    public final int getDepth() {
        int depth = 0;
        YAPIONAnyType yapionAnyType = this;
        while ((yapionAnyType = yapionAnyType.getParent()) != null) {
            depth++;
        }
        return depth;
    }

    public final String indent() {
        return indent((getDepth() + 1) * 2);
    }

    public final String reducedIndent() {
        return indent(getDepth() * 2);
    }

    // Path System
    @Override
    public final YAPIONPath getPath() {
        return new YAPIONPath(this);
    }

    @Override
    public String getPath(YAPIONAnyType yapionAnyType) {
        return "";
    }

    // Parent System
    private YAPIONAnyType parent = null;
    private boolean valuePresent = false;

    public final void setParent(YAPIONAnyType yapionAnyType) {
        if (valuePresent) {
            return;
        }
        this.parent = yapionAnyType;
        valuePresent = true;
    }

    public final void removeParent() {
        parent = null;
        valuePresent = false;
    }

    public final YAPIONAnyType getParent() {
        return parent;
    }

    public final boolean hasParent() {
        return valuePresent;
    }

    protected final boolean isValuePresent() {
        return valuePresent;
    }

    // Parse Time
    private long parseTime = 0;

    @SuppressWarnings("java:S1144")
    private void setParseTime(long time) {
        this.parseTime = time;
    }

    /**
     * @return parseTime in nanoseconds
     */
    public final long getParseTime() {
        return parseTime;
    }

    /**
     * @return parseTime in milliseconds as a double
     */
    public final double getParseTimeMillis() {
        return parseTime / 1000000.0;
    }

    /**
     * @return parseTime in milliseconds as a long
     */
    public final long getParseTimeMillisAsLong() {
        return (long) (parseTime / 1000000.0);
    }

    // Traverse System
    public final Optional<YAPIONSearchResult<?>> get(String... s) {
        if (s == null || s.length == 0) {
            return Optional.of(new YAPIONSearchResult<>(this));
        }
        Optional<YAPIONSearchResult<?>> optional = Optional.of(new YAPIONSearchResult<>(this));
        for (String value : s) {
            if (value == null) return Optional.empty();
            if (!optional.isPresent()) return Optional.empty();
            optional = optional.get().value.get(value);
        }
        return optional;
    }

    @Override
    public Optional<YAPIONSearchResult<?>> get(String key) {
        return Optional.empty();
    }

}
