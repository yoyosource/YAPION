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
            "%REFLECTION_FIELDS%",
            "%FIELDS%",
            "    @Override",
            "    public void init() {",
            "%FIELDS_INIT_CALL%",
            "%REFLECTION%",
            "    }",
            "",
            "%FIELDS_INIT%",
            "%FIELDS_LOAD%",
            "    @Override",
            "    public Class<%NAME%> type() {",
            "        return %NAME%.class;",
            "    }",
            "",
            "    @Override",
            "    public YAPIONAnyType serialize(SerializeData<%NAME%> serializeData) {",
            "        ContextManager contextManager = new ContextManager(serializeData.context);",
            "%PRE_SERIALIZATION%",
            "        YAPIONObject yapionObject = new YAPIONObject(type());",
            "%SERIALIZATION%",
            "%SERIALIZATION_FIELDS%",
            "%POST_SERIALIZATION%",
            "        return yapionObject;",
            "    }",
            "",
            "    @Override",
            "    public %NAME% deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {",
            "        ContextManager contextManager = new ContextManager(deserializeData.context);",
            "        %NAME% object = deserializeData.getInstanceByFactoryOrObjenesis(type());",
            "%PRE_DESERIALIZATION%",
            "%DESERIALIZATION%",
            "%DESERIALIZATION_FIELDS%",
            "%POST_DESERIALIZATION%",
            "        return object;",
            "    }",
            "}"
    };

}
