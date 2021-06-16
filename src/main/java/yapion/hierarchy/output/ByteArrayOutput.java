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

package yapion.hierarchy.output;

public class ByteArrayOutput extends AbstractArrayOutput<Byte> {

    @Override
    protected Byte[] convert(String s) {
        byte[] bts = bytes(s);
        Byte[] bytes = new Byte[bts.length];
        for (int i = 0; i < bts.length; i++) {
            bytes[i] = bts[i];
        }
        return bytes;
    }

    public byte[] bytes() {
        byte[] bytes = new byte[internalList.size()];
        for (int i = 0; i < internalList.size(); i++) {
            bytes[i] = internalList.get(i);
        }
        return bytes;
    }
}
