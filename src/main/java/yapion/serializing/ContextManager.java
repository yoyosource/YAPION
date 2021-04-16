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

    boolean is(YAPIONLoad[] annotations) {
        if (annotations == null) return false;
        for (YAPIONLoad annotation : annotations) {
            if (is(annotation.context())) {
                return true;
            }
        }
        return false;
    }

    boolean is(YAPIONLoadExclude[] annotations) {
        if (annotations == null) return false;
        for (YAPIONLoadExclude annotation : annotations) {
            if (is(annotation.context())) {
                return true;
            }
        }
        return false;
    }

    boolean is(YAPIONOptimize[] annotations) {
        if (annotations == null) return false;
        for (YAPIONOptimize annotation : annotations) {
            if (is(annotation.context())) {
                return true;
            }
        }
        return false;
    }

    boolean is(YAPIONSave[] annotations) {
        if (annotations == null) return false;
        for (YAPIONSave annotation : annotations) {
            if (is(annotation.context())) {
                return true;
            }
        }
        return false;
    }

    boolean is(YAPIONSaveExclude[] annotations) {
        if (annotations == null) return false;
        for (YAPIONSaveExclude annotation : annotations) {
            if (is(annotation.context())) {
                return true;
            }
        }
        return false;
    }

    boolean is(YAPIONData[] annotations) {
        if (annotations == null) return false;
        for (YAPIONData annotation : annotations) {
            if (is(annotation.context())) {
                return true;
            }
        }
        return false;
    }

    boolean is(YAPIONField[] annotations) {
        if (annotations == null) return false;
        for (YAPIONField annotation : annotations) {
            if (is(annotation.context())) {
                return true;
            }
        }
        return false;
    }

    private boolean globalLoad = false;
    private boolean globalSave = false;
    private YAPIONData[] yapionDatas = null;

    @SuppressWarnings("java:S1117")
    boolean willBeCascading(Class<?> clazz) {
        YAPIONData[] yapionDatas = clazz.getDeclaredAnnotationsByType(YAPIONData.class);
        if (yapionDatas == null) return false;
        if (!is(yapionDatas)) {
            return false;
        }
        for (YAPIONData yapionData : yapionDatas) {
            if (yapionData.cascading()) {
                return true;
            }
        }
        return false;
    }

    boolean willBeCascading(YAPIONObject yapionObject) {
        if (!yapionObject.hasValue(TYPE_IDENTIFIER, String.class)) {
            return true;
        }
        try {
            return willBeCascading(Class.forName(yapionObject.getValue(TYPE_IDENTIFIER, String.class).get()));
        } catch (Exception e) {
            return true;
        }
    }

    YAPIONInfo is(Class<?> clazz) {
        if (is(clazz.getDeclaredAnnotationsByType(YAPIONLoadExclude.class))) globalLoad = false;
        globalLoad = is(clazz.getDeclaredAnnotationsByType(YAPIONLoad.class));
        if (is(clazz.getDeclaredAnnotationsByType(YAPIONSaveExclude.class))) globalSave = false;
        globalSave = is(clazz.getDeclaredAnnotationsByType(YAPIONSave.class));

        if (yapionDatas == null) {
            yapionDatas = clazz.getDeclaredAnnotationsByType(YAPIONData.class);
        }
        if (!isCascading()) {
            yapionDatas = clazz.getDeclaredAnnotationsByType(YAPIONData.class);
        }
        if (yapionDatas != null) {
            boolean yapionDataBoolean = is(yapionDatas);
            globalLoad = globalLoad || yapionDataBoolean;
            globalSave = globalSave || yapionDataBoolean;
        }

        return new YAPIONInfo(globalLoad, globalSave, (yapionDatas != null && yapionDatas.length > 0) && globalLoad);
    }

    boolean isCascading() {
        if (yapionDatas == null || yapionDatas.length == 0) {
            return false;
        }
        for (YAPIONData yapionData : yapionDatas) {
            if (yapionData.cascading()) {
                return true;
            }
        }
        return false;
    }

    YAPIONInfo is(Object object) {
        return is(object.getClass());
    }

    @SuppressWarnings({"java:S3011"})
    YAPIONInfo is(Object object, Field field) {
        is(object.getClass());

        YAPIONLoadExclude[] yapionLoadExclude = field.getDeclaredAnnotationsByType(YAPIONLoadExclude.class);
        YAPIONLoad[] yapionLoad = field.getDeclaredAnnotationsByType(YAPIONLoad.class);
        YAPIONOptimize[] yapionOptimize = field.getDeclaredAnnotationsByType(YAPIONOptimize.class);
        YAPIONSaveExclude[] yapionSaveExclude = field.getDeclaredAnnotationsByType(YAPIONSaveExclude.class);
        YAPIONSave[] yapionSave = field.getDeclaredAnnotationsByType(YAPIONSave.class);
        YAPIONField[] yapionField = field.getDeclaredAnnotationsByType(YAPIONField.class);

        boolean localDefault = false;
        try {
            localDefault = field.getType().isEnum();
        } catch (IllegalArgumentException e) {
            // ignored
        }

        boolean localLoad = localDefault; // no field ignore for enum values (if not explicitly defined)
        if (yapionLoadExclude != null && yapionLoad == null) {
            localLoad = !is(yapionLoadExclude);
        } else if (yapionLoadExclude == null && yapionLoad != null) {
            localLoad = is(yapionLoad);
        } else if (yapionLoadExclude != null) {
            if (is(yapionLoadExclude)) localLoad = false;
            localLoad |= is(yapionLoad);
        }

        boolean localOptimize = is(yapionOptimize);

        boolean localSave = localDefault; // no field ignore for enum values (if not explicitly defined)
        if (yapionSaveExclude != null && yapionSave == null) {
            localSave = !is(yapionSaveExclude);
        } else if (yapionSaveExclude == null && yapionSave != null) {
            localSave = is(yapionSave);
        } else if (yapionSaveExclude != null) {
            if (is(yapionSaveExclude)) localSave = false;
            localSave |= is(yapionSave);
        }

        if (yapionField != null) {
            boolean yapionFieldBoolean = is(yapionField);
            localLoad = localLoad || yapionFieldBoolean;
            localSave = localSave || yapionFieldBoolean;
        }

        if (yapionDatas != null) {
            boolean yapionDataBoolean = is(yapionDatas);
            localLoad = localLoad || yapionDataBoolean;
            localSave = localSave || yapionDataBoolean;
        }

        return new YAPIONInfo(globalLoad && localLoad, globalSave && localSave, (yapionDatas != null && yapionDatas.length > 0) && localLoad, localOptimize);
    }

    public static class YAPIONInfo {

        public final boolean load;
        public final boolean save;
        public final boolean optimize;
        public final boolean data;

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
