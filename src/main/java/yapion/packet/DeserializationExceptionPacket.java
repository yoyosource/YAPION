// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.packet;

import lombok.Getter;
import yapion.hierarchy.types.YAPIONObject;

@Getter
public class DeserializationExceptionPacket extends YAPIONPacket {

    private YAPIONObject yapionObject;

    DeserializationExceptionPacket(YAPIONObject yapionObject) {
        this.yapionObject = yapionObject;
    }

}