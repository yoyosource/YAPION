package yapion.hierarchy.interfaces;

import yapion.hierarchy.typegroups.YAPIONAnyType;

public interface ObjectPath {

    String getPath(YAPIONAnyType yapionAnyType);

    String[] getPath();

    int getDepth();

}
