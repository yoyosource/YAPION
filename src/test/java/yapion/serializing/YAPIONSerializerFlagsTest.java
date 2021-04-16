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

package yapion.serializing;

import org.junit.Test;
import yapion.exceptions.serializing.YAPIONDataLossException;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class YAPIONSerializerFlagsTest {

    @Test
    public void testFlagCount() {
        assertThat(YAPIONSerializerFlagDefault.flagKeys().length, greaterThanOrEqualTo(4));
    }

    @Test
    public void testFlagKeyConstructor() {
        assertThat(YAPIONSerializerFlagDefault.flagKey("base.dataLoss.exception"), sameInstance(YAPIONSerializerFlagDefault.DATA_LOSS_EXCEPTION));
    }

    @Test
    public void testErrorSerialization() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new Error());
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), notNullValue());
    }

    @Test(expected = YAPIONDataLossException.class)
    public void testErrorSerializationFailOne() {
        YAPIONSerializerFlags yapionSerializerFlags = new YAPIONSerializerFlags();
        yapionSerializerFlags.setTrue(YAPIONSerializerFlagDefault.DATA_LOSS_EXCEPTION);
        YAPIONSerializer.serialize(new Error(), yapionSerializerFlags);
    }

    @Test(expected = YAPIONDataLossException.class)
    public void testErrorSerializationFailTwo() {
        YAPIONSerializerFlags yapionSerializerFlags = new YAPIONSerializerFlags();
        yapionSerializerFlags.setTrue(YAPIONSerializerFlagDefault.ERROR_EXCEPTION);
        YAPIONSerializer.serialize(new Error(), yapionSerializerFlags);
    }

    @Test(expected = YAPIONDataLossException.class)
    public void testPrivateKeySerializationFailOne() throws Exception {
        KeyPair pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        YAPIONSerializer.serialize(pair);
    }

    @Test(expected = YAPIONDataLossException.class)
    public void testPrivateKeySerializationFailTwo() throws Exception {
        KeyPair pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        YAPIONSerializerFlags yapionSerializerFlags = new YAPIONSerializerFlags();
        yapionSerializerFlags.setTrue(YAPIONSerializerFlagDefault.DATA_LOSS_EXCEPTION);
        YAPIONSerializer.serialize(pair, yapionSerializerFlags);
    }

    @Test
    public void testPrivateKeySerialization() throws Exception {
        KeyPair pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        YAPIONSerializerFlags yapionSerializerFlags = new YAPIONSerializerFlags();
        yapionSerializerFlags.setTrue(YAPIONSerializerFlagDefault.DATA_LOSS_EXCEPTION);
        yapionSerializerFlags.setFalse(YAPIONSerializerFlagDefault.PRIVATE_KEY_EXCEPTION);
        YAPIONObject yapionObject = YAPIONSerializer.serialize(pair, yapionSerializerFlags);
        assertThat(yapionObject, notNullValue());
    }

}
