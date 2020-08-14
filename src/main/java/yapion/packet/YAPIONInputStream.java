package yapion.packet;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

import java.io.IOException;
import java.io.InputStream;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class YAPIONInputStream {

    private final InputStream inputStream;

    public YAPIONInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public synchronized int available() throws IOException {
        return inputStream.available();
    }

    public synchronized YAPIONObject read() {
        return YAPIONParser.parse(inputStream);
    }

    public synchronized Object readObject() {
        read();
        return null;
    }

}
