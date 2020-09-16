// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.types;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSave;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.typegroups.YAPIONMappingType;
import yapion.parser.JSONMapper;
import yapion.parser.YAPIONParser;
import yapion.parser.YAPIONParserMapMapping;
import yapion.parser.YAPIONParserMapObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@YAPIONSave(context = "*")
@YAPIONLoad(context = "*")
public class YAPIONMap extends YAPIONMappingType {

    private final Map<YAPIONAnyType, YAPIONAnyType> variables = new LinkedHashMap<>();
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private final List<YAPIONParserMapMapping> mappingList = new ArrayList<>();
    @YAPIONSaveExclude(context = "*")
    @YAPIONLoadExclude(context = "*")
    private final Map<String, YAPIONAnyType> mappingVariables = new LinkedHashMap<>();

    @Override
    public YAPIONType getType() {
        return YAPIONType.MAP;
    }

    @Override
    public long referenceValue() {
        return getType().getReferenceValue();
    }

    @Override
    public String getPath(YAPIONAnyType yapionAnyType) {
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            if (entry.getValue() == yapionAnyType) {
                return entry.getKey().toYAPIONString();
            }
        }
        return "";
    }

    @Override
    public String toYAPIONString() {
        long id = 0;

        StringBuilder st = new StringBuilder();
        st.append("<");
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            String id1 = String.format("%01X", id++);
            String id2 = String.format("%01X", id++);

            st.append(id1).append(":").append(id2);
            st.append("#").append(id1).append(entry.getKey().toYAPIONString());
            st.append("#").append(id2).append(entry.getValue().toYAPIONString());
        }
        st.append(">");
        return st.toString();
    }

    @Override
    public String toJSONString() {
        YAPIONObject yapionObject = new YAPIONObject();
        YAPIONArray mapping = new YAPIONArray();
        yapionObject.add(JSONMapper.MAP_IDENTIFIER, mapping);

        long id = 0;
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            String id1 = String.format("%01X", id++);
            String id2 = String.format("%01X", id++);

            mapping.add(new YAPIONValue<>(id1 + ":" + id2));
            yapionObject.add("#" + id1, entry.getKey());
            yapionObject.add("#" + id2, entry.getValue());
        }
        return yapionObject.toJSONString();
    }

    @Override
    public String toLossyJSONString() {
        YAPIONObject yapionObject = new YAPIONObject();
        YAPIONArray mapping = new YAPIONArray();
        yapionObject.add(JSONMapper.MAP_IDENTIFIER, mapping);

        long id = 0;
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            String id1 = String.format("%01X", id++);
            String id2 = String.format("%01X", id++);

            mapping.add(new YAPIONValue<>(id1 + ":" + id2));
            yapionObject.add("#" + id1, entry.getKey());
            yapionObject.add("#" + id2, entry.getValue());
        }
        return yapionObject.toLossyJSONString();
    }

    @Override
    public void toOutputStream(OutputStream outputStream) throws IOException {
        long id = 0;
        outputStream.write("<".getBytes(StandardCharsets.UTF_8));
        boolean b = false;
        for (Map.Entry<YAPIONAnyType, YAPIONAnyType> entry : variables.entrySet()) {
            String id1 = String.format("%01X", id++);
            String id2 = String.format("%01X", id++);

            if (b) {
                outputStream.write(",".getBytes(StandardCharsets.UTF_8));
            }
            b = true;

            outputStream.write((id1 + ":" + id2).getBytes(StandardCharsets.UTF_8));
            outputStream.write(("#" + id1).getBytes(StandardCharsets.UTF_8));
            entry.getKey().toOutputStream(outputStream);
            outputStream.write(("#" + id2).getBytes(StandardCharsets.UTF_8));
            entry.getValue().toOutputStream(outputStream);
        }
        outputStream.write(">".getBytes(StandardCharsets.UTF_8));
    }

    public YAPIONMap add(YAPIONAnyType key, YAPIONAnyType value) {
        variables.put(key, value);
        return this;
    }

    public YAPIONMap add(YAPIONVariable variable) {
        return add(new YAPIONValue<>(variable.getName()), variable.getValue());
    }

    public YAPIONMap add(YAPIONParserMapObject variable) {
        mappingVariables.put(variable.variable.getName().substring(1), variable.variable.getValue());
        variable.variable.getValue().setParent(this);
        return this;
    }

    public YAPIONMap add(YAPIONParserMapMapping mapping) {
        mappingList.add(mapping);
        return this;
    }

    public int size() {
        return variables.size();
    }

    public int length() {
        return variables.size();
    }

    public boolean isEmpty() {
        return variables.isEmpty();
    }

    public synchronized YAPIONMap finishMapping() {
        if (mappingVariables.isEmpty()) {
            return this;
        }

        for (YAPIONParserMapMapping mapping : mappingList) {
            String[] strings = mapping.mapping.get().split(":");
            if (strings.length != 2) {
                continue;
            }

            variables.put(mappingVariables.get(strings[0]), mappingVariables.get(strings[1]));
        }

        mappingVariables.clear();
        mappingList.clear();
        return this;
    }

    public YAPIONAnyType get(YAPIONAnyType key) {
        return variables.get(key);
    }

    public List<YAPIONAnyType> getKeys() {
        return new ArrayList<>(variables.keySet());
    }

    @Override
    public Optional<YAPIONSearchResult<? extends YAPIONAnyType>> get(String key) {
        YAPIONVariable variable = YAPIONParser.parse("{" + key + "}").getVariable("");
        if (variable == null) return Optional.empty();
        YAPIONAnyType anyKey = variable.getValue();
        if (anyKey == null) return Optional.empty();
        YAPIONAnyType anyValue = get(anyKey);
        if (anyValue == null) return Optional.empty();
        return Optional.of(new YAPIONSearchResult<>(anyValue));
    }

    @Override
    public String toString() {
        return toYAPIONString();
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