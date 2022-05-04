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

import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;
import yapion.parser.callbacks.CallbackResult;
import yapion.parser.callbacks.CallbackType;
import yapion.parser.options.StreamOptions;

public class StreamingSerializer {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONParser.parse(StreamingSerializer.class.getResourceAsStream("/test.yapion"), new StreamOptions()
                .parseCallback(CallbackType.VALUE, (key, value) -> {
                    System.out.println(key + ": " + value);
                    return CallbackResult.KEEP;
                }).parseCallback(CallbackType.POINTER, (key, value) -> {
                    System.out.println(key + ": " + value);
                    return CallbackResult.KEEP;
                }).parseCallback(CallbackType.COMMENT, (key, value) -> {
                    System.out.println(key + ": " + value);
                    return CallbackResult.KEEP;
                }));
        System.out.println(yapionObject);
    }
}
