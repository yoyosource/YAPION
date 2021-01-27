// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.output;

public class StringBuilderOutput extends AbstractOutput {

    protected StringBuilder st;

    public StringBuilderOutput(StringBuilder st) {
        this.st = new StringBuilder();
    }

    @Override
    protected void internalConsume(String s) {
        st.append(s);
    }

    public int length() {
        return st.length();
    }

}