// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.YAPIONSave;
import yapion.hierarchy.Type;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.parser.YAPIONParser;
import yapion.parser.YAPIONParserMapMapping;
import yapion.parser.YAPIONParserMapObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@YAPIONSave(context = "*")
public class YAPIONMap extends YAPIONAny {

    private final Map<YAPIONAny, YAPIONAny> variables = new LinkedHashMap<>();
    private final List<YAPIONParserMapMapping> mappingList = new ArrayList<>();
    private final Map<String, YAPIONAny> mappingVariables = new LinkedHashMap<>();

    @Override
    public Type getType() {
        return Type.MAP;
    }

    @Override
    public long referenceValue() {
        return getType().getReferenceValue();
    }

    @Override
    public String getPath(YAPIONAny yapionAny) {
        for (Map.Entry<YAPIONAny, YAPIONAny> entry : variables.entrySet()) {
            if (entry.getValue() == yapionAny) {
                return entry.getKey().toString();
            }
        }
        return "";
    }

    @Override
    public void toOutputStream(OutputStream outputStream) throws IOException {
        long id = 0;
        outputStream.write("<".getBytes(StandardCharsets.UTF_8));
        boolean b = false;
        for (Map.Entry<YAPIONAny, YAPIONAny> entry : variables.entrySet()) {
            String id1 = String.format("%01X", id++);
            String id2 = String.format("%01X", id++);

            if (b) {
                outputStream.write(",".getBytes(StandardCharsets.UTF_8));
            }
            b = true;

            outputStream.write((id1 + ":" + id2).getBytes(StandardCharsets.UTF_8));
            outputStream.write(",".getBytes(StandardCharsets.UTF_8));
            outputStream.write(("#" + id1 + "{@").getBytes(StandardCharsets.UTF_8));
            entry.getKey().toOutputStream(outputStream);
            outputStream.write("}".getBytes(StandardCharsets.UTF_8));
            outputStream.write(",".getBytes(StandardCharsets.UTF_8));
            outputStream.write(("#" + id2 + "{@").getBytes(StandardCharsets.UTF_8));
            entry.getValue().toOutputStream(outputStream);
            outputStream.write("}".getBytes(StandardCharsets.UTF_8));
        }
        outputStream.write(">".getBytes(StandardCharsets.UTF_8));
    }

    public void add(YAPIONAny key, YAPIONAny value) {
        variables.put(key, value);
    }

    public void add(YAPIONVariable variable) {
        add(new YAPIONValue<>(variable.getName()), variable.getValue());
    }

    public void add(YAPIONParserMapObject variable) {
        mappingVariables.put(variable.variable.getName().substring(1), variable.variable.getValue());
        variable.variable.getValue().setParent(this);
    }

    public void add(YAPIONParserMapMapping mapping) {
        mappingList.add(mapping);
    }

    public synchronized void finishMapping() {
        if (mappingVariables.isEmpty()) {
            return;
        }

        for (YAPIONParserMapMapping mapping : mappingList) {
            String[] strings = mapping.mapping.get().split(":");
            if (strings.length != 2) {
                continue;
            }

            YAPIONAny key = ((YAPIONObject)mappingVariables.get(strings[0])).getVariable("").getValue();
            YAPIONAny value = ((YAPIONObject)mappingVariables.get(strings[1])).getVariable("").getValue();
            variables.put(key, value);
        }

        mappingVariables.clear();
        mappingList.clear();
    }

    public YAPIONAny get(YAPIONAny key) {
        return variables.get(key);
    }

    public List<YAPIONAny> getKeys() {
        return new ArrayList<>(variables.keySet());
    }

    @Override
    protected Optional<YAPIONSearch<? extends YAPIONAny>> get(String key) {
        YAPIONVariable variable = YAPIONParser.parse("{" + key + "}").getVariable("");
        if (variable == null) return Optional.empty();
        YAPIONAny anyKey = variable.getValue();
        if (anyKey == null) return Optional.empty();
        YAPIONAny anyValue = get(anyKey);
        if (anyValue == null) return Optional.empty();
        return Optional.of(new YAPIONSearch<>(anyValue));
    }

    @Override
    public String toString() {
        long id = 0;

        StringBuilder st = new StringBuilder();
        st.append("<");
        boolean b = false;
        for (Map.Entry<YAPIONAny, YAPIONAny> entry : variables.entrySet()) {
            String id1 = String.format("%01X", id++);
            String id2 = String.format("%01X", id++);

            if (b) {
                st.append(",");
            }
            b = true;

            st.append(id1).append(":").append(id2);
            st.append(",");
            st.append("#").append(id1).append("{").append(entry.getKey().toString()).append("}");
            st.append(",");
            st.append("#").append(id2).append("{").append(entry.getValue().toString()).append("}");
        }
        st.append(">");
        return st.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YAPIONMap)) return false;
        YAPIONMap yapionMap = (YAPIONMap) o;
        return Objects.equals(variables, yapionMap.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables);
    }
}