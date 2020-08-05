// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.parser;

import yapion.hierarchy.types.YAPIONValue;

public class YAPIONParserMapMapping {

    public final YAPIONValue<String> mapping;

    YAPIONParserMapMapping(YAPIONValue<String> mapping) {
        this.mapping = mapping;
    }

    @Override
    public String toString() {
        return "YAPIONParserMapMapping{" +
                "mapping=" + mapping +
                '}';
    }

}