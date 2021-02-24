/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.object.YAPIONField;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.annotations.serialize.YAPIONSave;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.types.YAPIONObject;

import java.lang.reflect.Field;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public final class ContextManager {

    private final String state;
    private final boolean emptyState;

    public ContextManager(String state) {
        if (state == null) state = "";
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

    boolean willBeCascading(Class<?> clazz) {
        YAPIONData yapionData = clazz.getDeclaredAnnotation(YAPIONData.class);
        if (yapionData == null) return false;
        return yapionData.cascading() && is(yapionData.context());
    }

    boolean willBeCascading(YAPIONObject yapionObject) {
        if (yapionObject.hasValue(TYPE_IDENTIFIER, String.class)) {
            return true;
        }
        try {
            return willBeCascading(Class.forName(yapionObject.getValue(TYPE_IDENTIFIER, String.class).get()));
        } catch (Exception e) {
            return true;
        }
    }

    YAPIONInfo is(Class<?> clazz) {
        if (is(clazz.getDeclaredAnnotation(YAPIONLoadExclude.class))) globalLoad = false;
        globalLoad = is(clazz.getDeclaredAnnotation(YAPIONLoad.class));
        if (is(clazz.getDeclaredAnnotation(YAPIONSaveExclude.class))) globalSave = false;
        globalSave = is(clazz.getDeclaredAnnotation(YAPIONSave.class));

        if (yapionData == null) {
            yapionData = clazz.getDeclaredAnnotation(YAPIONData.class);
        }
        if (!isCascading()) {
            yapionData = clazz.getDeclaredAnnotation(YAPIONData.class);
        }
        if (yapionData != null) {
            boolean yapionDataBoolean = is(yapionData);
            globalLoad = globalLoad || yapionDataBoolean;
            globalSave = globalSave || yapionDataBoolean;
        }

        return new YAPIONInfo(globalLoad, globalSave, yapionData != null && globalLoad);
    }

    boolean isCascading() {
        return yapionData != null && yapionData.cascading();
    }

    YAPIONInfo is(Object object) {
        return is(object.getClass());
    }

    @SuppressWarnings({"java:S3011"})
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
