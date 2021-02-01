// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.api;

import yapion.hierarchy.output.AbstractOutput;

public interface ObjectOutput {

    StringBuilder indentString = new StringBuilder().append(" ");

    default String indent(int i) {
        if (i > 4096) {
            return indentString.toString();
        }
        if (i > indentString.length()) {
            while (indentString.length() < i) {
                indentString.append(indentString);
            }
        }
        return indentString.substring(0, i);
    }

    <T extends AbstractOutput> T toYAPION(T abstractOutput);

    <T extends AbstractOutput> T toJSON(T abstractOutput);

    <T extends AbstractOutput> T toJSONLossy(T abstractOutput);

}