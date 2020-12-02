// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.typeinterfaces;

import yapion.hierarchy.output.AbstractOutput;

public interface ObjectOutput {

    StringBuilder indent = new StringBuilder().append(" ");

    default String indent(int i) {
        if (i > 4096) {
            return indent.toString();
        }
        if (i > indent.length()) {
            while (indent.length() < i) {
                indent.append(indent);
            }
        }
        return indent.substring(0, i);
    }

    <T extends AbstractOutput> T toYAPION(T abstractOutput);

    <T extends AbstractOutput> T toJSON(T abstractOutput);

    <T extends AbstractOutput> T toJSONLossy(T abstractOutput);

}