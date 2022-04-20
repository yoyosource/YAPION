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

import lombok.AccessLevel;
import lombok.Getter;
import yapion.exceptions.parser.YAPIONParserException;
import yapion.hierarchy.types.YAPIONType;

import java.util.Iterator;
import java.util.LinkedList;

@Getter(AccessLevel.PACKAGE)
public class TypeStack {

    private final LinkedList<YAPIONType> stack = new LinkedList<>();
    private final LinkedList<Long> readStack = new LinkedList<>();

    public void push(YAPIONType yapionType, long reads) {
        stack.push(yapionType);
        readStack.push(reads);
    }

    public YAPIONType pop(YAPIONType yapionType) {
        if (empty()) {
            throw new YAPIONParserException("Cannot close " + yapionType + " because it was not opened beforehand");
        }
        YAPIONType current = stack.pop();
        long reads = readStack.pop();
        if (current != yapionType) {
            if (yapionType == YAPIONType.ANY) {
                throw new YAPIONParserException("The opened type " + current + " at " + reads + " reads is not closed");
            }
            throw new YAPIONParserException("Cannot close " + yapionType + " because the last opened type is " + current + " at " + reads + " reads");
        }
        return current;
    }

    public YAPIONType peek() {
        if (empty()) {
            throw new YAPIONParserException("Cannot look on top of an empty stack");
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
