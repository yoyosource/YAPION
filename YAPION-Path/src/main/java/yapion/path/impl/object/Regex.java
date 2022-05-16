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

package yapion.path.impl.object;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.path.PathContext;
import yapion.path.PathElement;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Regex implements PathElement {

    private Predicate<String> matchPredicate;

    public Regex(String check) {
        this(Pattern.compile(check));
    }

    public Regex(Pattern pattern) {
        this.matchPredicate = s -> pattern.matcher(s).matches();
    }

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        if (yapionAnyType instanceof YAPIONObject yapionObject) {
            return yapionObject.unsafe().keySet().stream().anyMatch(matchPredicate);
        }
        return false;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return pathContext.streamMap(yapionAnyType -> {
            long time = System.nanoTime();
            try {
                if (yapionAnyType instanceof YAPIONObject yapionObject) {
                    return yapionObject.unsafe().entrySet().stream().filter(entry -> matchPredicate.test(entry.getKey())).map(Map.Entry::getValue);
                }
                return null;
            } finally {
                pathContext.addTiming(this, System.nanoTime() - time);
            }
        });
    }
}
