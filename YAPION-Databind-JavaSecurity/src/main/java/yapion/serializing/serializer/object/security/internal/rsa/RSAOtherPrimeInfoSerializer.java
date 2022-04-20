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

package yapion.serializing.serializer.object.security.internal.rsa;

import yapion.annotations.api.SerializerImplementation;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.FinalInternalSerializer;

import java.math.BigInteger;
import java.security.spec.RSAOtherPrimeInfo;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@SerializerImplementation(since = "0.23.0")
public class RSAOtherPrimeInfoSerializer implements FinalInternalSerializer<RSAOtherPrimeInfo> {

    @Override
    public Class<?> type() {
        return RSAOtherPrimeInfo.class;
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<RSAOtherPrimeInfo> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());
        yapionObject.add("prime", serializeData.object.getPrime());
        yapionObject.add("primeExponent", serializeData.object.getExponent());
        yapionObject.add("crtCoefficient", serializeData.object.getCrtCoefficient());
        return yapionObject;
    }

    @Override
    public RSAOtherPrimeInfo deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;
        BigInteger prime = yapionObject.getValue("prime", BigInteger.class).get();
        BigInteger primeExponent = yapionObject.getValue("primeExponent", BigInteger.class).get();
        BigInteger crtCoefficient = yapionObject.getValue("crtCoefficient", BigInteger.class).get();
        return new RSAOtherPrimeInfo(prime, primeExponent, crtCoefficient);
    }

}
