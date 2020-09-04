package yapion.hierarchy.interfaces;

import yapion.hierarchy.YAPIONAny;

public interface ObjectPath {

    String getPath(YAPIONAny yapionAny);

    String[] getPath();

    int getDepth();

}
