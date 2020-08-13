// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.parser;

import yapion.exceptions.parser.YAPIONParserException;
import yapion.exceptions.utils.YAPIONIOException;
import yapion.hierarchy.Type;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YAPIONParser {

    public static YAPIONObject parse(String s) {
        return new YAPIONParser(s).parse().result();
    }

    public static YAPIONObject parse(InputStream inputStream) {
        return new YAPIONParser(inputStream).parse().result();
    }

    private YAPIONObject result = null;
    private YAPIONAny currentObject = null;
    private String s;
    private InputStream inputStream;

    private int index = 0;

    private final TypeStack typeStack = new TypeStack();

    public YAPIONObject result() {
        return result;
    }

    public YAPIONParser(String s) {
        this.s = s;
    }

    public YAPIONParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public YAPIONParser parse() {
        try {
            parseInternal();
        } catch (YAPIONParserException e) {
            YAPIONParserException yapionParserException = new YAPIONParserException(((e.getMessage() != null ? e.getMessage() + "\n" : "\n") + generateErrorMessage()), e.getCause());
            yapionParserException.setStackTrace(e.getStackTrace());
            throw yapionParserException;
        } catch (IOException e) {
            YAPIONIOException yapionioException = new YAPIONIOException(e.getMessage(), e.getCause());
            yapionioException.setStackTrace(e.getStackTrace());
            throw yapionioException;
        }
        return this;
    }

    private String generateErrorMessage() {
        StringBuilder st = new StringBuilder();
        st.append(s);
        st.append("\n");
        for (int i = 0; i < s.length(); i++) {
            if (i == index) {
                st.append("^");
            } else {
                st.append(" ");
            }
        }
        return st.toString();
    }

    private boolean escaped = false;
    private StringBuilder current = new StringBuilder();
    private String key = "";

    private final List<YAPIONObject> yapionObjectList = new ArrayList<>();
    private final List<YAPIONPointer> yapionPointerList = new ArrayList<>();

    private void parseInternal() throws IOException {
        char lastChar;
        char c = '\u0000';

        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                lastChar = c;
                c = s.charAt(i);
                index = i;

                parseStep(lastChar, c);
            }
        } else if (inputStream != null) {
            int i = 0;
            while (inputStream.available() > 0) {
                lastChar = c;
                c = (char)inputStream.read();
                index = i++;

                parseStep(lastChar, c);
            }
        } else {
            throw new YAPIONParserException("null input");
        }

        parseFinish();
    }

    private void parseStep(char lastChar, char c) {
        //System.out.println(typeStack + " " + c);

        if (typeStack.isEmpty()) {
            initialObject(c);
            return;
        }

        if (typeStack.peek() == Type.POINTER) {
            parsePointer(c);
            return;
        } else if (typeStack.peek() == Type.OBJECT) {
            // HANDLED elsewhere
        } else if (typeStack.peek() == Type.ARRAY) {
            parseArray(c, lastChar);
            return;
        } else if (typeStack.peek() == Type.VALUE) {
            parseValue(c);
            return;
        } else if (typeStack.peek() == Type.MAP) {
            parseMap(c);
            return;
        }

        if (!escaped) {
            if (c == '{') {
                push(Type.OBJECT);
                YAPIONObject yapionObject = new YAPIONObject();
                yapionObjectList.add(yapionObject);
                add(new YAPIONVariable(key, yapionObject));
                currentObject = yapionObject;
                key = "";
                return;
            } else if (c == '}') {
                pop(Type.OBJECT);
                reset();
                currentObject = currentObject.getParent();
                return;
            }
            if (c == '[') {
                push(Type.ARRAY);
                YAPIONArray yapionArray = new YAPIONArray();
                add(new YAPIONVariable(key, yapionArray));
                currentObject = yapionArray;
                key = "";
                return;
            }
            if (c == '(') {
                push(Type.VALUE);
                return;
            }
            if (lastChar == '-' && c == '>') {
                current.deleteCharAt(current.length() - 1);
                push(Type.POINTER);
                return;
            }
            if (c == '<') {
                push(Type.MAP);
                YAPIONMap yapionMap = new YAPIONMap();
                add(new YAPIONVariable(key, yapionMap));
                currentObject = yapionMap;
                key = "";
                return;
            }
        }
        if (current.length() == 0 && c == ' ' && escaped) {
            current.append(c);
        }
        if (current.length() != 0 || (c != ' ' && c != '\n')) {
            current.append(c);
        }

        if (escaped) {
            escaped = false;
        }
        if (c == '\\') {
            escaped = true;
        }
    }

    private void parseFinish() {
        Map<Long, YAPIONObject> yapionObjectMap = new HashMap<>();
        for (YAPIONObject yapionObject : yapionObjectList) {
            yapionObjectMap.put(new YAPIONPointer(yapionObject).getPointerID(), yapionObject);
        }
        for (YAPIONPointer yapionPointer : yapionPointerList) {
            long id = yapionPointer.getPointerID();
            YAPIONObject yapionObject = yapionObjectMap.get(id);
            if (yapionObject == null) continue;
            try {
                Method method = yapionPointer.getClass().getDeclaredMethod("setYAPIONObject", YAPIONObject.class);
                method.setAccessible(true);
                method.invoke(yapionPointer, yapionObject);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void push(Type type) {
        typeStack.push(type);
        key = current.toString();
        current = new StringBuilder();
    }

    private void pop(Type type) {
        setParseTime();
        typeStack.pop(type);
    }

    private final void setParseTime() {
        try {
            Method method = currentObject.getClass().getSuperclass().getDeclaredMethod("setParseTime", long.class);
            method.setAccessible(true);
            method.invoke(currentObject, typeStack.peekTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        current = new StringBuilder();
        key = "";
    }

    private void initialObject(char c) {
        if (c == '{') {
            typeStack.push(Type.OBJECT);
            result = new YAPIONObject();
            yapionObjectList.add(result);
            currentObject = result;
        } else {
            throw new YAPIONParserException();
        }
    }

    private void add(YAPIONVariable yapionVariable) {
        if (currentObject instanceof YAPIONObject) {
            ((YAPIONObject) currentObject).add(yapionVariable);
        } else if (currentObject instanceof YAPIONMap) {
            if (yapionVariable.getName().startsWith("#")) {
                ((YAPIONMap) currentObject).add(new YAPIONParserMapObject(yapionVariable));
            } else {
                ((YAPIONMap) currentObject).add(yapionVariable);
            }
        } else if (currentObject instanceof YAPIONArray) {
            ((YAPIONArray) currentObject).add(yapionVariable.getValue());
        }
    }

    private void add(YAPIONValue<String> yapionValue) {
        if (currentObject instanceof YAPIONMap) {
            ((YAPIONMap) currentObject).add(new YAPIONParserMapMapping(yapionValue));
        }
    }

    private void parseValue(char c) {
        if (!escaped && c == ')') {
            pop(Type.VALUE);
            add(new YAPIONVariable(key, YAPIONValue.parseValue(current.toString())));
            reset();
        } else {
            current.append(c);
        }
    }

    private void parsePointer(char c) {
        current.append(c);
        if (current.length() == 16) {
            pop(Type.POINTER);
            YAPIONPointer yapionPointer = new YAPIONPointer(current.toString());
            yapionPointerList.add(yapionPointer);
            add(new YAPIONVariable(key, yapionPointer));
            reset();
        }
    }

    private void parseMap(char c) {
        if (c == '>') {
            pop(Type.MAP);
            if (current.length() != 0) {
                add(new YAPIONValue<>(current.toString()));
            }
            ((YAPIONMap)currentObject).finishMapping();
            currentObject = currentObject.getParent();
            reset();
            return;
        }
        if (!escaped && c == '{') {
            push(Type.OBJECT);
            YAPIONObject yapionObject = new YAPIONObject();
            yapionObjectList.add(yapionObject);
            add(new YAPIONVariable(key, yapionObject));
            currentObject = yapionObject;
            key = "";
            return;
        }

        if (c == ',') {
            if (current.length() != 0) {
                add(new YAPIONValue<>(current.toString()));
            }
            current = new StringBuilder();
            return;
        }
        current.append(c);
    }

    private void parseArray(char c, char lastChar) {
        key = "";
        if (!escaped) {
            if (c == ',') {
                if (current.length() != 0) {
                    add(new YAPIONVariable("", YAPIONValue.parseValue(current.toString())));
                }
                current = new StringBuilder();
                return;
            }
            if (c == ']') {
                if (current.length() != 0) {
                    add(new YAPIONVariable("", YAPIONValue.parseValue(current.toString())));
                }
                pop(Type.ARRAY);
                currentObject = currentObject.getParent();
                reset();
                return;
            }
            if (c == '{') {
                push(Type.OBJECT);
                YAPIONObject yapionObject = new YAPIONObject();
                yapionObjectList.add(yapionObject);
                add(new YAPIONVariable("", yapionObject));
                currentObject = yapionObject;
                return;
            }
            if (c == '[') {
                push(Type.ARRAY);
                YAPIONArray yapionArray = new YAPIONArray();
                add(new YAPIONVariable("", yapionArray));
                currentObject = yapionArray;
                return;
            }
            if (c == '<') {
                push(Type.MAP);
                YAPIONMap yapionMap = new YAPIONMap();
                add(new YAPIONVariable("", yapionMap));
                currentObject = yapionMap;
                return;
            }
            if (lastChar == '-' && c == '>') {
                push(Type.POINTER);
                return;
            }
        }
        if (current.length() == 0 && (c == ' ' || c == '\n') && !escaped) {
            return;
        }
        current.append(c);
    }

}