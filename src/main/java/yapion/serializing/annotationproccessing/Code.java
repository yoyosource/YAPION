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

package yapion.serializing.annotationproccessing;

import lombok.experimental.UtilityClass;

@UtilityClass
class Code {

    static final String[] defaultSerializer = new String[]{
            "package %PACKAGE%;",
            "",
            "import yapion.annotations.deserialize.YAPIONLoadExclude;",
            "import yapion.annotations.serialize.YAPIONOptimize;",
            "import yapion.annotations.serialize.YAPIONSaveExclude;",
            "import yapion.exceptions.utils.YAPIONReflectionException;",
            "import yapion.hierarchy.api.groups.YAPIONAnyType;",
            "import yapion.hierarchy.types.YAPIONObject;",
            "import yapion.serializing.ContextManager;",
            "import yapion.serializing.MethodManager;",
            "import yapion.serializing.InternalSerializer;",
            "import yapion.serializing.data.DeserializeData;",
            "import yapion.serializing.data.SerializeData;",
            "",
            "import java.lang.reflect.Field;",
            "import java.lang.reflect.Modifier;",
            "import java.util.Set;",
            "import java.util.HashSet;",
            "",
            "import static yapion.utils.ReflectionsUtils.getField;",
            "import static yapion.utils.ReflectionsUtils.getFields;",
            "import static yapion.utils.ReflectionsUtils.accessField;",
            "",
            "public class %NAME%Serializer implements InternalSerializer<%NAME%> {",
            "    private %NAME%Serializer() {}",
            "",
            "    private Set<Field> handledFields = null;",
            "    private Set<Field> sFields = null;",
            "    private Set<Field> dFields = null;",
            "",
            "%FIELDS%",
            "    @Override",
            "    public void init() {",
            "        handledFields = new HashSet<>();",
            "%FIELDS_INIT%",
            "        sFields = new HashSet<>();",
            "        dFields = new HashSet<>();",
            "        getFields(type()).forEach(f -> {",
            "            if (Modifier.isStatic(f.getModifiers())) return;",
            "            if (handledFields.contains(f)) return;",
            "            if (Modifier.isPrivate(f.getModifiers())) {",
            "                sFields.add(f);",
            "                dFields.add(f);",
            "                return;",
            "            }",
            "            if (Modifier.isFinal(f.getModifiers())) {",
            "                dFields.add(f);",
            "                return;",
            "            }",
            "            if (f.getDeclaringClass() != type()) {",
            "                sFields.add(f);",
            "                dFields.add(f);",
            "            }",
            "        });",
            "    }",
            "",
            "%FIELDS_LOAD%",
            "    @Override",
            "    public Class<%NAME%> type() {",
            "        return %NAME%.class;",
            "    }",
            "",
            "    @Override",
            "    public YAPIONAnyType serialize(SerializeData<%NAME%> serializeData) {",
            "        ContextManager contextManager = new ContextManager(serializeData.context);",
            "        MethodManager.preSerializationStep(serializeData.object, type(), contextManager);",
            "        YAPIONObject yapionObject = new YAPIONObject(type());",
            "%SERIALIZATION%",
            "        for (Field f : sFields) {",
            "            if (contextManager.is(f.getAnnotationsByType(YAPIONSaveExclude.class))) continue;",
            "            if (contextManager.is(f.getAnnotationsByType(YAPIONOptimize.class))) {",
            "                Object fObject = serializeData.getField(f);",
            "                if (fObject != null) yapionObject.add(f.getName(), serializeData.serialize(fObject));",
            "            } else {",
            "                serializeData.serialize(yapionObject, f);",
            "            }",
            "        }",
            "        MethodManager.postSerializationStep(serializeData.object, type(), contextManager);",
            "        return yapionObject;",
            "    }",
            "",
            "    @Override",
            "    public %NAME% deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {",
            "        ContextManager contextManager = new ContextManager(deserializeData.context);",
            "        %NAME% object = deserializeData.getInstanceByFactoryOrObjenesis(type());",
            "        MethodManager.preDeserializationStep(object, type(), contextManager);",
            "%DESERIALIZATION%",
            "        for (Field f : dFields) {",
            "            if (contextManager.is(f.getAnnotationsByType(YAPIONLoadExclude.class))) continue;",
            "            deserializeData.deserialize(object, f);",
            "        }",
            "        MethodManager.postDeserializationStep(object, type(), contextManager);",
            "        return object;",
            "    }",
            "}"
    };

}
