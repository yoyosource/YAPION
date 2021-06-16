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

package yapion.hierarchy.validators;

import yapion.annotations.api.DeprecationInfo;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.packet.YAPIONPacket;

import java.util.List;

@Deprecated
@DeprecationInfo(since = "0.25.3")
@FunctionalInterface
public interface ValidationHook {

    /**
     * Alter the validation so that the path of you {@link YAPIONAnyType}
     * can be deeper in the object. This is useful for validating
     * data in an {@link YAPIONPacket} as you don't want to generate
     * a complete validator for the whole Object, just for the data.
     *
     * @param path
     * @return the new path
     */
    List<String> path(List<String> path);

}
