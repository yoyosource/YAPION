// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.serializing.serializer.object.security.iternal;

import yapion.hierarchy.types.YAPIONObject;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeySpecSerializer<PR extends PrivateKey, PU extends PublicKey> {

    YAPIONObject serializePrivateKey(PR pr) throws Exception;
    PR deserializePrivateKey(YAPIONObject yapionObject, String algorithm) throws Exception;

    YAPIONObject serializePublicKey(PU pu)throws Exception;
    PU deserializePublicKey(YAPIONObject yapionObject, String algorithm)throws Exception;

}