package yapion.serializing;

import yapion.exceptions.serializing.YAPIONClassTypeException;
import yapion.serializing.serializer.other.YAPIONSerializerPacket;
import yapion.utils.YAPIONTreeIterator;

import java.util.HashMap;
import java.util.Map;

public class TypeReMapper {

    private Map<String, String> typeMappings = new HashMap<>();

    String remap(String type) {
        if (typeMappings.containsKey(type)) {
            return typeMappings.get(type);
        }
        String copiedType = type;
        while (!typeMappings.containsKey(type + ".")) {
            if (type.isEmpty()) {
                return copiedType;
            }
            if (!type.contains(".")) {
                return copiedType;
            }
            type = type.substring(0, type.lastIndexOf('.'));
        }
        if (!type.isEmpty() && typeMappings.containsKey(type + ".")) {
            return typeMappings.get(type + ".") + copiedType.substring(type.length() + 1);
        }
        return copiedType;
    }

    private void checkClassTypeValidity(String type) {
        if (type.endsWith(".")) {
            throw new YAPIONClassTypeException("No valid class");
        }
        if (type.substring(0, type.lastIndexOf('.')).contains("$")) {
            throw new YAPIONClassTypeException("No valid class");
        }
        if (type.equals("java.lang.Enum")) {
            throw new YAPIONClassTypeException("No enum remapping");
        }
    }

    private void putClass(String fromType, String toType) {
        checkClassTypeValidity(fromType);
        checkClassTypeValidity(toType);
        typeMappings.put(fromType, toType);
    }

    public TypeReMapper addClassMapping(String fromType, String toType) {
        putClass(fromType, toType);
        return this;
    }

    public TypeReMapper addClassMapping(Class<?> fromType, String toType) {
        putClass(fromType.getTypeName(), toType);
        return this;
    }

    public TypeReMapper addClassMapping(String fromType, Class<?> toType) {
        putClass(fromType, toType.getTypeName());
        return this;
    }

    public TypeReMapper addClassMapping(Class<?> fromType, Class<?> toType) {
        putClass(fromType.getTypeName(), toType.getTypeName());
        return this;
    }

    private void checkPackageTypeValidity(String type) {
        if (!type.endsWith(".")) {
            throw new YAPIONClassTypeException("No valid package");
        }
        if (type.contains("$")) {
            throw new YAPIONClassTypeException("No valid package");
        }
    }

    private void putPackage(String fromType, String toType) {
        checkPackageTypeValidity(fromType);
        checkPackageTypeValidity(toType);
        typeMappings.put(fromType, toType);
    }

    public TypeReMapper addPackageMapping(String fromType, String toType) {
        putPackage(fromType, toType);
        return this;
    }

    public TypeReMapper addPackageMapping(Package fromType, String toType) {
        putPackage(fromType.getName() + ".", toType);
        return this;
    }

    public TypeReMapper addPackageMapping(String fromType, Package toType) {
        putPackage(fromType, toType.getName() + ".");
        return this;
    }

    public TypeReMapper addPackageMapping(Package fromType, Package toType) {
        putPackage(fromType.getName() + ".", toType.getName() + ".");
        return this;
    }

    public TypeReMapper copy() {
        TypeReMapper typeReMapper = new TypeReMapper();
        typeMappings.forEach((s, s2) -> typeReMapper.typeMappings.put(s, s2));
        return typeReMapper;
    }

}
