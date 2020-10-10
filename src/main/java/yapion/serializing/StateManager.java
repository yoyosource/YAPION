// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONField;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.annotations.serialize.YAPIONSave;
import yapion.annotations.serialize.YAPIONSaveExclude;

import java.lang.reflect.Field;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public final class StateManager {

    private final String state;
    private boolean emptyState;

    public StateManager(String state) {
        this.state = state;
        this.emptyState = state.isEmpty();
    }

    protected String get() {
        return state;
    }

    private boolean is(String[] strings) {
        if (emptyState) return true;
        if (strings.length == 0) return true;
        for (String s : strings) {
            if (s.equals("*")) return true;
            if (s.isEmpty()) return true;
            if (s.equals(state)) return true;
        }
        return false;
    }

    boolean is(YAPIONLoad annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    boolean is(YAPIONLoadExclude annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    boolean is(YAPIONOptimize annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    boolean is(YAPIONSave annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    boolean is(YAPIONSaveExclude annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    boolean is(YAPIONData annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    boolean is(YAPIONField annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    private boolean globalLoad = false;
    private boolean globalSave = false;
    private YAPIONData yapionData = null;

    private boolean localLoad = false;
    private boolean localOptimize = false;
    private boolean localSave = false;

    YAPIONInfo is(Class<?> clazz) {
        if (is(clazz.getDeclaredAnnotation(YAPIONLoadExclude.class))) globalLoad = false;
        globalLoad = is(clazz.getDeclaredAnnotation(YAPIONLoad.class));
        if (is(clazz.getDeclaredAnnotation(YAPIONSaveExclude.class))) globalSave = false;
        globalSave = is(clazz.getDeclaredAnnotation(YAPIONSave.class));

        yapionData = clazz.getDeclaredAnnotation(YAPIONData.class);
        if (yapionData != null) {
            boolean yapionDataBoolean = is(yapionData);
            globalLoad = globalLoad || yapionDataBoolean;
            globalSave = globalSave || yapionDataBoolean;
        }

        return new YAPIONInfo(globalLoad, globalSave, yapionData != null && globalLoad);
    }

    YAPIONInfo is(Object object) {
        return is(object.getClass());
    }

    YAPIONInfo is(Object object, Field field) {
        is(object.getClass());

        YAPIONLoadExclude yapionLoadExclude = field.getDeclaredAnnotation(YAPIONLoadExclude.class);
        YAPIONLoad yapionLoad = field.getDeclaredAnnotation(YAPIONLoad.class);
        YAPIONOptimize yapionOptimize = field.getDeclaredAnnotation(YAPIONOptimize.class);
        YAPIONSaveExclude yapionSaveExclude = field.getDeclaredAnnotation(YAPIONSaveExclude.class);
        YAPIONSave yapionSave = field.getDeclaredAnnotation(YAPIONSave.class);
        YAPIONField yapionField = field.getDeclaredAnnotation(YAPIONField.class);

        boolean localDefault = false;
        try {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            if (fieldValue != null) {
                localDefault = fieldValue.getClass().isEnum();
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            // ignored
        }

        localLoad = localDefault; // no field ignore for enum values (if not explicitly defined)
        if (yapionLoadExclude != null && yapionLoad == null) {
            localLoad = !is(yapionLoadExclude);
        } else if (yapionLoadExclude == null && yapionLoad != null) {
            localLoad = is(yapionLoad);
        } else if (yapionLoadExclude != null) {
            if (is(yapionLoadExclude)) localLoad = false;
            localLoad = is(yapionLoad);
        }

        localOptimize = is(yapionOptimize);

        localSave = localDefault; // no field ignore for enum values (if not explicitly defined)
        if (yapionSaveExclude != null && yapionSave == null) {
            localSave = !is(yapionSaveExclude);
        } else if (yapionSaveExclude == null && yapionSave != null) {
            localSave = is(yapionSave);
        } else if (yapionSaveExclude != null) {
            if (is(yapionSaveExclude)) localSave = false;
            localSave = is(yapionSave);
        }

        if (yapionField != null) {
            boolean yapionFieldBoolean = is(yapionField);
            localLoad = localLoad || yapionFieldBoolean;
            localSave = localSave || yapionFieldBoolean;
        }

        if (yapionData != null) {
            boolean yapionDataBoolean = is(yapionData);
            localLoad = localLoad || yapionDataBoolean;
            localSave = localSave || yapionDataBoolean;
        }

        return new YAPIONInfo(globalLoad && localLoad, globalSave && localSave, yapionData != null && localLoad, localOptimize);
    }

    public static class YAPIONInfo {

        public final boolean load;
        public final boolean save;
        public final boolean optimize;
        public final boolean data;

        private YAPIONInfo(boolean load, boolean save) {
            this.load = load;
            this.save = save;
            this.optimize = false;
            this.data = false;
        }

        private YAPIONInfo(boolean load, boolean save, boolean data) {
            this.load = load;
            this.save = save;
            this.optimize = false;
            this.data = data;
        }

        public YAPIONInfo(boolean load, boolean save, boolean data, boolean optimize) {
            this.load = load;
            this.save = save;
            this.optimize = optimize;
            this.data = data;
        }

    }

}