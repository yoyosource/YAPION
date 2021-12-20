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

package yapion.path.elements;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.path.PathContext;
import yapion.path.PathElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class RegexElement implements PathElement {

    private Pattern pattern;

    public RegexElement(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public RegexElement(Pattern regex) {
        this.pattern = regex;
    }

    private <T> Set<T> check(Set<T> current) {
        current.removeIf(t -> !pattern.asPredicate().test(t.toString()));
        return current;
    }

    @Override
    public boolean check(YAPIONAnyType current) {
        if (current instanceof YAPIONObject yapionObject) {
            return !check(yapionObject.allKeys()).isEmpty();
        } else if (current instanceof YAPIONArray yapionArray) {
            return !check(yapionArray.allKeys()).isEmpty();
        }
        return false;
    }

    @Override
    public PathContext apply(PathContext pathContext) {
        List<YAPIONAnyType> result = new ArrayList<>();
        for (YAPIONAnyType element : pathContext.getCurrent()) {
            if (element instanceof YAPIONObject yapionObject) {
                check(yapionObject.allKeys()).forEach(s -> {
                    result.add(yapionObject.getAny(s));
                });
            } else if (element instanceof YAPIONArray yapionArray) {
                check(yapionArray.allKeys()).forEach(i -> {
                    result.add(yapionArray.getAny(i));
                });
            }
        }
        pathContext.setCurrent(result);
        return pathContext;
    }
}
