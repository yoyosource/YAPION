// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.output;

public class StringBuilderPrettifiedOutput extends StringBuilderOutput {

    public StringBuilderPrettifiedOutput(StringBuilder st) {
        super(st);
    }

    @Override
    protected boolean prettified() {
        return true;
    }

}