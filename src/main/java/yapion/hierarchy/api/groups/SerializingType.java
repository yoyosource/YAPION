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

package yapion.hierarchy.api.groups;

import yapion.serializing.TypeReMapper;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONFlags;

public interface SerializingType<T extends YAPIONDataType<?, ?> & SerializingType<T>> {

    default <K> K deserialize() {
        return YAPIONDeserializer.deserialize((T) this);
    }

    default <K> K deserialize(String context) {
        return YAPIONDeserializer.deserialize((T) this, context);
    }

    default <K> K deserialize(TypeReMapper typeReMapper) {
        return YAPIONDeserializer.deserialize((T) this, typeReMapper);
    }

    default <K> K deserialize(YAPIONFlags yapionFlags) {
        return YAPIONDeserializer.deserialize((T) this, yapionFlags);
    }

    default <K> K deserialize(String context, TypeReMapper typeReMapper) {
        return YAPIONDeserializer.deserialize((T) this, context, typeReMapper);
    }

    default <K> K deserialize(String context, YAPIONFlags yapionFlags) {
        return YAPIONDeserializer.deserialize((T) this, context, yapionFlags);
    }

    default <K> K deserialize(String context, TypeReMapper typeReMapper, YAPIONFlags yapionFlags) {
        return YAPIONDeserializer.deserialize((T) this, context, typeReMapper, yapionFlags);
    }

}
