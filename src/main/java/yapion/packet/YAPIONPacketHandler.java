// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

@YAPIONLoadExclude(context = "*")
@YAPIONSaveExclude(context = "*")
public interface YAPIONPacketHandler {

    void handlePacket(YAPIONPacket yapionPacket);

    default boolean inThread() {
        return false;
    }

}