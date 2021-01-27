// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.output;

public class StringOutput extends StringBuilderOutput {

    public StringOutput() {
        super(new StringBuilder());
    }

    @Override
    protected void internalConsume(String s) {
        super.internalConsume(s);
    }

    public String getResult() {
        return super.st.toString();
    }

}