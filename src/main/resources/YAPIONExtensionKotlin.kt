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

import yapion.hierarchy.types.YAPIONObject
import yapion.parser.YAPIONParser
import yapion.serializing.YAPIONSerializer
import java.io.File
import java.io.InputStream

fun String.parse(): YAPIONObject {
    return YAPIONParser.parse(this);
}

fun StringBuilder.parse(): YAPIONObject {
    return YAPIONParser.parse(this.toString());
}

fun InputStream.parse(): YAPIONObject {
    return YAPIONParser.parse(this);
}

fun InputStream.parse(stopOnEnd: Boolean): YAPIONObject {
    return YAPIONParser.parse(this, stopOnEnd);
}

fun File.parse(): YAPIONObject {
    return YAPIONParser.parse(this);
}

fun File.parse(stopOnEnd: Boolean): YAPIONObject {
    return YAPIONParser.parse(this, stopOnEnd);
}

fun Any.serialize(): YAPIONObject {
    return YAPIONSerializer.serialize(this)
}

fun Any.serializeReduced(): YAPIONObject {
    return YAPIONSerializer(this).parse().getReducedYAPIONObject()
}