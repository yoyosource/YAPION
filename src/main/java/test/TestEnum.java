// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package test;

import lombok.ToString;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONObjenesis;
import yapion.annotations.serialize.YAPIONSave;

public class TestEnum {

    public enum Value {
        Type,
        Value,
        HUGO;
    }

    @YAPIONData
    @ToString
    public static class TestEnumObjenesis {

        private Value value = Value.Type;
        private Value type = Value.HUGO;

    }

    @YAPIONObjenesis
    @YAPIONSave
    @YAPIONLoad
    @ToString
    public static class TestEnumObjenesis2 {

        private Value value = Value.HUGO;
        private Value type = Value.Value;

    }

    @YAPIONSave
    @YAPIONLoad
    @ToString
    public static class TestEnumNormal {

        private Value value = Value.Type;
        private Value type = Value.Value;

    }

}