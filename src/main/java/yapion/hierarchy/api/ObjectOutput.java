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
import yapion.hierarchy.output.*;
import yapion.utils.ReflectionsUtils;

import java.io.File;

public interface ObjectOutput {

    <T extends AbstractOutput> T toYAPION(T abstractOutput);

    @SneakyThrows
    default <T extends AbstractOutput & InstantiableOutput> T toYAPION(Class<T> clazz) {
        return toYAPION((T) ReflectionsUtils.constructObjectObjenesis(clazz));
    }

    <T extends AbstractOutput> T toJSON(T abstractOutput);

    @SneakyThrows
    default <T extends AbstractOutput & InstantiableOutput> T toJSON(Class<T> clazz) {
        return toJSON((T) ReflectionsUtils.constructObjectObjenesis(clazz));
    }

    <T extends AbstractOutput> T toJSONLossy(T abstractOutput);

    @SneakyThrows
    default <T extends AbstractOutput & InstantiableOutput> T toJSONLossy(Class<T> clazz) {
        return toJSONLossy((T) ReflectionsUtils.constructObjectObjenesis(clazz));
    }

    /// Copied from {@link yapion.YAPIONExtension}

    default String toYAPION() {
        return toYAPION(new StringOutput()).getResult();
    }

    default String toYAPION(boolean prettified) {
        return toYAPION(new StringOutput(prettified)).getResult();
    }

    @SneakyThrows
    default void toFile(File file) {
        toYAPION(new FileOutput(file)).close();
    }

    @SneakyThrows
    default void toFile(File file, boolean prettified) {
        toYAPION(new FileOutput(file, prettified)).close();
    }

    @SneakyThrows
    default void toGZIPFile(File file) {
        toYAPION(new FileGZIPOutput(file)).close();
    }

    default long toLength() {
        return toYAPION(new LengthOutput()).getLength();
    }

    default long toLength(boolean prettified) {
        LengthOutput lengthOutput = toYAPION(new LengthOutput());
        if (prettified) {
            return lengthOutput.getPrettifiedLength();
        } else {
            return lengthOutput.getLength();
        }
    }

}
