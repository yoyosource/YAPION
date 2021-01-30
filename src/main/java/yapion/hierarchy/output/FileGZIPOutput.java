package yapion.hierarchy.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class FileGZIPOutput extends StreamOutput {

    public FileGZIPOutput(File file) throws IOException {
        super(new GZIPOutputStream(new FileOutputStream(file)));
    }

    public FileGZIPOutput(File file, int size) throws IOException {
        super(new GZIPOutputStream(new FileOutputStream(file), size));
    }

    @Override
    protected void internalConsume(String s) {
        super.internalConsume(s);
    }

}
