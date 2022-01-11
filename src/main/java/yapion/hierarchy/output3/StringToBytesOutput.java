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

package yapion.hierarchy.output3;

import java.nio.charset.StandardCharsets;

public class StringToBytesOutput implements DecorativeOutput<String, Byte> {

    private Output<Byte> byteOutput;

    @Override
    public Output<Byte> getWrapped() {
        return byteOutput;
    }

    public StringToBytesOutput(Output<Byte> byteOutput) {
        this.byteOutput = byteOutput;
    }

    @Override
    public void consume(String value) {
        for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
            byteOutput.consume(b);
        }
    }

    @Override
    public void consumePrettified(String value) {
        for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
            byteOutput.consumePrettified(b);
        }
    }
}
