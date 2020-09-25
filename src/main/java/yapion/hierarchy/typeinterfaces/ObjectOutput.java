// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.typeinterfaces;

import java.io.IOException;
import java.io.OutputStream;

public interface ObjectOutput {

    String toYAPIONString();

    String toJSONString();

    String toLossyJSONString();

    void toOutputStream(OutputStream outputStream) throws IOException;

}