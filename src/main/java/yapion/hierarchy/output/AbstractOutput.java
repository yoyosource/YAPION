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

import yapion.exceptions.YAPIONException;

import java.nio.charset.StandardCharsets;

public abstract class AbstractOutput {

    public final AbstractOutput consume(String s) {
        validateMethodCall();
        internalConsume(s);
        return this;
    }

    public final AbstractOutput consumePrettified(String s) {
        validateMethodCall();
        if (prettified()) {
            internalConsume(s);
        }
        return this;
    }

    protected abstract void internalConsume(String s);

    protected boolean prettified() {
        return false;
    }

    protected final byte[] bytes(String s) {
        return s.getBytes(StandardCharsets.UTF_16BE);
    }

    private void validateMethodCall() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length < 4) {
            throw new YAPIONException("Invalid calling class");
        }
        if (stackTraceElements[3].getClassName().startsWith("yapion.hierarchy.types.YAPION")) {
            return;
        }
        throw new YAPIONException("Invalid calling class");
    }

}
