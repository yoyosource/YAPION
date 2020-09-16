package yapion.hierarchy.interfaces;

import java.io.IOException;
import java.io.OutputStream;

public interface ObjectOutput {

    String toYAPIONString();

    String toJSONString();

    String toLossyJSONString();

    void toOutputStream(OutputStream outputStream) throws IOException;

}
