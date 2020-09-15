package yapion.hierarchy.typegroups;

import java.io.IOException;
import java.io.OutputStream;

public abstract class YAPIONAnyClosure {

    public abstract String toYAPIONString();

    public abstract String toJSONString();

    public abstract String toLossyJSONString();

    public abstract void toOutputStream(OutputStream outputStream) throws IOException;

    
    public abstract long referenceValue();

}
