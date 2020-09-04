package yapion.hierarchy.interfaces;

import java.io.IOException;
import java.io.OutputStream;

public interface Output {

    String toYAPIONString();

    String toJSONString();

    void toOutputStream(OutputStream outputStream) throws IOException;

}
