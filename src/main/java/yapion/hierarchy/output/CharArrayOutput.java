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

public class CharArrayOutput extends AbstractArrayOutput<Character> {

    @Override
    protected Character[] convert(String s) {
        char[] chs = s.toCharArray();
        Character[] chars = new Character[chs.length];
        for (int i = 0; i < chs.length; i++) {
            chars[i] = chs[i];
        }
        return chars;
    }

    public char[] chars() {
        char[] chars = new char[internalList.size()];
        for (int i = 0; i < internalList.size(); i++) {
            chars[i] = internalList.get(i);
        }
        return chars;
    }
}
