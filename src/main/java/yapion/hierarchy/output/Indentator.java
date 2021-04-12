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

public abstract class Indentator {

    private static final StringBuilder indentString = new StringBuilder().append(" ");

    private static String growAndGetIndent(int indentLevel) {
        if (indentLevel < 0) {
            return "";
        }
        if (indentLevel > 4096) {
            return indentString.substring(0, 4096);
        }
        if (indentLevel > indentString.length()) {
            while (indentString.length() < indentLevel) {
                indentString.append(indentString);
            }
        }
        return indentString.substring(0, indentLevel);
    }

    public static final Indentator SINGLE_SPACE = new Indentator() {
        @Override
        public String indent(int indentLevel) {
            return Indentator.growAndGetIndent(indentLevel);
        }
    };

    public static final Indentator DEFAULT = new Indentator() {
        @Override
        public String indent(int indentLevel) {
            return Indentator.growAndGetIndent(indentLevel * 2);
        }
    };

    public static final Indentator QUAD_SPACE = new Indentator() {
        @Override
        public String indent(int indentLevel) {
            return Indentator.growAndGetIndent(indentLevel * 4);
        }
    };

    public abstract String indent(int indentLevel);

}
