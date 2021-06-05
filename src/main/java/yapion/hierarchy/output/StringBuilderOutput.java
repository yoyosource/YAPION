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

package yapion.hierarchy.output;

public class StringBuilderOutput extends AbstractOutput {

    protected final StringBuilder st;
    private final boolean prettified;

    public StringBuilderOutput(StringBuilder st) {
        this(st, false);
    }

    public StringBuilderOutput(StringBuilder st, boolean prettified) {
        this.st = st;
        this.prettified = prettified;
    }

    @Override
    protected void internalConsume(String s) {
        st.append(s);
    }

    public StringBuilder getStringBuilder() {
        return st;
    }

    public int length() {
        return st.length();
    }

    @Override
    public boolean prettified() {
        return prettified;
    }

}
