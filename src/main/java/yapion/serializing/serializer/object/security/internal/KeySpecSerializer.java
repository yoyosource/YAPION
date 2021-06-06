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

package yapion.serializing.serializer.object.security.internal;

import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeySpecSerializer<PR extends PrivateKey, PU extends PublicKey> {

    YAPIONObject serializePrivateKey(SerializeData<PR> serializeData) throws GeneralSecurityException;
    PR deserializePrivateKey(DeserializeData<YAPIONObject> serializeData, String algorithm) throws GeneralSecurityException;

    YAPIONObject serializePublicKey(SerializeData<PU> serializeData)throws GeneralSecurityException;
    PU deserializePublicKey(DeserializeData<YAPIONObject> serializeData, String algorithm)throws GeneralSecurityException;

}
