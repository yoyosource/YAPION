// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.parser;

import yapion.hierarchy.YAPIONVariable;

public class YAPIONParserMapObject {

    public final YAPIONVariable variable;

    YAPIONParserMapObject(YAPIONVariable variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return "YAPIONParserMapObject{" +
                "variable=" + variable +
                '}';
    }

}