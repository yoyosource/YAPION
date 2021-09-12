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

package yapion.serializing.annotationproccessing.serializingdata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import yapion.annotations.object.YAPIONData;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@YAPIONData(cascading = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClassData {

    @EqualsAndHashCode.Include
    private String qualifiedName;

    private String simpleName;

    private boolean initNeeded;
    private boolean unknownSuper;
    private boolean serializerContextManager;
    private boolean serializerMethods;
    private boolean deserializerContextManager;
    private boolean deserializerMethods;
    private List<FieldData> fieldDataList = new ArrayList<>();

    public void setFieldType(String fieldName, String fieldType) {
        for (FieldData fieldData : fieldDataList) {
            if (fieldData.getFieldName().equals(fieldName)) {
                fieldData.setFieldType(fieldType);
                return;
            }
        }
    }
}
