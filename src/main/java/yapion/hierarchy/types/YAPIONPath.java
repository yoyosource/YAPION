// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yapion.hierarchy.typegroups.YAPIONAnyType;

import java.util.Arrays;
import java.util.LinkedList;

@ToString
@EqualsAndHashCode
public final class YAPIONPath {

    private final String[] path;

    public YAPIONPath(YAPIONAnyType yapionAnyType) {
        LinkedList<String> path = new LinkedList<>();
        YAPIONAnyType last = yapionAnyType;
        while ((yapionAnyType = yapionAnyType.getParent()) != null) {
            path.addFirst(yapionAnyType.getPath(last));
            last = yapionAnyType;
        }
        this.path = path.toArray(new String[0]);
    }

    public String[] getPath() {
        return Arrays.copyOf(path, path.length);
    }

    public String join(String delimiter) {
        return String.join(delimiter, path);
    }

    public String join() {
        return join("");
    }

    public int depth() {
        return path.length;
    }

}