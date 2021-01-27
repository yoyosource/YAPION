// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.packet;

import yapion.hierarchy.types.YAPIONObject;

public class HandleFailedPacket extends YAPIONPacket {

    public YAPIONObject yapionObject;

    public HandleFailedPacket(YAPIONObject yapionObject) {
        this.yapionObject = yapionObject;
    }

}