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

package yapion.serializing.annotationproccessing;

import yapion.annotations.registration.YAPIONAccessGenerator;
import yapion.parser.YAPIONParser;

public class Test {
    @YAPIONAccessGenerator(lombokToString = true)
    private final String text = "{@name(Person)name!(S)greeting!(S)contacts![{@reference(Person)}]}";

    public static void main(String[] args) {
        Person person = new Person(YAPIONParser.parse("{name(YoyoNow)greeting(Hello YoyoNow)contacts[]}"));
        System.out.println(person);
    }
}
