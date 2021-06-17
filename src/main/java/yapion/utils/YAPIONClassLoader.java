package yapion.utils;

import yapion.annotations.api.InternalAPI;

import java.util.HashMap;
import java.util.Map;

@InternalAPI
public class YAPIONClassLoader extends ClassLoader {

    private Map<String, Class<?>> current = new HashMap<>();

    public YAPIONClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (current.containsKey(name)) {
            return current.get(name);
        }
        return Class.forName(name);
    }

    public Class<?> defineClass(String name, byte[] bytes) {
        Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
        current.put(name, clazz);
        return clazz;
    }

}
