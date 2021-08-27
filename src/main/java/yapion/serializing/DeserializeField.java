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

package yapion.serializing;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import yapion.hierarchy.api.groups.YAPIONAnyType;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public final class DeserializeField {

    /**
     * The fieldName that could not be serialized.
     */
    public final String fieldName;

    /**
     * The value that should have been serialized into the fieldName.
     */
    @EqualsAndHashCode.Exclude
    public final YAPIONAnyType ignoredValue;
}
