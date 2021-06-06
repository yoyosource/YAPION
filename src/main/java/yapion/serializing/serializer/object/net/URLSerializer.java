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

package yapion.serializing.serializer.object.net;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.net.MalformedURLException;
import java.net.URL;

@SerializerImplementation(since = "0.12.0")
public class URLSerializer implements InternalSerializer<URL> {

    @Override
    public Class<?> type() {
        return URL.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<URL> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject(type());
        try {
            yapionObject.add("protocol", serializeData.object.getProtocol());
            yapionObject.add("host", serializeData.object.getHost());
            yapionObject.add("port", serializeData.object.getPort());
            yapionObject.add("file", serializeData.object.getFile());
        } catch (Exception e) {
            // Ignored
        }
        return yapionObject;
    }

    @Override
    public URL deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        String protocol = yapionObject.getValue("protocol", "").get();
        String host = yapionObject.getValue("host", "").get();
        int port = yapionObject.getValue("port", 0).get();
        String file = yapionObject.getValue("file", "").get();
        try {
            return new URL(protocol, host, port, file);
        } catch (MalformedURLException e) {
            // Ignored
        }
        try {
            return new URL(protocol, host, file);
        } catch (MalformedURLException e) {
            // Ignored
        }
        try {
            return new URL(host);
        } catch (MalformedURLException e) {
            // Ignored
        }
        return null;
    }
}
