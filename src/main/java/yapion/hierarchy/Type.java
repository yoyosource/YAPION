// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy;

public enum Type {

    ANY(0x21b7bcb508370b96L, "ANY"),

    OBJECT(0xff2986f7947f3bf9L, "OBJ"),
    ARRAY(0xf4f767c97724e81eL, "ARR"),
    MAP(0x29548df23a131510L, "MAP"),
    POINTER(0x4e958af3539fb57cL, "PNR"),
    // BINARY_POINTER(0x4e958af3539fb57cL, "PNR"),
    VALUE(0xf86bfba6285c9be2L, "VAL");

    private final long referenceValue;
    private final String name;

    Type(long referenceValue, String name) {
        this.referenceValue = referenceValue;
        this.name = name;
    }

    public long getReferenceValue() {
        return referenceValue;
    }

    public String getName() {
        return name;
    }

}