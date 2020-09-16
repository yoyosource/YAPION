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

import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

import java.io.*;

public class YAPIONMain {

    public static void main(String[] args) throws IOException {
        /*YAPIONObject yapionObject = parseJSON("{\"contributor\":[{\"name\":\"yoyosource\",\"owner\":true},{\"name\":\"chaoscaot444\",\"owner\":\"false\"}]}");
        System.out.println(yapionObject);
        System.out.println(yapionObject.toJSONString());
        System.out.println(yapionObject.toJSONString().equals("{\"contributor\":[{\"name\":\"yoyosource\",\"owner\":true},{\"name\":\"chaoscaot444\",\"owner\":\"false\"}]}"));

        YAPIONObject object = serialize(new Test());
        System.out.println(object);
        System.out.println(YAPIONParser.mapJSON(YAPIONParser.parseJSON(object.toJSONString())));
        System.out.println(object.toJSONString());
        for (int i = 0; i < 5; i++) {
            test(object);
            if (false) {
                object = parseJSON(object.toJSONString());
            } else {
                object = serialize(object);
            }
        }
        // object.toOutputStream(new FileOutputStream(new File("test.yapion")));*/

        /*YAPIONObject enum1 = YAPIONSerializer.serialize(new TestEnum.TestEnumNormal());
        YAPIONObject enum2 = YAPIONSerializer.serialize(new TestEnum.TestEnumObjenesis());
        YAPIONObject enum3 = YAPIONSerializer.serialize(new TestEnum.TestEnumObjenesis2());

        System.out.println(enum1);
        System.out.println(enum2);
        System.out.println(enum3);

        System.out.println(YAPIONDeserializer.deserialize(enum1));
        System.out.println(YAPIONDeserializer.deserialize(enum2));
        System.out.println(YAPIONDeserializer.deserialize(enum3));

        YAPIONPacket yapionPacket = new YAPIONPacket("type");
        yapionPacket.add("type", "type");
        System.out.println(yapionPacket.getYAPION());
        System.out.println(YAPIONDeserializer.deserialize(yapionPacket.getYAPION()));

        System.out.println(yapionPacket.getYAPION().toBinaryYAPIONString());*/

        //YAPIONObject yapionObject = YAPIONSerializer.serialize(new Test());
        //System.out.println(yapionObject.toYAPIONString());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("test.json"))));
        String line = bufferedReader.readLine();
        System.out.println(line);
        System.out.println(line.length());
        System.out.println(YAPIONParser.parseJSON(line).toYAPIONString());
        System.out.println(YAPIONParser.parseJSON(line).toYAPIONString().length());


        // System.out.println(yapionObject.toBinaryYAPIONString());
        // System.out.println(YAPIONParser.parse(yapionObject.toBinaryYAPIONString()));

        /*YAPIONObject yapionObject = YAPIONSerializer.serialize(new Test());
        yapionObject = YAPIONParser.parse(yapionObject.toString());
        System.out.println(yapionObject.getParseTime());
        System.out.println(yapionObject.getParseTimeMillis());
        long time = System.currentTimeMillis();
        int times = 80000;
        for (int i = 0; i <= times; i++) {
            YAPIONDeserializer.deserialize(yapionObject);
        }
        time = System.currentTimeMillis() - time;
        System.out.println(time * 1.0 / times + " s/deserialization");
        System.out.println(1.0 / (time * 1.0 / times) + " deserialization/s");
        System.out.println(time + " ms");*/
    }

    // 10000
    // 0.5993 s/deserialization
    // 1.6686133822793257 deserialization/s
    // 5993 ms

    // 20000
    // 0.45095 s/deserialization
    // 2.2175407473112316 deserialization/s
    // 9019 ms

    // 30000
    // 0.422 s/deserialization
    // 2.3696682464454977 deserialization/s
    // 12660 ms

    // 40000
    // 0.407825 s/deserialization
    // 2.4520321216207934 deserialization/s
    // 16313 ms

    // 50000
    // 0.4091 s/deserialization
    // 2.4443901246638964 deserialization/s
    // 20455 ms

    // 60000
    // 0.3728 s/deserialization
    // 2.6824034334763946 deserialization/s
    // 22368 ms

    // 70000
    // 0.3871 s/deserialization
    // 2.5833118057349522 deserialization/s
    // 27097 ms

    // 80000
    // 0.491175 s/deserialization
    // 2.03593423932407 deserialization/s
    // 39294 ms

    // 80000
    // 0.52795 s/deserialization
    // 1.89411876124633 deserialization/s
    // 42236 ms

    public static void test(YAPIONObject yapionObject) {
        int lengthJSON = yapionObject.toJSONString().length();
        int lengthYAPION = yapionObject.toString().length();
        double factor = (double)lengthJSON / lengthYAPION;
        System.out.println("FACTOR: " + factor + "   JSON: " + lengthJSON + "   YAPION: " + lengthYAPION + "   DIV: " + (lengthJSON - lengthYAPION));
    }

}