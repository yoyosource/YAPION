package yapion.utils;

import sun.jvm.hotspot.tools.jcore.ClassWriter;
import yapion.exceptions.utils.YAPIONConstructInstanceException;
import yapion.exceptions.utils.YAPIONReflectionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class ReflectionsUtils {

    private ReflectionsUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Optional<Object> invokeMethod(String name, Object object, Object... parameters) {
        Class<?>[] classes = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].getClass();
        }

        try {
            Method method = object.getClass().getDeclaredMethod(name, classes);
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(object, parameters));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            return Optional.empty();
        }
    }

    public static Object constructObject(String className) {
        Object o = null;
        if (className.contains("$")) {
            String cName = className.substring(0, className.lastIndexOf('$'));
            o = constructObject(cName);
        }
        return constructInstance(className, o);
    }

    public static Object constructInstance(String className, Object o) {
        if (className.contains("$")) {
            throw new YAPIONConstructInstanceException();
        }
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor;
            if (o == null) {
                constructor = clazz.getDeclaredConstructor();
            } else {
                constructor = clazz.getDeclaredConstructor(o.getClass());
            }
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new YAPIONReflectionException(e.getMessage(), e.getCause());
        }
    }

}