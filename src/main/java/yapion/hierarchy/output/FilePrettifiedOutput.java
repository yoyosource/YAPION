// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.hierarchy.output;

import java.io.File;
import java.io.IOException;

public class FilePrettifiedOutput extends FileOutput {

    public FilePrettifiedOutput(File file) throws IOException {
        super(file);
    }

    @Override
    protected boolean prettified() {
        return true;
    }

}