package yapion.serializing;

import yapion.annotations.*;
import yapion.exceptions.YAPIONException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public class StateManager {

    private String state;
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
    private boolean globalOptimize = false;
    private boolean globalSave = false;

    public boolean is(Object object, Field field) {
        if (this.object == null || this.object != object) {
            this.object = object;
            if (is(object.getClass().getDeclaredAnnotation(YAPIONLoadExclude.class))) globalLoad = false;
            globalLoad = is(object.getClass().getDeclaredAnnotation(YAPIONLoad.class));
            globalOptimize = is(object.getClass().getDeclaredAnnotation(YAPIONOptimize.class));
            if (is(object.getClass().getDeclaredAnnotation(YAPIONSaveExclude.class))) globalSave = false;
            globalSave = is(object.getClass().getDeclaredAnnotation(YAPIONSave.class));
        }

        return false;
    }

    public static class YAPIONInfo {

        public final boolean load;
        public final boolean save;
        public final boolean optimize;

        private YAPIONInfo(boolean load, boolean save, boolean optimize) {
            this.load = load;
            this.save = save;
            this.optimize = optimize;
        }

    }

}
