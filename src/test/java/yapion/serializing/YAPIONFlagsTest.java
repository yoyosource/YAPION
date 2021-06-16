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

public class YAPIONFlagsTest {

    @Test
    public void testFlagCount() {
        assertThat(YAPIONFlag.flagKeys().length, greaterThanOrEqualTo(4));
    }

    @Test
    public void testFlagKeyConstructor() {
        assertThat(YAPIONFlag.flagKey("base.dataLoss.exception"), sameInstance(YAPIONFlag.DATA_LOSS_EXCEPTION));
    }

    @Test
    public void testErrorSerialization() {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new Error());
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), notNullValue());
    }

    @Test(expected = YAPIONDataLossException.class)
    public void testErrorSerializationFailOne() {
        YAPIONFlags yapionFlags = new YAPIONFlags();
        yapionFlags.setTrue(YAPIONFlag.DATA_LOSS_EXCEPTION);
        YAPIONSerializer.serialize(new Error(), yapionFlags);
    }

    @Test(expected = YAPIONDataLossException.class)
    public void testErrorSerializationFailTwo() {
        YAPIONFlags yapionFlags = new YAPIONFlags();
        yapionFlags.setTrue(YAPIONFlag.ERROR_EXCEPTION);
        YAPIONSerializer.serialize(new Error(), yapionFlags);
    }

    @Test(expected = YAPIONDataLossException.class)
    public void testPrivateKeySerializationFailOne() throws Exception {
        KeyPair pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        YAPIONSerializer.serialize(pair);
    }

    @Test(expected = YAPIONDataLossException.class)
    public void testPrivateKeySerializationFailTwo() throws Exception {
        KeyPair pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        YAPIONFlags yapionFlags = new YAPIONFlags();
        yapionFlags.setTrue(YAPIONFlag.DATA_LOSS_EXCEPTION);
        YAPIONSerializer.serialize(pair, yapionFlags);
    }

    @Test
    public void testPrivateKeySerialization() throws Exception {
        KeyPair pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        YAPIONFlags yapionFlags = new YAPIONFlags();
        yapionFlags.setTrue(YAPIONFlag.DATA_LOSS_EXCEPTION);
        yapionFlags.setFalse(YAPIONFlag.PRIVATE_KEY_EXCEPTION);
        YAPIONObject yapionObject = YAPIONSerializer.serialize(pair, yapionFlags);
        assertThat(yapionObject, notNullValue());
    }

}
