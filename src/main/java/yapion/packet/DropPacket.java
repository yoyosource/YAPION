/*
 * SPDX-License-Identifier: Apache-2.0
 * YAPION
 * Copyright (C) 2019,2020 yoyosource
 */

package yapion.packet;

import lombok.Getter;

@Getter
public class DropPacket extends YAPIONPacket {

    private final byte[] droppedBytes;
    private final int length;

    DropPacket(byte[] droppedBytes) {
        this.droppedBytes = droppedBytes;
        this.length = droppedBytes.length;
    }

}
