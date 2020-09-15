// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.typegroups;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.hierarchy.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public abstract class YAPIONAnyType extends YAPIONAnyClosure implements ObjectSearch, ObjectPath, ObjectType {

    public final int getDepth() {
        int depth = 0;
        YAPIONAnyType yapionAnyType = this;
        while ((yapionAnyType = yapionAnyType.getParent()) != null){
            depth++;
        }
        return depth;
    }

    public final String[] getPath() {
        List<String> path = new ArrayList<>();
        YAPIONAnyType last = this;
        YAPIONAnyType yapionAnyType = this;
        while ((yapionAnyType = yapionAnyType.getParent()) != null) {
            path.add(yapionAnyType.getPath(last));
            last = yapionAnyType;
        }
        return path.toArray(new String[0]);
    }

    @Override
    public String getPath(YAPIONAnyType yapionAnyType) {
        return "";
    }

    private YAPIONAnyType parent = null;
    private boolean valuePresent = false;
    private long parseTime = 0;

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

    private final void setParseTime(long time) {
        this.parseTime = time;
    }

    /**
     * @return parseTime in nanoseconds
     */
    public final long getParseTime() {
        return parseTime;
    }

    /**
     * @return parseTime in milliseconds as double
     */
    public final double getParseTimeMillis() {
        return parseTime / 1000000.0;
    }

    protected final boolean isValuePresent() {
        return valuePresent;
    }

    public final YAPIONAnyType getParent() {
        return parent;
    }

    public final Optional<ObjectSearch.YAPIONSearchResult<? extends YAPIONAnyType>> get(String... s) {
        Optional<ObjectSearch.YAPIONSearchResult<? extends YAPIONAnyType>> optional = Optional.of(new ObjectSearch.YAPIONSearchResult<>(this));
        for (int i = 0; i < s.length; i++) {
            if (!optional.isPresent()) return Optional.empty();
            optional = optional.get().value.get(s[i]);
        }
        return optional;
    }

    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(String key) {
        return Optional.empty();
    }

}