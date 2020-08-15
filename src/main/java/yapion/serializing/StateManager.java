// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.serializing;

import yapion.annotations.*;
import yapion.exceptions.YAPIONException;

import java.lang.reflect.Field;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class StateManager {

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

    public boolean is(YAPIONLoad annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    public boolean is(YAPIONLoadExclude annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    public boolean is(YAPIONOptimize annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    public boolean is(YAPIONSave annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    public boolean is(YAPIONSaveExclude annotation) {
        if (annotation == null) return false;
        return is(annotation.context());
    }

    private Object object;
    private boolean globalLoad = false;
    private boolean globalSave = false;

    private Field field;
    private boolean localLoad = false;
    private boolean localOptimize = false;
    private boolean localSave = false;

    public YAPIONInfo is(Class<?> clazz) {
        if (is(clazz.getDeclaredAnnotation(YAPIONLoadExclude.class))) globalLoad = false;
        globalLoad = is(clazz.getDeclaredAnnotation(YAPIONLoad.class));
        if (is(clazz.getDeclaredAnnotation(YAPIONSaveExclude.class))) globalSave = false;
        globalSave = is(clazz.getDeclaredAnnotation(YAPIONSave.class));

        return new YAPIONInfo(globalLoad, globalSave);
    }

    public YAPIONInfo is(Object object) {
        if (this.object == null || this.object != object) {
            this.object = object;
            if (is(object.getClass().getDeclaredAnnotation(YAPIONLoadExclude.class))) globalLoad = false;
            globalLoad = is(object.getClass().getDeclaredAnnotation(YAPIONLoad.class));
            if (is(object.getClass().getDeclaredAnnotation(YAPIONSaveExclude.class))) globalSave = false;
            globalSave = is(object.getClass().getDeclaredAnnotation(YAPIONSave.class));

            // System.out.println(globalLoad + "   " + globalSave + "   false");
        }

        return new YAPIONInfo(globalLoad, globalSave);
    }

    public YAPIONInfo is(Object object, Field field) {
        is(object);

        if (this.field == null || this.field.equals(field)) {
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
            } else if (yapionLoadExclude != null && yapionLoad != null) {
                if (is(yapionLoadExclude)) localLoad = false;
                localLoad = is(yapionLoad);
            }

            localOptimize = is(yapionOptimize);

            localSave = true;
            if (yapionSaveExclude != null && yapionSave == null) {
                localSave = !is(yapionSaveExclude);
            } else if (yapionSaveExclude == null && yapionSave != null) {
                localSave = is(yapionSave);
            } else if (yapionSaveExclude != null && yapionSave != null) {
                if (is(yapionSaveExclude)) localSave = false;
                localSave = is(yapionSave);
            }

            // System.out.println(globalLoad + " " + localLoad + "   " + localOptimize + "   " + globalSave + " " + localSave);
        }

        return new YAPIONInfo(globalLoad && localLoad, globalSave && localSave, localOptimize);
    }

    public static class YAPIONInfo {

        public final boolean load;
        public final boolean save;
        public final boolean optimize;

        private YAPIONInfo(boolean load, boolean save) {
            this.load = load;
            this.save = save;
            this.optimize = false;
        }

        private YAPIONInfo(boolean load, boolean save, boolean optimize) {
            this.load = load;
            this.save = save;
            this.optimize = optimize;
        }

    }

}