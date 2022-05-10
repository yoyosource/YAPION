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

package yapion.pathn.impl;

import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.pathn.PathContext;
import yapion.pathn.PathElement;

import java.util.*;
import java.util.stream.Collectors;

public class Flatten implements PathElement {

    @Override
    public boolean check(YAPIONAnyType yapionAnyType) {
        return true;
    }

    @Override
    public PathContext apply(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        return apply1(pathContext, possibleNextPathElement);
    }

    private PathContext apply2(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        long time = System.nanoTime();
        List<YAPIONAnyType> elements = pathContext.eval();
        System.out.println((System.nanoTime() - time) / 1000000.0 + "ms");
        Map<Long, Map<String, YAPIONAnyType>> map = new HashMap<>();
        for (YAPIONAnyType element : elements) {
            if (element instanceof YAPIONObject yapionObject && yapionObject.size() == 1) {
                String key = yapionObject.getKeys().get(0);
                YAPIONAnyType value = yapionObject.get(key);
                long identifier = pathContext.getReverseIdentifier(value);
                map.computeIfAbsent(identifier, integer -> new HashMap<>()).put(key, value);
            }
        }
        elements = map.entrySet().stream().map(longMapEntry -> {
            YAPIONObject yapionObject = new YAPIONObject();
            longMapEntry.getValue().forEach((s, yapionAnyType) -> {
                yapionObject.put(s, yapionAnyType);
            });
            return yapionObject;
        }).collect(Collectors.toList());
        return pathContext.with(elements);
    }

    private PathContext apply1(PathContext pathContext, Optional<PathElement> possibleNextPathElement) {
        Map<Long, YAPIONObject> map = new HashMap<>();
        pathContext.stream().forEach(yapionAnyType -> {
            long time = System.nanoTime();
            try {
                if (!(yapionAnyType instanceof YAPIONObject yapionObject)) {
                    return;
                }
                if (yapionObject.size() != 1) {
                    return;
                }
                String key = yapionObject.getKeys().get(0);
                YAPIONAnyType value = yapionObject.get(key);
                long identifier = pathContext.getReverseIdentifier(value);
                if (identifier == -1) {
                    return;
                }
                map.computeIfAbsent(identifier, ignore -> new YAPIONObject()).put(key, value);
            } finally {
                pathContext.addTiming(this, System.nanoTime() - time);
            }
        });
        return pathContext.with(new ArrayList<>(map.values()));
    }
}
