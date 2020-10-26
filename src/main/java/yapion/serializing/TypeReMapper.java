package yapion.serializing;

import yapion.exceptions.serializing.YAPIONClassTypeException;

import java.util.HashMap;
import java.util.Map;

public class TypeReMapper {

    private Map<String, String> typeMappings = new HashMap<>();

    String remap(String type) {
        if (typeMappings.containsKey(type)) {
            return typeMappings.get(type);
        }
        return type;
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

    private void put(String fromType, String toType) {
        checkClassTypeValidity(fromType);
        checkClassTypeValidity(toType);
        typeMappings.put(fromType, toType);
    }

    public void addClassMapping(String fromType, String toType) {
        put(fromType, toType);
    }

    public void addClassMapping(Class<?> fromType, String toType) {
        put(fromType.getTypeName(), toType);
    }

    public void addClassMapping(String fromType, Class<?> toType) {
        put(fromType, toType.getTypeName());
    }

    public void addClassMapping(Class<?> fromType, Class<?> toType) {
        put(fromType.getTypeName(), toType.getTypeName());
    }

}
