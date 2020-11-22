package yapion.hierarchy.output;

import yapion.exceptions.YAPIONException;

import java.io.IOException;
import java.io.OutputStream;

public class StreamOutput extends AbstractOutput {

    private final OutputStream outputStream;

    public StreamOutput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public AbstractOutput consume(String s) {
        validCall();
        try {
            outputStream.write(bytes(s));
        } catch (IOException e) {
            throw new YAPIONException("Exception while writing data");
        }
        return this;
    }

    public void flush() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new YAPIONException("Exception while stream flushing");
        }
    }

    public void close() {
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new YAPIONException("Exception while stream closing");
        }
    }

}
