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
