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

package yapion;

import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.CommentParsing;
import yapion.parser.YAPIONParser;

public class YAPIONAssertion {

    public static void main(String[] args) {
        String s = """
                {
                    test/*hello*/(This is a test)
                    /*This is the key ip with the IP to the server*/
                    ip(127.0.0.1)
                    /*This is another comment*/
                    port(22)
                    /*Test comment*/
                    test[
                        /*Hello World*/
                        test,
                        /*This one is interesting*/
                        {}
                        /*HELLO THERE*/,
                        ()
                        /*HELLO THERE*/,,,,
                        /*Hello THERE 2*/
                        whatever this is,
                    ]
                    map<
                        /*This is also a comment*/
                        ():
                        /*THis is also a comment*/
                        ()
                        /*What to do with this one*/
                    >
                    /*
                    This one is interesting as well
                    */
                }""";
        YAPIONObject yapionObject = YAPIONParser.parse(s, CommentParsing.KEEP);
        System.out.println();
        System.out.println(yapionObject);
        System.out.println(yapionObject.toYAPION(true));
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends YAPIONAnyType> Matcher<T> isYAPION(String value) {
        return Is.is((T) YAPIONParser.parse("{" + value + "}").getYAPIONAnyType(""));
    }

    public static <T extends YAPIONAnyType> Matcher<T> isYAPION(T yapionAnyType) {
        return Is.is(yapionAnyType);
    }

}
