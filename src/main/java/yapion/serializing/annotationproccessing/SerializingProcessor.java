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
import yapion.hierarchy.output.Indentator;
import yapion.serializing.InternalSerializer;
import yapion.serializing.annotationproccessing.generator.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@ProcessorImplementation
@SupportedAnnotationTypes("yapion.annotations.registration.YAPIONSerializing")
public class SerializingProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

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
            if (element.getKind() != ElementKind.CLASS) {
                error(element, "Element needs to be class");
                continue;
            }

            YAPIONSerializing yapionSerializing = element.getAnnotation(YAPIONSerializing.class);
            AtomicBoolean serializerContextManager = new AtomicBoolean(yapionSerializing.serializationStep());
            AtomicBoolean deserializerContextManager = new AtomicBoolean(yapionSerializing.deserializationStep());

            TypeElement clazz = (TypeElement) element;
            if (clazz.getEnclosingElement().getKind() == ElementKind.CLASS) {
                error(element, "Element cannot be inner class");
            }

            List<VariableElement> elementList = new ArrayList<>();
            Element current = element;
            boolean unknownSuper = false;
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

            List<VariableElement> serializeFieldList = elementList.stream()
                    .filter(e -> {
                        if (e.getModifiers().contains(Modifier.PRIVATE)) return true;
                        String s = clazz.getQualifiedName().toString();
                        if (!e.getEnclosingElement().toString().startsWith(s.substring(0, s.length() - clazz.getSimpleName().toString().length()))) {
                            return !e.getModifiers().contains(Modifier.PUBLIC);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

            List<VariableElement> deserializeFieldList = elementList.stream()
                    .filter(e -> {
                        if (e.getModifiers().contains(Modifier.PRIVATE)) return true;
                        if (e.getModifiers().contains(Modifier.FINAL)) return true;
                        String s = clazz.getQualifiedName().toString();
                        if (!e.getEnclosingElement().toString().startsWith(s.substring(0, s.length() - clazz.getSimpleName().toString().length()))) {
                            return !e.getModifiers().contains(Modifier.PUBLIC);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

            ClassGenerator classGenerator = new ClassGenerator(clazz.getQualifiedName().toString().substring(0, clazz.getQualifiedName().toString().lastIndexOf('.')), clazz.getSimpleName().toString() + "Serializer");
            classGenerator.addInterface(InternalSerializer.class.getTypeName() + "<" + clazz.getSimpleName() + ">");
            classGenerator.add(new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE), null, clazz.getSimpleName().toString() + "Serializer"));
            classGenerator.addImport("yapion.hierarchy.api.groups.YAPIONAnyType");
            classGenerator.addImport("yapion.hierarchy.types.YAPIONObject");
            classGenerator.addImport("yapion.serializing.InternalSerializer");
            classGenerator.addImport("yapion.serializing.data.DeserializeData");
            classGenerator.addImport("yapion.serializing.data.SerializeData");

            FunctionGenerator typeFunction = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "type", "Class<" + clazz.getSimpleName().toString() + ">");
            typeFunction.add("return " + clazz.getSimpleName().toString() + ".class;");
            typeFunction.addAnnotation(Override.class);
            classGenerator.add(typeFunction);

            FunctionGenerator initFunction = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "init", "void");
            initFunction.addAnnotation(Override.class);
            classGenerator.add(initFunction);

            FunctionGenerator serializeFunction = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "serialize", "YAPIONAnyType", new ParameterGenerator("SerializeData<" + clazz.getSimpleName().toString() + ">", "serializeData"));
            serializeFunction.addAnnotation(Override.class);
            classGenerator.add(serializeFunction);

            FunctionGenerator deserializeFunction = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "deserialize", clazz.getSimpleName().toString(), new ParameterGenerator("DeserializeData<? extends YAPIONAnyType>", "deserializeData"));
            deserializeFunction.addAnnotation(Override.class);
            classGenerator.add(deserializeFunction);

            if (!serializeFieldList.isEmpty() || !deserializeFieldList.isEmpty()) {
                classGenerator.addImport("java.lang.reflect.Field");
            }
            for (VariableElement e : serializeFieldList) {
                classGenerator.add(new FieldGenerator("Field", e.getSimpleName().toString(), null));
            }
            for (VariableElement e : deserializeFieldList) {
                if (!serializeFieldList.contains(e)) {
                    classGenerator.add(new FieldGenerator("Field", e.getSimpleName().toString(), null));
                }
            }

            if (!((elementList.isEmpty() && !unknownSuper) || (serializeFieldList.isEmpty() && deserializeFieldList.isEmpty()))) {
                initFunction.add("initFields();");
            }
            if (!((elementList.isEmpty() && !unknownSuper) || (serializeFieldList.isEmpty() && deserializeFieldList.isEmpty()))) {
                FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE), "initFields", "void");
                if (unknownSuper) {
                    functionGenerator.add("handledFields = new HashSet<>();");
                    for (VariableElement e : elementList) {
                        if (!serializeFieldList.contains(e) && !deserializeFieldList.contains(e)) {
                            functionGenerator.add("loadField(\"" + e.getSimpleName() + "\");");
                        }
                    }
                }
                for (VariableElement e : serializeFieldList) {
                    functionGenerator.add(e.getSimpleName() + " = loadField(\"" + e.getSimpleName() + "\");");
                }
                for (VariableElement e : deserializeFieldList) {
                    if (!serializeFieldList.contains(e)) {
                        functionGenerator.add(e.getSimpleName() + " = loadField(\"" + e.getSimpleName() + "\");");
                    }
                }
                classGenerator.add(functionGenerator);
            }

            if (unknownSuper) {
                classGenerator.addImport("java.lang.reflect.Field");
                classGenerator.addImport("java.util.HashSet");
                classGenerator.add(new FieldGenerator("Set<Field>", "handledFields", "null"));
                classGenerator.add(new FieldGenerator("Set<Field>", "sFields", "null"));
                classGenerator.add(new FieldGenerator("Set<Field>", "dFields", "null"));
            }

            if (!((elementList.isEmpty() && !unknownSuper) || (serializeFieldList.isEmpty() && deserializeFieldList.isEmpty()))) {
                FunctionGenerator loadFields = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE), "loadField", "Field", new ParameterGenerator("String", "fieldName"));
                classGenerator.addImport("java.lang.reflect.Field");
                classGenerator.addImport("yapion.exceptions.utils.YAPIONReflectionException");
                classGenerator.addImport("static yapion.utils.ReflectionsUtils.getField");
                loadFields.add("try {");
                if (unknownSuper) {
                    loadFields.add("    Field f = getField(" + clazz.getSimpleName() + ".class, fieldName);");
                    loadFields.add("    handledFields.add(f);");
                    loadFields.add("    return f;");
                } else {
                    loadFields.add("    return getField(" + clazz.getSimpleName() + ".class, fieldName);");
                }
                loadFields.add("} catch (Exception e) {");
                loadFields.add("    throw new YAPIONReflectionException(e.getMessage(), e);");
                loadFields.add("}");
                classGenerator.add(loadFields);
            }

            if (unknownSuper) {
                classGenerator.addImport("static yapion.utils.ReflectionsUtils.getFields");
                classGenerator.addImport("java.util.HashSet");
                classGenerator.addImport("java.lang.reflect.Modifier");
                initFunction.add("    sFields = new HashSet<>();");
                initFunction.add("    dFields = new HashSet<>();");
                initFunction.add("    getFields(type()).forEach(f -> {");
                initFunction.add("        if (Modifier.isStatic(f.getModifiers())) return;");
                initFunction.add("        if (handledFields.contains(f)) return;");
                initFunction.add("        if (Modifier.isPrivate(f.getModifiers())) {");
                initFunction.add("            sFields.add(f);");
                initFunction.add("            dFields.add(f);");
                initFunction.add("            return;");
                initFunction.add("        }");
                initFunction.add("        if (Modifier.isFinal(f.getModifiers())) {");
                initFunction.add("            dFields.add(f);");
                initFunction.add("            return;");
                initFunction.add("        }");
                initFunction.add("        if (f.getDeclaringClass() != type()) {");
                initFunction.add("            sFields.add(f);");
                initFunction.add("            dFields.add(f);");
                initFunction.add("        }");
                initFunction.add("    });");
            }

            if (yapionSerializing.serializationStep()) {
                classGenerator.addImport("yapion.serializing.ContextManager");
                classGenerator.addImport("yapion.serializing.MethodManager");
                serializerContextManager.set(true);
                serializeFunction.add("ContextManager contextManager = new ContextManager(serializeData.context);");
                serializeFunction.add("MethodManager.preSerializationStep(serializeData.object, type(), contextManager);");
            }
            serializeFunction.add("YAPIONObject yapionObject = new YAPIONObject(type());");
            for (VariableElement e : elementList) {
                generateFieldSerializer(serializeFunction, e, serializeFieldList.contains(e), serializerContextManager);
            }
            if (unknownSuper) {
                serializeFunction.add("for (Field f : sFields) {");
                serializeFunction.add("    if (contextManager.is(f.getAnnotationsByType(YAPIONSaveExclude.class))) continue;");
                serializeFunction.add("    if (contextManager.is(f.getAnnotationsByType(YAPIONOptimize.class))) {");
                serializeFunction.add("        Object fObject = serializeData.getField(f);");
                serializeFunction.add("        if (fObject != null) yapionObject.add(f.getName(), serializeData.serialize(fObject));");
                serializeFunction.add("    } else {");
                serializeFunction.add("        serializeData.serialize(yapionObject, f);");
                serializeFunction.add("    }");
                serializeFunction.add("}");
            }
            if (yapionSerializing.serializationStep()) {
                serializeFunction.add("MethodManager.postSerializationStep(serializeData.object, type(), contextManager);");
            }
            serializeFunction.add("return yapionObject;");

            deserializeFunction.add(clazz.getSimpleName() + " object = deserializeData.getInstanceByFactoryOrObjenesis(type());");
            if (yapionSerializing.deserializationStep()) {
                classGenerator.addImport("yapion.serializing.ContextManager");
                classGenerator.addImport("yapion.serializing.MethodManager");
                deserializerContextManager.set(true);
                deserializeFunction.add("ContextManager contextManager = new ContextManager(deserializeData.context);");
                deserializeFunction.add("MethodManager.preDeserializationStep(object, type(), contextManager);");
            }
            for (VariableElement e : elementList) {
                generateFieldDeserializer(deserializeFunction, e, deserializeFieldList.contains(e), deserializerContextManager);
            }
            if (unknownSuper) {
                deserializeFunction.add("for (Field f : dFields) {");
                deserializeFunction.add("    if (contextManager.is(f.getAnnotationsByType(YAPIONLoadExclude.class))) continue;");
                deserializeFunction.add("    deserializeData.deserialize(object, f);");
                deserializeFunction.add("}");
            }
            if (yapionSerializing.deserializationStep()) {
                classGenerator.addImport("yapion.serializing.ContextManager");
                classGenerator.addImport("yapion.serializing.MethodManager");
                deserializeFunction.add("MethodManager.postDeserializationStep(object, type(), contextManager);");
            }
            deserializeFunction.add("return object;");

            JavaFileObject f = processingEnv.getFiler().createSourceFile(clazz.getQualifiedName() + "Serializer");
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Creating " + f.toUri());
            PrintWriter pw = new PrintWriter(f.openWriter());
            classGenerator.output(0).forEach(pw::println);
            pw.close();
        }
        return true;
    }

    private void generateFieldSerializer(FunctionGenerator functionGenerator, VariableElement e, boolean reflective, AtomicBoolean contextManager) {
        YAPIONOptimize[] yapionOptimize = e.getAnnotationsByType(YAPIONOptimize.class);
        String yapionOptimizeContext = Arrays.stream(yapionOptimize).flatMap(o -> Arrays.stream(o.context())).distinct().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));

        YAPIONSaveExclude[] yapionSaveExclude = e.getAnnotationsByType(YAPIONSaveExclude.class);
        String yapionSaveExcludeContext = Arrays.stream(yapionSaveExclude).flatMap(o -> Arrays.stream(o.context())).distinct().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));
        if (yapionSaveExcludeContext.isEmpty() && yapionSaveExclude.length != 0) {
            return;
        }

        List<String> conditions = new ArrayList<>();
        boolean b = false;
        boolean contextManagerNeeded = false;
        if (yapionOptimize.length != 0) {
            if (!yapionOptimizeContext.isEmpty()) {
                conditions.add("contextManager.is(" + yapionOptimizeContext + ")");
                contextManagerNeeded = true;
            }
            conditions.add(e.getSimpleName() + "Object != null");
            b = true;
        }
        if (yapionSaveExclude.length != 0) {
            conditions.add("!contextManager.is(" + yapionSaveExcludeContext + ")");
            contextManagerNeeded = true;
        }
        if (contextManagerNeeded && !contextManager.get()) {
            functionGenerator.add("ContextManager contextManager = new ContextManager(serializeData.context);");
            contextManager.set(true);
        }
        if (b) {
            if (reflective) {
                functionGenerator.add("Object " + e.getSimpleName() + "Object = serializeData.getField(" + e.getSimpleName() + ");");
            } else {
                functionGenerator.add("Object " + e.getSimpleName() + "Object = serializeData.object." + e.getSimpleName() + ";");
            }
        }
        if (!conditions.isEmpty()) {
            functionGenerator.add("if (" + String.join(" && ", conditions) + ") {");
        }
        if (b) {
            functionGenerator.add("yapionObject.add(\"" + e.getSimpleName() + "\", serializeData.serialize(" + e.getSimpleName() + "Object));");
        } else {
            if (reflective) {
                functionGenerator.add( "serializeData.serialize(yapionObject, " + e.getSimpleName() + ");");
            } else {
                functionGenerator.add( "yapionObject.add(\"" + e.getSimpleName() + "\", serializeData.serialize(serializeData.object." + e.getSimpleName() + "));");
            }
        }
        if (!conditions.isEmpty()) {
            functionGenerator.add("}");
        }
    }

    private void generateFieldDeserializer(FunctionGenerator functionGenerator, VariableElement e, boolean reflective, AtomicBoolean contextManager) {
        YAPIONLoadExclude[] yapionLoadExclude = e.getAnnotationsByType(YAPIONLoadExclude.class);
        String yapionSaveExcludeContext = Arrays.stream(yapionLoadExclude).flatMap(o -> Arrays.stream(o.context())).distinct().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));
        if (yapionSaveExcludeContext.isEmpty() && yapionLoadExclude.length != 0) {
            return;
        }
        List<String> conditions = new ArrayList<>();
        boolean contextManagerNeeded = false;
        if (yapionLoadExclude.length != 0) {
            conditions.add("!contextManager.is(" + yapionSaveExcludeContext + ")");
            contextManagerNeeded = true;
        }
        if (contextManagerNeeded && !contextManager.get()) {
            functionGenerator.add("ContextManager contextManager = new ContextManager(serializeData.context);");
            contextManager.set(true);
        }
        if (!conditions.isEmpty()) {
            functionGenerator.add("if (" + String.join(" && ", conditions) + ") {");
        }
        if (reflective) {
            functionGenerator.add( "deserializeData.deserialize(object, " + e.getSimpleName() + ");");
        } else {
            functionGenerator.add( "object." + e.getSimpleName() + " = deserializeData.deserialize(\"" + e.getSimpleName() + "\");");
        }
        if (!conditions.isEmpty()) {
            functionGenerator.add( "}");
        }
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
}

