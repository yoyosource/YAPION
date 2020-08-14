package yapion.packet;

import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONSerializer;

import java.io.IOException;
import java.io.OutputStream;

public class YAPIONOutputStream extends OutputStream {

    public YAPIONOutputStream() {
        super();
    }

    @Override
    public void write(byte[] b) throws IOException {
        throw new IOException();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        throw new IOException();
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public void write(int b) throws IOException {
        throw new IOException();
    }

    public void write(YAPIONObject yapionObject) throws IOException {
        super.write(yapionObject.toString().getBytes());
    }

    public void write(Object object) throws IOException {
        write(YAPIONSerializer.serialize(object));
    }

    public void write(Object object, String state) throws IOException {
        write(YAPIONSerializer.serialize(object, state));
    }

}
