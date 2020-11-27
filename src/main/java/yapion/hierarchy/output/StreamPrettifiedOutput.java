package yapion.hierarchy.output;

import java.io.OutputStream;

public class StreamPrettifiedOutput extends StreamOutput {

    public StreamPrettifiedOutput(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    protected boolean prettified() {
        return true;
    }

}
