/**
 * Copyright 2019,2020 yoyosource
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

package system;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.*;
import yapion.parser.YAPIONParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class YAPIONMain {

    public static void main(String[] args) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("test", new YAPIONValue<>("Hello World")));
        yapionObject.add(new YAPIONVariable("test2", new YAPIONValue<>("")));
        yapionObject.add(new YAPIONVariable("test3", new YAPIONValue<>("true")));
        yapionObject.add(new YAPIONVariable("test4", new YAPIONValue<>("0")));
        yapionObject.add(new YAPIONVariable("test5", new YAPIONValue<>(true)));
        yapionObject.add(new YAPIONVariable("test6", new YAPIONValue<>('A')));
        yapionObject.add(new YAPIONVariable("pointer", new YAPIONPointer(yapionObject)));
        yapionObject.add(new YAPIONVariable("test8", new YAPIONValue<>(0)));
        yapionObject.add(new YAPIONVariable("test9", new YAPIONValue<>(0L)));
        yapionObject.add(new YAPIONVariable("test10", new YAPIONValue<>(0F)));
        yapionObject.add(new YAPIONVariable("test11", new YAPIONValue<>(0D)));
        yapionObject.add(new YAPIONVariable("test12", new YAPIONValue<>(BigInteger.valueOf(0))));
        yapionObject.add(new YAPIONVariable("test13", new YAPIONValue<>(BigDecimal.valueOf(0))));

        YAPIONObject yapionObject1 = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("test7", yapionObject1));
        yapionObject1.addOrPointer(new YAPIONVariable("pointer", yapionObject));

        YAPIONMap yapionMap = new YAPIONMap();
        yapionObject1.add(new YAPIONVariable("test-MAP", yapionMap));
        yapionMap.add(new YAPIONValue<>("hello"), new YAPIONValue<>("1"));

        YAPIONMap yapionMap1 = new YAPIONMap();
        yapionObject1.add(new YAPIONVariable("test-MAP1", yapionMap1));
        yapionMap1.add(new YAPIONObject(), new YAPIONObject());

        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add(new YAPIONVariable("test-ARRAY", yapionArray));
        yapionArray.add(new YAPIONValue<>("17853"));
        yapionArray.add(new YAPIONValue<>(3784));
        yapionArray.add(new YAPIONValue<>(true));
        System.out.println(yapionObject);

        /*for (int i = 0; i < 100; i++) {
            String s = yapionObject.toString();
            long time = System.currentTimeMillis();
            YAPIONParser.parse(s);
            time = System.currentTimeMillis() - time;
            System.out.println(YAPIONParser.parse(s));
            System.out.println(time);
        }*/

        System.out.println(YAPIONParser.parse(yapionObject.toString()));

        Optional<YAPIONAny.YAPIONSearch<? extends YAPIONAny>> value = yapionObject.get("test7", "test-MAP", "(hello)");
        if (value.isPresent()) {
            System.out.println(value.get());
        } else {
            System.out.println("Value Is Empty");
        }
        /*
        String s = yapionObject.toString();
        long time = System.currentTimeMillis();
        YAPIONParser.parse(s);
        System.out.println(System.currentTimeMillis() - time);
        */
    }

}