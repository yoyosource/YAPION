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

package yapion.path;

import yapion.path.elements.AnyDeeperElement;
import yapion.path.elements.AnyElement;
import yapion.path.elements.Element;
import yapion.path.elements.RegexElement;

import java.util.ArrayList;
import java.util.List;

public class Selector {

    private List<PathElement> pathElementList = new ArrayList<>();

    public Selector select(String s) {
        pathElementList.add(new Element(s));
        return this;
    }

    public Selector select(int i) {
        return select(i + "");
    }

    public Selector selectRegex(String regex) {
        pathElementList.add(new RegexElement(regex));
        return this;
    }

    public Selector any() {
        pathElementList.add(new AnyElement());
        return this;
    }

    public Selector anyDeeper() {
        pathElementList.add(new AnyDeeperElement());
        return this;
    }

    public MultiSelector array() {
        return new MultiSelector(this);
    }

    public WhereSelector where() {
        return new WhereSelector(this);
    }

    public YAPIONPath build() {
        return new YAPIONPath(pathElementList);
    }

    public static class MultiSelector {

        private Selector selector;

        private List<String> elements = new ArrayList<>();

        private MultiSelector(Selector selector) {
            this.selector = selector;
        }

        public MultiSelector select(String s) {
            elements.add(s);
            return this;
        }

        public MultiSelector select(int i) {
            return select(i + "");
        }

        public Selector done() {
            selector.pathElementList.add(new Element(elements.toArray(new String[0])));
            return selector;
        }
    }

    public static class WhereSelector {

        private Selector selector;

        private WhereSelector(Selector selector) {
            this.selector = selector;
        }

        public Selector done() {
            return selector;
        }
    }
}
