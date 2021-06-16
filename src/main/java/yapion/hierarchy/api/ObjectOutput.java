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

import lombok.SneakyThrows;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.InstantiableOutput;

public interface ObjectOutput {

    <T extends AbstractOutput> T toYAPION(T abstractOutput);

    @SneakyThrows
    default <T extends AbstractOutput & InstantiableOutput> T toYAPION(Class<T> clazz) {
        return toYAPION(clazz.newInstance());
    }

    <T extends AbstractOutput> T toJSON(T abstractOutput);

    @SneakyThrows
    default <T extends AbstractOutput & InstantiableOutput> T toJSON(Class<T> clazz) {
        return toJSON(clazz.newInstance());
    }

    <T extends AbstractOutput> T toJSONLossy(T abstractOutput);

    @SneakyThrows
    default <T extends AbstractOutput & InstantiableOutput> T toJSONLossy(Class<T> clazz) {
        return toJSONLossy(clazz.newInstance());
    }

}
