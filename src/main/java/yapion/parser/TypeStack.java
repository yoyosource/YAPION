// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.parser;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.parser.YAPIONParserException;
import yapion.exceptions.utils.YAPIONArrayIndexOutOfBoundsException;
import yapion.hierarchy.Type;

import java.util.Iterator;
import java.util.LinkedList;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class TypeStack {

    private final LinkedList<Type> stack = new LinkedList<>();
    private final LinkedList<Long> timeStack = new LinkedList<>();

    public void push(Type type) {
        stack.push(type);
        timeStack.push(System.nanoTime());
    }

    public Type pop(Type type) {
        if (empty()) {
            throw new YAPIONArrayIndexOutOfBoundsException();
        }
        timeStack.pop();
        Type current = stack.pop();
        if (current != type) {
            throw new YAPIONParserException();
        }
        return current;
    }

    public Type peek() {
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
        Iterator<Type> iterator = stack.descendingIterator();
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