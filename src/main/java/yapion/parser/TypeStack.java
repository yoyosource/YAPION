// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.parser;

import yapion.exceptions.parser.YAPIONParserException;
import yapion.exceptions.utils.YAPIONArrayIndexOutOfBoundsException;
import yapion.hierarchy.types.YAPIONType;

import java.util.Iterator;
import java.util.LinkedList;

public class TypeStack {

    private final LinkedList<YAPIONType> stack = new LinkedList<>();
    private final LinkedList<Long> timeStack = new LinkedList<>();

    public void push(YAPIONType YAPIONType) {
        stack.push(YAPIONType);
        timeStack.push(System.nanoTime());
    }

    public YAPIONType pop(YAPIONType YAPIONType) {
        if (empty()) {
            throw new YAPIONArrayIndexOutOfBoundsException();
        }
        timeStack.pop();
        YAPIONType current = stack.pop();
        if (current != YAPIONType) {
            throw new YAPIONParserException();
        }
        return current;
    }

    public YAPIONType peek() {
        if (empty()) {
            throw new YAPIONArrayIndexOutOfBoundsException();
        }
        return stack.getFirst();
    }

    public long peekTime() {
        if (empty()) {
            throw new YAPIONArrayIndexOutOfBoundsException();
        }
        return System.nanoTime() - timeStack.getFirst();
    }

    private boolean empty() {
        return (stack.isEmpty());
    }

    public boolean isEmpty() {
        return (stack.isEmpty());
    }

    public boolean isNotEmpty() {
        return !(stack.isEmpty());
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
        return "TypeStack[" + st.toString() + "]";
    }

}