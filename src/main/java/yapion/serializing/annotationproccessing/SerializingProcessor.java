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

package yapion.serializing.annotationproccessing;

import lombok.SneakyThrows;
import yapion.annotations.api.ProcessorImplementation;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.registration.YAPIONSerializing;
import yapion.annotations.serialize.YAPIONOptimize;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.output.FileOutput;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.annotationproccessing.serializingdata.ClassData;
import yapion.serializing.annotationproccessing.serializingdata.FieldData;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@ProcessorImplementation
@SupportedAnnotationTypes("yapion.annotations.registration.YAPIONSerializing")
public class SerializingProcessor extends AbstractProcessor {

    private Messager messager;
    private Set<ClassData> classDatas = new HashSet<>();

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @SneakyThrows
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @SneakyThrows
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(YAPIONSerializing.class);
        for (Element element : elements) {
            ClassData classData = new ClassData();
            if (element.getKind() != ElementKind.CLASS) {
                error(element, "Element needs to be class");
                continue;
            }

            YAPIONSerializing yapionSerializing = element.getAnnotation(YAPIONSerializing.class);
            classData.setSerializerMethods(yapionSerializing.serializationStep());
            classData.setDeserializerMethods(yapionSerializing.deserializationStep());

            TypeElement clazz = (TypeElement) element;
            messager.printMessage(Diagnostic.Kind.NOTE, clazz.getQualifiedName());
            if (clazz.getEnclosingElement().getKind() == ElementKind.CLASS) {
                error(element, "Element cannot be inner class");
            }

            List<VariableElement> elementList = new ArrayList<>();
            Element current = element;
            boolean unknownSuper = false;
            AtomicBoolean initNeeded = new AtomicBoolean(false);
            while (current != null && current.getKind() == ElementKind.CLASS) {
                elementList.addAll(current.getEnclosedElements()
                        .stream()
                        .filter(VariableElement.class::isInstance)
                        .map(VariableElement.class::cast)
                        .filter(e -> !(e.getModifiers().contains(Modifier.STATIC) || element.getModifiers().contains(Modifier.TRANSIENT)))
                        .collect(Collectors.toList()));
                Element finalCurrent = current;
                TypeMirror typeMirror = ((TypeElement) finalCurrent).getSuperclass();
                if (typeMirror.toString().equals("java.lang.Object")) {
                    break;
                }
                if (typeMirror.getKind() == TypeKind.NONE) {
                    break;
                }
                current = roundEnv.getRootElements().stream().filter(e -> e.toString().equals(typeMirror.toString())).findFirst().orElse(null);
                if (current == null) {
                    unknownSuper = true;
                }
            }

            elementList.forEach(variableElement -> {
                int modifier = variableElement.getModifiers().stream().map(javax.lang.model.element.Modifier::name).mapToInt(s -> {
                    try {
                        return java.lang.reflect.Modifier.class.getDeclaredField(s).getInt(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }).reduce((left, right) -> left | right).orElse(0);

                FieldData fieldData = new FieldData();
                fieldData.setFieldName(variableElement.getSimpleName().toString());
                fieldData.setModifiers(modifier);
                classData.getFieldDataList().add(fieldData);

                {
                    List<String> contexts = new ArrayList<>();
                    YAPIONOptimize[] yapionOptimizes = variableElement.getAnnotationsByType(YAPIONOptimize.class);
                    if (yapionOptimizes.length != 0) {
                        for (YAPIONOptimize yapionOptimize : yapionOptimizes) {
                            contexts.addAll(Arrays.asList(yapionOptimize.context()));
                        }
                        fieldData.setOptimize(contexts.toArray(new String[0]));
                    }
                }
                {
                    List<String> contexts = new ArrayList<>();
                    YAPIONSaveExclude[] yapionSaveExcludes = variableElement.getAnnotationsByType(YAPIONSaveExclude.class);
                    if (yapionSaveExcludes.length != 0) {
                        for (YAPIONSaveExclude yapionSaveExclude : yapionSaveExcludes) {
                            contexts.addAll(Arrays.asList(yapionSaveExclude.context()));
                        }
                        fieldData.setSaveExclude(contexts.toArray(new String[0]));
                    }
                    classData.setSerializerContextManager(true);
                }
                {
                    List<String> contexts = new ArrayList<>();
                    YAPIONLoadExclude[] yapionLoadExcludes = variableElement.getAnnotationsByType(YAPIONLoadExclude.class);
                    if (yapionLoadExcludes.length != 0) {
                        for (YAPIONLoadExclude yapionLoadExclude : yapionLoadExcludes) {
                            contexts.addAll(Arrays.asList(yapionLoadExclude.context()));
                        }
                        fieldData.setLoadExclude(contexts.toArray(new String[0]));
                    }
                    if (java.lang.reflect.Modifier.isFinal(modifier) && !contexts.isEmpty()) {
                        initNeeded.set(true);
                    }
                    classData.setDeserializerContextManager(true);
                }
            });

            if (initNeeded.get()) {
                classData.setInitNeeded(true);
            }
            classData.setUnknownSuper(unknownSuper);
            classData.setQualifiedName(clazz.getQualifiedName().toString());
            classData.setSimpleName(clazz.getSimpleName().toString());
            classDatas.add(classData);
        }
        YAPIONSerializer.serialize(classDatas).toYAPION(new FileOutput(new File("./YAPIONAnnotationProcessor.yapion"), true));
        return true;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
}

