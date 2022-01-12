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

package yapion.hierarchy.output2;

import yapion.hierarchy.output.Indentator;

public class PrettifyOutput implements DecorativeOutput<String, String> {

    private final Output<String> output;

    private Indentator indentator = Indentator.DEFAULT;

    @Override
    public Output<String> getWrapped() {
        return output;
    }

    public PrettifyOutput(Output<String> output) {
        this.output = output;
    }

    public PrettifyOutput(Output<String> output, Indentator indentator) {
        this.output = output;
        this.indentator = indentator;
    }

    @Override
    public void consume(String value) {
        output.consume(value);
    }

    @Override
    public void consumePrettified(String value) {
        output.consume(value);
    }

    @Override
    public void consumePrettified(int indent) {
        output.consume(indentator.indent(indent));
    }
}
