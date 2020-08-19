// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yapion.hierarchy.types.*;
import yapion.packet.YAPIONInputStream;
import yapion.packet.YAPIONOutputStream;
import yapion.parser.TypeStack;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

public class YAPIONLogger {

    private YAPIONLogger() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger("YAPION");

    private static final Logger parsingLogger       = LoggerFactory.getLogger("YAPION [parser      ]");
    private static final Logger serializingLogger   = LoggerFactory.getLogger("YAPION [serializer  ]");
    private static final Logger deserializingLogger = LoggerFactory.getLogger("YAPION [deserializer]");
    private static final Logger utilsLogger         = LoggerFactory.getLogger("YAPION [utils       ]");

    private static final Logger outsideLogger       = LoggerFactory.getLogger("YAPION [wrong class ]");

    public enum LoggingType {
        PARSER(parsingLogger, YAPIONParser.class, TypeStack.class, YAPIONArray.class, YAPIONMap.class, YAPIONObject.class, YAPIONPointer.class, YAPIONValue.class),
        SERIALIZER(serializingLogger, YAPIONSerializer.class, YAPIONOutputStream.class, ModifierUtils.class, ReflectionsUtils.class),
        DESERIALIZER(deserializingLogger, YAPIONDeserializer.class, YAPIONInputStream.class, ModifierUtils.class, ReflectionsUtils.class),
        UTILS(utilsLogger, ModifierUtils.class, RecursionUtils.class, ReferenceIDUtils.class, ReflectionsUtils.class, YAPIONLogger.class);

        private final Logger logger;
        private final Class<?>[] validClasses;

        LoggingType(Logger logger, Class<?>... validClasses) {
            this.logger = logger;
            this.validClasses = validClasses;
        }

        private Logger getLogger() {
            Class<?> caller;
            try {
                caller = getCallingClass(3);
            } catch (IllegalStateException e) {
                outsideLogger.warn("A Logger was used from outside the valid class span!");
                return outsideLogger;
            }
            if (caller != null) {
                String name = caller.getTypeName();
                for (Class<?> clazz : validClasses) {
                    if (clazz.getTypeName().equals(name)) {
                        return logger;
                    }
                }
            }
            outsideLogger.warn("A Logger was used from outside the valid class span!");
            return outsideLogger;
        }
    }

    private static final class ClassContextSecurityManager extends SecurityManager {
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }

    private static ClassContextSecurityManager SECURITY_MANAGER;
    private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;

    private static ClassContextSecurityManager getSecurityManager() {
        if (SECURITY_MANAGER != null)
            return SECURITY_MANAGER;
        else if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED)
            return null;
        else {
            SECURITY_MANAGER = safeCreateSecurityManager();
            SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
            return SECURITY_MANAGER;
        }
    }

    private static ClassContextSecurityManager safeCreateSecurityManager() {
        try {
            return new ClassContextSecurityManager();
        } catch (java.lang.SecurityException sm) {
            return null;
        }
    }

    private static Class<?> getCallingClass(int i) {
        ClassContextSecurityManager securityManager = getSecurityManager();
        if (securityManager == null)
            return null;
        Class<?>[] trace = securityManager.getClassContext();
        String thisClassName = YAPIONLogger.class.getName();

        // Advance until Util is found
        for (; i < trace.length; i++) {
            if (thisClassName.equals(trace[i].getName())) break;
        }

        // trace[i] = Util; trace[i+1] = caller; trace[i+2] = caller's caller
        if (i >= trace.length || i + 1 >= trace.length) {
            throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; " + "this should not happen");
        }

        return trace[i + 1];
    }


    public static void debug(LoggingType loggingType, String message) {
        loggingType.getLogger().debug(message);
    }

    public static void debug(LoggingType loggingType, String format, Object... args) {
        loggingType.getLogger().debug(format, args);
    }

    public static void debug(LoggingType loggingType, String message, Throwable t) {
        loggingType.getLogger().debug(message, t);
    }

    public static void info(LoggingType loggingType, String message) {
        loggingType.getLogger().info(message);
    }

    public static void info(LoggingType loggingType, String format, Object... args) {
        loggingType.getLogger().info(format, args);
    }

    public static void info(LoggingType loggingType, String message, Throwable t) {
        loggingType.getLogger().info(message, t);
    }

    public static void error(LoggingType loggingType, String message) {
        loggingType.getLogger().error(message);
    }

    public static void error(LoggingType loggingType, String format, Object... args) {
        loggingType.getLogger().error(format, args);
    }

    public static void error(LoggingType loggingType, String message, Throwable t) {
        loggingType.getLogger().error(message, t);
    }

    public static void trace(LoggingType loggingType, String message) {
        loggingType.getLogger().trace(message);
    }

    public static void trace(LoggingType loggingType, String format, Object... args) {
        loggingType.getLogger().trace(format, args);
    }

    public static void trace(LoggingType loggingType, String message, Throwable t) {
        loggingType.getLogger().trace(message, t);
    }

    public static void warn(LoggingType loggingType, String message) {
        loggingType.getLogger().warn(message);
    }

    public static void warn(LoggingType loggingType, String format, Object... args) {
        loggingType.getLogger().warn(format, args);
    }

    public static void warn(LoggingType loggingType, String message, Throwable t) {
        loggingType.getLogger().warn(message, t);
    }

}