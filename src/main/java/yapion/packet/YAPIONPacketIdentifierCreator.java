// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.packet;

@FunctionalInterface
public interface YAPIONPacketIdentifierCreator<T> {

    YAPIONPacketIdentifier<T> identifier();

}