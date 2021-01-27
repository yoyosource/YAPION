// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.parser;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONValue;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONParserMapMapping {

    public final YAPIONValue<String> mapping;

    YAPIONParserMapMapping(YAPIONValue<String> mapping) {
        this.mapping = mapping;
    }

}