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

package yapion.parser;

import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.options.ParseOptions;

public class Test {

    public static void main(String[] args) {
        {
            YAPIONObject yapionObject = YAPIONParser.parse(
                    """
                    {
                      Hello(World)
                    }
                    """);
            System.out.println(yapionObject);
        }
        {
            YAPIONObject yapionObject = YAPIONParser.parse(
                    """
                    [
                      Hello World,
                    ]
                    """);
            System.out.println(yapionObject);
        }
        {
            YAPIONObject yapionObject = YAPIONParser.parse(
                    """
                    <
                      (Hello World):(Hello World)
                      /*Hello World*/
                      {}:(Test)
                      []:->0000000000000000
                      <>:{}
                    >
                    """,
                    new ParseOptions().commentParsing(CommentParsing.KEEP)
            );
            System.out.println(yapionObject);
        }
        {
            try {
                YAPIONParser.parse(
                        """
                        {
                          Hello(\\u99kk)
                        }
                        """);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
