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

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.*;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public class Test {

    public static void main(String[] args) {
        if (false) {
            Cache<String, String> cache = CacheBuilder.newBuilder().build();
            cache.put("test", "test");
            YAPIONObject yapionObject = YAPIONSerializer.serialize(cache);
            System.out.println(yapionObject);
            System.out.println((Object) YAPIONDeserializer.deserialize(yapionObject));
        }

        if (true) {
            Optional<?> optional = Optional.fromNullable(null);
            YAPIONObject yapionObject = YAPIONSerializer.serialize(optional);
            System.out.println(yapionObject);
            System.out.println((Object) YAPIONDeserializer.deserialize(yapionObject));
        }

        if (true) {
            Multimap<String, String> multimap = ArrayListMultimap.create();
            multimap.put("test", "test");
            multimap.put("test", "test2");
            YAPIONObject yapionObject = YAPIONSerializer.serialize(multimap);
            System.out.println(yapionObject);
            System.out.println((Object) YAPIONDeserializer.deserialize(yapionObject));
        }

        if (true) {
            YAPIONObject yapionObject = YAPIONSerializer.serialize(Range.all());
            System.out.println(yapionObject);
            System.out.println((Object) YAPIONDeserializer.deserialize(yapionObject));
        }
    }
}
