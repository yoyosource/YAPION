// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.hierarchy.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public abstract class YAPIONAny implements Output, ObjectSearch, ObjectPath, YAPIONType {

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

    @Override
    public String getPath(YAPIONAny yapionAny) {
        return "";
    }

    private YAPIONAny parent = null;
    private boolean valuePresent = false;
    private long parseTime = 0;

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

    public final YAPIONAny getParent() {
        return parent;
    }

    public final Optional<ObjectSearch.YAPIONSearchResult<? extends YAPIONAny>> get(String... s) {
        Optional<ObjectSearch.YAPIONSearchResult<? extends YAPIONAny>> optional = Optional.of(new ObjectSearch.YAPIONSearchResult<>(this));
        for (int i = 0; i < s.length; i++) {
            if (!optional.isPresent()) return Optional.empty();
            optional = optional.get().value.get(s[i]);
        }
        return optional;
    }

    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAny>> get(String key) {
        return Optional.empty();
    }

}