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

import lombok.experimental.UtilityClass;
import yapion.hierarchy.api.groups.SerializingType;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;
import yapion.serializing.TypeReMapper;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONFlags;
import yapion.serializing.YAPIONSerializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class can be used with lombok as an {@link lombok.experimental.ExtensionMethod} class.
 */
@UtilityClass
public class YAPIONExtension {

    /// Parser

    public static YAPIONObject parse(String s) {
        return new YAPIONParser(s).parse().result();
    }

    public static YAPIONObject parse(StringBuilder s) {
        return new YAPIONParser(s).parse().result();
    }

    public static YAPIONObject parse(byte[] s) {
        return new YAPIONParser(s).parse().result();
    }

    public static YAPIONObject parse(char[] s) {
        return new YAPIONParser(s).parse().result();
    }

    public static YAPIONObject parse(InputStream inputStream) {
        return new YAPIONParser(inputStream).parse().result();
    }

    public static YAPIONObject parse(InputStream inputStream, boolean stopOnEnd) {
        return new YAPIONParser(inputStream, stopOnEnd).parse().result();
    }

    public static YAPIONObject parse(File file) throws IOException {
        return new YAPIONParser(file).parse().result();
    }

    public static YAPIONObject parse(File file, boolean stopOnEnd) throws IOException {
        return new YAPIONParser(file, stopOnEnd).parse().result();
    }

    /// Output - is defined on ObjectOutput

    /// Serializing
    // Deserializing is defined on SerializingType

    public static <T extends YAPIONAnyType> T serialize(Object object) {
        return YAPIONSerializer.serialize(object);
    }

    public static <T extends YAPIONAnyType> T serializeReduced(Object object) {
        return new YAPIONSerializer(object).parse().getReducedYAPIONObject();
    }

    public static <K, T extends YAPIONDataType<?, ?> & SerializingType<T>> K deserialize(T serializingType) {
        return YAPIONDeserializer.deserialize(serializingType);
    }

    public static <K, T extends YAPIONDataType<?, ?> & SerializingType<T>> K deserialize(T serializingType, String context) {
        return YAPIONDeserializer.deserialize(serializingType, context);
    }

    public static <K, T extends YAPIONDataType<?, ?> & SerializingType<T>> K deserialize(T serializingType, TypeReMapper typeReMapper) {
        return YAPIONDeserializer.deserialize(serializingType, typeReMapper);
    }

    public static <K, T extends YAPIONDataType<?, ?> & SerializingType<T>> K deserialize(T serializingType, YAPIONFlags yapionFlags) {
        return YAPIONDeserializer.deserialize(serializingType, yapionFlags);
    }

    public static <K, T extends YAPIONDataType<?, ?> & SerializingType<T>> K deserialize(T serializingType, String context, TypeReMapper typeReMapper) {
        return YAPIONDeserializer.deserialize(serializingType, context, typeReMapper);
    }

    public static <K, T extends YAPIONDataType<?, ?> & SerializingType<T>> K deserialize(T serializingType, String context, YAPIONFlags yapionFlags) {
        return YAPIONDeserializer.deserialize(serializingType, context, yapionFlags);
    }

    public static <K, T extends YAPIONDataType<?, ?> & SerializingType<T>> K deserialize(T serializingType, String context, TypeReMapper typeReMapper, YAPIONFlags yapionFlags) {
        return YAPIONDeserializer.deserialize(serializingType, context, typeReMapper, yapionFlags);
    }

}
