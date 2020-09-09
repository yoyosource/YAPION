// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONPostDeserialization;
import yapion.annotations.object.YAPIONPreDeserialization;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.annotations.serialize.YAPIONSave;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;

import java.lang.reflect.Field;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public final class StateManager {

    private final String state;
    private boolean emptyState = false;

    public StateManager(String state) {
        if (state.contains(" ")) {
            throw new YAPIONException("State cannot contain ' ': \"" + state + "\"");
        }
        this.state = state;
        this.emptyState = state.isEmpty();
    }

    protected String get() {
        return state;
    }

    private boolean is(String s) {
        if (s.equals("*")) return true;
        if (!s.isEmpty() && emptyState) return false;
        if (emptyState) return true;
        if (s.isEmpty()) return true;
        return s.contains(state);
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

    boolean is(YAPIONPreDeserialization annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    boolean is(YAPIONPostDeserialization annotation) {
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
            globalLoad = is(yapionData);
            globalSave = is(yapionData);
        }

        return new YAPIONInfo(globalLoad, globalSave, yapionData != null && globalLoad);
    }

    YAPIONInfo is(Object object) {
        if (is(object.getClass().getDeclaredAnnotation(YAPIONLoadExclude.class))) globalLoad = false;
        globalLoad = is(object.getClass().getDeclaredAnnotation(YAPIONLoad.class));
        if (is(object.getClass().getDeclaredAnnotation(YAPIONSaveExclude.class))) globalSave = false;
        globalSave = is(object.getClass().getDeclaredAnnotation(YAPIONSave.class));

        yapionData = object.getClass().getDeclaredAnnotation(YAPIONData.class);
        if (yapionData != null) {
            globalLoad = is(yapionData);
            globalSave = is(yapionData);
        }

        return new YAPIONInfo(globalLoad, globalSave, yapionData != null && globalLoad);
    }

    YAPIONInfo is(Object object, Field field) {
        is(object);

        YAPIONLoadExclude yapionLoadExclude = field.getDeclaredAnnotation(YAPIONLoadExclude.class);
        YAPIONLoad yapionLoad = field.getDeclaredAnnotation(YAPIONLoad.class);
        YAPIONOptimize yapionOptimize = field.getDeclaredAnnotation(YAPIONOptimize.class);
        YAPIONSaveExclude yapionSaveExclude = field.getDeclaredAnnotation(YAPIONSaveExclude.class);
        YAPIONSave yapionSave = field.getDeclaredAnnotation(YAPIONSave.class);

        localLoad = true;
        if (yapionLoadExclude != null && yapionLoad == null) {
            localLoad = !is(yapionLoadExclude);
        } else if (yapionLoadExclude == null && yapionLoad != null) {
            localLoad = is(yapionLoad);
        } else if (yapionLoadExclude != null) {
            if (is(yapionLoadExclude)) localLoad = false;
            localLoad = is(yapionLoad);
        }

        localOptimize = is(yapionOptimize);

        localSave = true;
        if (yapionSaveExclude != null && yapionSave == null) {
            localSave = !is(yapionSaveExclude);
        } else if (yapionSaveExclude == null && yapionSave != null) {
            localSave = is(yapionSave);
        } else if (yapionSaveExclude != null) {
            if (is(yapionSaveExclude)) localSave = false;
            localSave = is(yapionSave);
        }

        if (yapionData != null) {
            localLoad = is(yapionData);
            localSave = is(yapionData);
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