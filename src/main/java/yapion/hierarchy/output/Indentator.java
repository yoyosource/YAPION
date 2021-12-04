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

import yapion.exceptions.YAPIONException;

public abstract class Indentator {

    private static final StringBuilder indentSpace = new StringBuilder().append(" ");
    private static final StringBuilder indentTab = new StringBuilder().append("\t");

    private static String growAndGetIndent(StringBuilder source, int indentLevel) {
        if (indentLevel < 0) {
            return "";
        }
        if (indentLevel > 4096) {
            indentLevel = 4096;
        }
        if (indentLevel > source.length()) {
            while (source.length() < indentLevel) {
                source.append(source);
            }
        }
        return source.substring(0, indentLevel);
    }

    public static final Indentator SINGLE_SPACE = new Indentator() {
        @Override
        public String indent(int indentLevel) {
            return Indentator.growAndGetIndent(indentSpace, indentLevel);
        }
    };

    public static final Indentator DEFAULT = new Indentator() {
        @Override
        public String indent(int indentLevel) {
            return Indentator.growAndGetIndent(indentSpace, indentLevel * 2);
        }
    };

    public static final Indentator QUAD_SPACE = new Indentator() {
        @Override
        public String indent(int indentLevel) {
            return Indentator.growAndGetIndent(indentSpace, indentLevel * 4);
        }
    };

    public static final Indentator SINGLE_TAB = new Indentator() {
        @Override
        public String indent(int indentLevel) {
            return Indentator.growAndGetIndent(indentTab, indentLevel);
        }
    };

    public static Indentator custom(int indentCount) {
        int indentMultiplier = Math.max(Math.min(indentCount, 16), 0);
        return new Indentator() {
            @Override
            public String indent(int indentLevel) {
                return Indentator.growAndGetIndent(indentSpace, indentLevel * indentMultiplier);
            }
        };
    }

    public static Indentator custom(int indentCount, char indent) {
        if (indent != ' ' && indent != '\t') throw new YAPIONException("Only Spaces and Tabs allowed");
        if (indent == ' ') {
            return custom(indentCount);
        }
        int indentMultiplier = Math.max(Math.min(indentCount, 16), 0);
        return new Indentator() {
            @Override
            public String indent(int indentLevel) {
                return Indentator.growAndGetIndent(indentTab, indentLevel * indentMultiplier);
            }
        };
    }

    public abstract String indent(int indentLevel);
}
