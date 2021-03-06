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
import lombok.SneakyThrows;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.ObjectOutput;
import yapion.hierarchy.api.ObjectPath;
import yapion.hierarchy.api.ObjectSearch;
import yapion.hierarchy.api.ObjectType;
import yapion.hierarchy.output.FileGZIPOutput;
import yapion.hierarchy.output.FileOutput;
import yapion.hierarchy.output.LengthOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONPath;
import yapion.parser.YAPIONParser;
import yapion.utils.ReferenceFunction;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@InternalAPI
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
        throw new YAPIONException("No Reference Value could be calculated");
    }

    public YAPIONAnyType internalCopy() {
        return YAPIONParser.parse("{" + toYAPION(new StringOutput()).getResult() + "}").getYAPIONAnyType("");
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
