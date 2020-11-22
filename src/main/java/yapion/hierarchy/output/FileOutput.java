package yapion.hierarchy.output;

import yapion.exceptions.YAPIONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutput extends StreamOutput {

    public FileOutput(File file) throws IOException {
        super(new FileOutputStream(file));
    }

    @Override
    public AbstractOutput consume(String s) {
        validCall();
        return super.consume(s);
    }

}
