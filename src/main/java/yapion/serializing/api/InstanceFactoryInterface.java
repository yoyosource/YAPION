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

package yapion.serializing.api;

import yapion.serializing.YAPIONDeserializer;

public interface InstanceFactoryInterface<T> {

    /**
     * Describes the Class type this Factory creates instances
     * of. This will be used by the {@link YAPIONDeserializer}.
     *
     * @return the Class Type this Factory is for
     */
    Class<T> type();

    /**
     * This should return an Instance of the desired class
     * described by {@link #type()}. Each time this method
     * is called it should return a completely new instance
     * because the {@link YAPIONDeserializer} would destroy
     * other instances otherwise.
     *
     * @return the Object instance of type {@link #type()}
     */
    T instance();

}
