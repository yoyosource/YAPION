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

import test.Test;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

import java.io.IOException;

import static yapion.parser.YAPIONParser.parseJSON;
import static yapion.serializing.YAPIONSerializer.serialize;

public class YAPIONMain {

    public static void main(String[] args) throws IOException {
        YAPIONObject yapionObject = parseJSON("{\"contributor\":[{\"name\":\"yoyosource\",\"owner\":true},{\"name\":\"chaoscaot444\",\"owner\":\"false\"}]}");
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
        // object.toOutputStream(new FileOutputStream(new File("test.yapion")));
    }

    public static void test(YAPIONObject yapionObject) {
        int lengthJSON = yapionObject.toJSONString().length();
        int lengthYAPION = yapionObject.toString().length();
        double factor = (double)lengthJSON / lengthYAPION;
        System.out.println("FACTOR: " + factor + "   JSON: " + lengthJSON + "   YAPION: " + lengthYAPION + "   DIV: " + (lengthJSON - lengthYAPION));
    }

}