/*
 * Copyright 2022 yoyosource
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

import yapion.annotations.object.YAPIONData;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.api.groups.YAPIONDataType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.data.SerializationMutationContext;
import yapion.serializing.data.SerializeData;
import yapion.serializing.views.Mutator;
import yapion.utils.ClassUtils;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

public final class YAPIONSerializerTest {

    @YAPIONData
    private static class Test {
        private String s1 = "s1";
        private String s2 = "s2";
        private String s3 = "s3";
        private String s4 = "s4";
        private String s5 = "s5";
        private String s6 = "s6";
        private String s7 = "s7";
        private String s8 = "s8";
        private String s9 = "s9";
        private String s10 = "s10";
        private String s11 = "s11";
        private String s12 = "s12";
        private String s13 = "s13";
        private String s14 = "s14";
        private String s15 = "s15";
        private String s16 = "s16";
        private String s17 = "s17";
        private String s18 = "s18";
        private String s19 = "s19";
        private String s20 = "s20";

        private String[] strings = new String[]{"s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11", "s12", "s13", "s14", "s15", "s16", "s17", "s18", "s19", "s20"};

        private TestEnum testEnum = TestEnum.A;

        private String hugo = null;

        private YAPIONObject yapionObject = new YAPIONObject();

        private Test test = this;
    }

    public enum TestEnum {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
    }

    public static void main(String[] args) {
        YAPIONSerializerTest yapionSerializerTest = new YAPIONSerializerTest(new Test());
        yapionSerializerTest.parse();
        System.out.println(yapionSerializerTest.result);
    }

    private Map<Object, YAPIONPointer> referenceMap = new IdentityHashMap<>();
    private YAPIONAnyType result = null;

    private ResolutionGraph<Object, YAPIONAnyType> resolutionGraph = new ResolutionGraph<>((o, yapionAnyType) -> {
        if (result == null) {
            result = yapionAnyType;
        }
    }, (o, yapionAnyType) -> {
        if (referenceMap.containsKey(o)) {
            return referenceMap.get(o);
        }
        if (yapionAnyType instanceof YAPIONDataType) {
            referenceMap.put(o, new YAPIONPointer((YAPIONDataType) yapionAnyType));
        }
        return yapionAnyType;
    });

    private Object source;

    private final ContextManager contextManager = new ContextManager(null);
    private YAPIONFlags yapionFlags = new YAPIONFlags();

    public YAPIONSerializerTest(Object o) {
        this.source = o;
    }

    public void parse() {
        parse(source);
        while (!resolutionGraph.isFinished()) {
            parse(resolutionGraph.getUnresolved());
        }
    }

    private void parse(Object object) {
        if (object == null) {
            resolutionGraph.register(null, new YAPIONValue<>(null));
            return;
        }

        Class<?> type = object.getClass();
        InternalSerializer serializer = SerializeManager.getInternalSerializer(type);
        if (serializer != null && !serializer.empty()) {
            serializer.serialize(new SerializeData<>(null, null, object, contextManager.get(), null), resolutionGraph);
            if (serializer.finished()) {
                return;
            }
        } else if (GeneratedSerializerLoader.loadSerializerIfNeeded(type)) {
            parse(object);
            return;
        }

        boolean saveWithoutAnnotation = serializer != null && serializer.saveWithoutAnnotation();
        if (!contextManager.is(object).save && !saveWithoutAnnotation) {
            if (yapionFlags.isSet(YAPIONFlag.CLASSES_WITHOUT_ANNOTATION_EXCEPTION)) {
                throw new YAPIONSerializerException("No suitable serializer found, maybe class (" + object.getClass().getTypeName() + ") is missing YAPION annotations or the corresponding databind jar is missing. Possible databindings are: " + SerializeManagerDataBindings.getDataBindings());
            }
            if (yapionFlags.isSet(YAPIONFlag.CLASSES_WITHOUT_ANNOTATION_AS_NULL)) {
                resolutionGraph.register(object, new YAPIONValue<>(null));
                return;
            }
            if (!yapionFlags.isSet(YAPIONFlag.CLASSES_SAVE_WITHOUT_ANNOTATION)) {
                throw new YAPIONSerializerException("No suitable serializer found, maybe class (" + object.getClass().getTypeName() + ") is missing YAPION annotations or the corresponding databind jar is missing. Possible databindings are: " + SerializeManagerDataBindings.getDataBindings());
            }
            saveWithoutAnnotation = true;
        }

        // TODO: Add type identifier

        Class<? extends Mutator> mutatorClass = contextManager.getMutator(type);
        MutationManager mutationManager = new MutationManager(mutatorClass, SerializationMutationContext.class);

        /*
        SerializationContext serializationContext = new SerializationContext(this, null);
        MethodManager.preSerializationStep(object, object.getClass(), contextManager, serializationContext);
        */

        YAPIONObject yapionObject = new YAPIONObject(type);
        referenceMap.put(object, new YAPIONPointer(yapionObject));
        Class<?> objectClass = object.getClass();
        DependencySupplier<String, Object> dependencySupplier = resolutionGraph.register(object, yapionObject, (self, resolution) -> {
            self.add(resolution.k, resolution.v);
        });
        for (Field field : ReflectionsUtils.getFields(objectClass)) {
            field.setAccessible(true);
            if (ClassUtils.removed(field)) {
                continue;
            }
            ContextManager.YAPIONInfo yapionInfo = contextManager.is(object, field);
            if (!yapionInfo.save && !saveWithoutAnnotation) continue;
            if (!SerializeManager.getReflectionStrategy().checkGet(field, object)) continue;

            String name = field.getName();
            Object fieldObject = SerializeManager.getReflectionStrategy().get(field, object);

            if (mutationManager.hasMutation(name)) {
                SerializationMutationContext serializationMutationContext = new SerializationMutationContext(name, fieldObject);
                MethodReturnValue<Object> methodReturnValue = mutationManager.mutate(name, serializationMutationContext);
                if (methodReturnValue.isPresent()) {
                    serializationMutationContext = (SerializationMutationContext) methodReturnValue.get();
                }
                if (serializationMutationContext.fieldName == null) {
                    continue;
                }
                name = serializationMutationContext.fieldName;
                fieldObject = serializationMutationContext.value;
            }
            if (name == null) {
                continue;
            }

            if (fieldObject == null) {
                if (!yapionInfo.optimize) {
                    dependencySupplier.depends(name, new YAPIONValue<>(null));
                }
                continue;
            }

            dependencySupplier.depends(name, fieldObject);
        }
        dependencySupplier.finalizer(() -> {
            // MethodManager.postSerializationStep(object, object.getClass(), contextManager, serializationContext);
        });
    }
}
