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

package yapion.serializing.serializer.object.other;

import yapion.annotations.api.InternalAPI;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.utils.ClassUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@InternalAPI
public class ArraySerializer implements InternalSerializer<Object> {

    @Override
    public Class<?> type() {
        return null;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<Object> serializeData) {
        YAPIONArray yapionArray = new YAPIONArray();
        for (int i = 0; i < Array.getLength(serializeData.object); i++) {
            Object object = Array.get(serializeData.object, i);
            yapionArray.add(serializeData.serialize(object));
        }
        return yapionArray;
    }

    @Override
    public Object deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONArray yapionArray = (YAPIONArray) deserializeData.object;
        String s = deserializeData.getArrayType().replace("[", "").replace("]", "");
        if (s.isEmpty()) {
            s = getType(yapionArray);
        }

        Object object = Array.newInstance(ClassUtils.getClass(s), deepSize(yapionArray));
        for (int i = 0; i < yapionArray.length(); i++) {
            Array.set(object, i, deserializeData.deserialize(yapionArray.getYAPIONAnyType(i)));
        }
        return object;
    }

    private int[] deepSize(YAPIONArray yapionArray) {
        List<Integer> sizes = new ArrayList<>();
        YAPIONArray current = yapionArray;
        do {
            sizes.add(yapionArray.length());
            if (!current.stream().allMatch(yapionAnyType -> yapionAnyType instanceof YAPIONArray)) {
                current = null;
                continue;
            }
            current = yapionArray.stream().filter(yapionAnyType -> yapionAnyType instanceof YAPIONArray).map(YAPIONArray.class::cast).findFirst().orElse(null);
        } while (current != null);

        int[] ints = new int[sizes.size()];
        for (int i = 0; i < sizes.size(); i++) {
            ints[i] = sizes.get(i);
        }
        return ints;
    }

    @SuppressWarnings({"java:S128", "java:S1117", "unchecked"})
    private String getType(YAPIONArray yapionArray) {
        String type = null;
        boolean primitive = true;
        for (int i = 0; i < yapionArray.length(); i++) {
            YAPIONAnyType yapionAnyType = yapionArray.getYAPIONAnyType(i);
            switch (yapionAnyType.getType()) {
                case OBJECT:
                    primitive = false;
                    YAPIONObject yapionObject = (YAPIONObject) yapionAnyType;
                    if (yapionObject.getYAPIONAnyType(TYPE_IDENTIFIER) != null) {
                        type = ((YAPIONValue<String>) yapionObject.getYAPIONAnyType(TYPE_IDENTIFIER)).get();
                        break;
                    }
                case MAP:
                    primitive = false;
                    type = "java.lang.Object";
                    break;
                case VALUE:
                    YAPIONValue<?> yapionValue = (YAPIONValue<?>) yapionAnyType;
                    if (yapionValue.getValueType().equals("null")) {
                        primitive = false;
                        break;
                    }
                    if (type != null) {
                        if (!yapionValue.getValueType().equals(type)) {
                            type = "java.lang.Object";
                        }
                        break;
                    }
                    type = yapionValue.getValueType();
                    break;
                case ARRAY:
                    String s = getType((YAPIONArray) yapionAnyType);
                    if (type != null) {
                        if (ClassUtils.getBoxed(s).equals(type)) {
                            primitive = false;
                            break;
                        }
                        if (!s.equals(type)) {
                            type = "java.lang.Object";
                        }
                        break;
                    }
                    type = s;
                default:
                    break;
            }
        }
        if (type == null) type = "java.lang.Object";
        return primitive ? ClassUtils.getPrimitive(type) : type;
    }
}
