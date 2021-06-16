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

import lombok.Getter;
import yapion.annotations.api.InternalAPI;

import java.nio.charset.StandardCharsets;

public abstract class AbstractOutput {

    @Getter
    protected Indentator indentator = Indentator.DEFAULT;

    public void setIndentator(Indentator indentator) {
        this.indentator = indentator;
    }

    public static <T extends AbstractOutput> T setIndentator(T abstractOutput, Indentator indentator) {
        abstractOutput.indentator = indentator;
        return abstractOutput;
    }

    @InternalAPI
    public final AbstractOutput consume(String s) {
        internalConsume(s);
        return this;
    }

    @InternalAPI
    public final AbstractOutput consumeIndent(int indentLevel) {
        return consumePrettified(indentator.indent(indentLevel));
    }

    @InternalAPI
    public final AbstractOutput consumePrettified(String s) {
        if (prettified() && internalConsumePrettified(s)) {
            internalConsume(s);
        }
        return this;
    }

    @SuppressWarnings("java:S1172")
    protected boolean internalConsumePrettified(String s) {
        return true;
    }

    protected abstract void internalConsume(String s);

    public boolean prettified() {
        return false;
    }

    protected final byte[] bytes(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

}
