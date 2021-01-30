// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.types.value;

import java.util.Optional;

public interface ValueHandler<T> {

    String output(T t);

    Optional<T> preParse(String s);

    Optional<T> parse(String s);

    long referenceValue();

}