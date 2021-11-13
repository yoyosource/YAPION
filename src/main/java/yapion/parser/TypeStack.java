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

package yapion.parser;

import yapion.exceptions.parser.YAPIONParserException;
import yapion.hierarchy.types.YAPIONType;

import java.util.Iterator;
import java.util.LinkedList;

public class TypeStack {

    private final LinkedList<YAPIONType> stack = new LinkedList<>();

    public void push(YAPIONType yapionType) {
        stack.push(yapionType);
    }

    public YAPIONType pop(YAPIONType yapionType) {
        if (empty()) {
            throw new YAPIONParserException("TypeStack is empty");
        }
        YAPIONType current = stack.pop();
        if (current != yapionType) {
            throw new YAPIONParserException("Current known type (" + current + ") is not expected specified type (" + yapionType + ")");
        }
        return current;
    }

    public YAPIONType peek() {
        if (empty()) {
            throw new YAPIONParserException("TypeStack is empty");
        }
        return stack.getFirst();
    }

    public void clear() {
        stack.clear();
    }

    private boolean empty() {
        return stack.isEmpty();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean isNotEmpty() {
        return !stack.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        Iterator<YAPIONType> iterator = stack.descendingIterator();
        boolean b = false;
        while (iterator.hasNext()) {
            if (b) {
                st.append(", ");
            }
            b = true;
            st.append(iterator.next().getName());
        }
        return "TypeStack[" + st + "]";
    }

}
