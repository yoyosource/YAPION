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
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.util.Locale;

public class Test {

    public static void main(String[] args) {
        Locale locale = Locale.forLanguageTag("de-us");
        System.out.println(locale);
        YAPIONObject yapionObject = YAPIONSerializer.serialize(locale);
        System.out.println(yapionObject);
        Locale referenceLocale = YAPIONDeserializer.deserialize(yapionObject);
        System.out.println(referenceLocale);
        System.out.println(locale.equals(referenceLocale));
    }
}
