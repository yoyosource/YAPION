// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.parser;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import yapion.hierarchy.api.groups.YAPIONAnyType;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class YAPIONParserMapObject {
    public final String key;
    public final YAPIONAnyType value;
}