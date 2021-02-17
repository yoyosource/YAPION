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

package yapion.hierarchy.api;

import yapion.hierarchy.output.AbstractOutput;

public interface ObjectOutput {

    StringBuilder indentString = new StringBuilder().append(" ");

    default String indent(int i) {
        if (i > 4096) {
            return indentString.toString();
        }
        if (i > indentString.length()) {
            while (indentString.length() < i) {
                indentString.append(indentString);
            }
        }
        return indentString.substring(0, i);
    }

    <T extends AbstractOutput> T toYAPION(T abstractOutput);

    <T extends AbstractOutput> T toJSON(T abstractOutput);

    <T extends AbstractOutput> T toJSONLossy(T abstractOutput);

}
