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
import yapion.hierarchy.output.flavours.*;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface ObjectOutput {

    default <T extends AbstractOutput> T output(T abstractOutput, Flavour flavour) {
        return abstractOutput;
    }

    default <T extends AbstractOutput> T toYAPION(T abstractOutput) {
        return output(abstractOutput, new YAPIONFlavour());
    }

    default <T extends AbstractOutput> T toJSON(T abstractOutput) {
        return output(abstractOutput, new YAPIONConvertedJSONFlavour());
    }

    default <T extends AbstractOutput> T toJSONLossy(T abstractOutput) {
        return output(abstractOutput, new JSONFlavour());
    }

    default <T extends AbstractOutput> T toThunderFile(T abstractOutput) {
        return output(abstractOutput, new ThunderFileFlavour());
    }

    default <T extends AbstractOutput> T toXML(T abstractOutput) {
        return output(abstractOutput, new XMLFlavour());
    }

    /**
     * Unwrap a {@link yapion.hierarchy.api.groups.YAPIONAnyType} to the parsable form used
     * by {@link yapion.parser.YAPIONParser#fromMap(Map)} or {@link yapion.parser.YAPIONParser#fromList(List)}.
     *
     * @param <U> the type of the unwrapped object
     * @return the unwrapped object
     */
    <U> U unwrap();

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
        return prettified ? lengthOutput.getPrettifiedLength() : lengthOutput.getLength();
    }

    @SneakyThrows
    default void toStream(OutputStream outputStream) {
        toYAPION(new StreamOutput(outputStream)).flush();
    }

    @SneakyThrows
    default void toStream(OutputStream outputStream, boolean prettified) {
        toYAPION(new StreamOutput(outputStream, prettified)).flush();
    }
}
