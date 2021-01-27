// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package yapion.hierarchy.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutput extends StreamOutput {

    public FileOutput(File file) throws IOException {
        super(new FileOutputStream(file));
    }

    @Override
    protected void internalConsume(String s) {
        super.internalConsume(s);
    }

}