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

public class MultiplexingOutput<T> implements Output<T> {

    private final Output<T>[] outputs;

    public Output<T>[] getWrapped() {
        return outputs;
    }

    public MultiplexingOutput(Output<T>... outputs) {
        this.outputs = outputs;
    }

    @Override
    public void consume(T value) {
        for (Output<T> output : outputs) {
            output.consume(value);
        }
    }

    @Override
    public void consumePrettified(T value) {
        for (Output<T> output : outputs) {
            output.consumePrettified(value);
        }
    }

    @Override
    public void consumePrettified(int indent) {
        for (Output<T> output : outputs) {
            output.consumePrettified(indent);
        }
    }
}
