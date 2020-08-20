// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy;

import yapion.annotations.serialize.YAPIONSave;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@YAPIONSave(context = "*")
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

    protected String getPath(YAPIONAny yapionAny) {
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

    public void toOutputStream(OutputStream outputStream) throws IOException {
        // This method should be overwritten by YAPIONArray, YAPIONMap, YAPIONObject, YAPIONPointer and YAPIONValue
    }

    public final Optional<YAPIONSearch<? extends YAPIONAny>> get(String... s) {
        Optional<YAPIONSearch<? extends YAPIONAny>> optional = Optional.of(new YAPIONSearch<>(this));
        for (int i = 0; i < s.length; i++) {
            if (!optional.isPresent()) return Optional.empty();
            optional = optional.get().value.get(s[i]);
        }
        return optional;
    }

    protected Optional<YAPIONSearch<? extends YAPIONAny>> get(String key) {
        return Optional.ofNullable(null);
    }

    public class YAPIONSearch<T> {

        public final T value;

        public YAPIONSearch(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "YAPIONSearch{" +
                    "value=" + value +
                    '}';
        }

    }

}