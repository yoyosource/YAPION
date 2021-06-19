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

import static yapion.serializing.annotationproccessing.Code.defaultSerializer;

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
            // JavaFileObject f = processingEnv.getFiler().createSourceFile(clazz.getQualifiedName().toString().substring(0, clazz.getQualifiedName().toString().lastIndexOf('.')) + "." + clazz.getSimpleName() + "$" + clazz.getSimpleName() + "Serializer");
            JavaFileObject f = processingEnv.getFiler().createSourceFile(clazz.getQualifiedName() + "Serializer");
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Creating " + f.toUri());

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

            Writer w = f.openWriter();
            PrintWriter pw = new PrintWriter(w);
            for (String s : defaultSerializer) {
                if (s.equals("%FIELDS%")) {
                    if (serializeFieldList.isEmpty() && deserializeFieldList.isEmpty()) {
                        continue;
                    }
                    for (VariableElement e : serializeFieldList) {
                        pw.println("    private Field " + e.getSimpleName() + ";");
                    }
                    for (VariableElement e : deserializeFieldList) {
                        if (!serializeFieldList.contains(e)) {
                            pw.println("    private Field " + e.getSimpleName() + ";");
                        }
                    }
                    pw.println("");
                    continue;
                }
                if (s.equals("%FIELDS_INIT%")) {
                    if ((elementList.isEmpty() && !unknownSuper) || (serializeFieldList.isEmpty() && deserializeFieldList.isEmpty())) {
                        continue;
                    }
                    pw.println("    private void initFields() {");
                    if (unknownSuper) {
                        pw.println("        handledFields = new HashSet<>();");
                    }
                    for (VariableElement e : serializeFieldList) {
                        pw.println("        " + e.getSimpleName() + " = loadField(\"" + e.getSimpleName() + "\");");
                    }
                    for (VariableElement e : deserializeFieldList) {
                        if (!serializeFieldList.contains(e)) {
                            pw.println("        " + e.getSimpleName() + " = loadField(\"" + e.getSimpleName() + "\");");
                        }
                    }
                    if (unknownSuper) {
                        for (VariableElement e : elementList) {
                            if (!serializeFieldList.contains(e) && !deserializeFieldList.contains(e)) {
                                pw.println("        loadField(\"" + e.getSimpleName() + "\");");
                            }
                        }
                    }
                    pw.println("    }");
                    pw.println();
                    continue;
                }
                if (s.equals("%FIELDS_INIT_CALL%")) {
                    if ((elementList.isEmpty() && !unknownSuper) || (serializeFieldList.isEmpty() && deserializeFieldList.isEmpty())) {
                        continue;
                    }
                    pw.println("        initFields();");
                    continue;
                }
                if (s.equals("%FIELDS_LOAD%")) {
                    if ((elementList.isEmpty() && !unknownSuper) || (serializeFieldList.isEmpty() && deserializeFieldList.isEmpty())) {
                        continue;
                    }
                    pw.println("    private Field loadField(String fieldName) {");
                    pw.println("        try {");
                    if (unknownSuper) {
                        pw.println("            Field f = getField(" + clazz.getSimpleName() + ".class, fieldName);");
                        pw.println("            handledFields.add(f);");
                        pw.println("            return f;");
                    } else {
                        pw.println("            return getField(" + clazz.getSimpleName() + ".class, fieldName);");
                    }
                    pw.println("        } catch (Exception e) {");
                    pw.println("            throw new YAPIONReflectionException(e.getMessage(), e);");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println("");
                    continue;
                }
                if (s.equals("%REFLECTION_FIELDS%")) {
                    if (unknownSuper) {
                        pw.println("    private Set<Field> handledFields = null;");
                        pw.println("    private Set<Field> sFields = null;");
                        pw.println("    private Set<Field> dFields = null;");
                        pw.println("");
                    }
                    continue;
                }
                if (s.equals("%REFLECTION%")) {
                    if (unknownSuper) {
                        pw.println("        sFields = new HashSet<>();");
                        pw.println("        dFields = new HashSet<>();");
                        pw.println("        getFields(type()).forEach(f -> {");
                        pw.println("            if (Modifier.isStatic(f.getModifiers())) return;");
                        pw.println("            if (handledFields.contains(f)) return;");
                        pw.println("            if (Modifier.isPrivate(f.getModifiers())) {");
                        pw.println("                sFields.add(f);");
                        pw.println("                dFields.add(f);");
                        pw.println("                return;");
                        pw.println("            }");
                        pw.println("            if (Modifier.isFinal(f.getModifiers())) {");
                        pw.println("                dFields.add(f);");
                        pw.println("                return;");
                        pw.println("            }");
                        pw.println("            if (f.getDeclaringClass() != type()) {");
                        pw.println("                sFields.add(f);");
                        pw.println("                dFields.add(f);");
                        pw.println("            }");
                        pw.println("        });");
                    }
                    continue;
                }
                if (s.equals("%PRE_SERIALIZATION%")) {
                    if (yapionSerializing.serializationStep()) {
                        pw.println("        ContextManager contextManager = new ContextManager(serializeData.context);");
                        pw.println("        MethodManager.preSerializationStep(serializeData.object, type(), contextManager);");
                    }
                    continue;
                }
                if (s.equals("%SERIALIZATION%")) {
                    for (VariableElement e : elementList) {
                        generateFieldSerializer(pw, e, serializeFieldList.contains(e), serializerContextManager);
                    }
                    continue;
                }
                if (s.equals("%SERIALIZATION_FIELDS%")) {
                    if (unknownSuper) {
                        pw.println("        for (Field f : sFields) {");
                        pw.println("            if (contextManager.is(f.getAnnotationsByType(YAPIONSaveExclude.class))) continue;");
                        pw.println("            if (contextManager.is(f.getAnnotationsByType(YAPIONOptimize.class))) {");
                        pw.println("                Object fObject = serializeData.getField(f);");
                        pw.println("                if (fObject != null) yapionObject.add(f.getName(), serializeData.serialize(fObject));");
                        pw.println("            } else {");
                        pw.println("                serializeData.serialize(yapionObject, f);");
                        pw.println("            }");
                        pw.println("        }");
                    }
                    continue;
                }
                if (s.equals("%POST_SERIALIZATION%")) {
                    if (yapionSerializing.serializationStep()) {
                        pw.println("        MethodManager.postSerializationStep(serializeData.object, type(), contextManager);");
                    }
                    continue;
                }
                if (s.equals("%PRE_DESERIALIZATION%")) {
                    if (yapionSerializing.deserializationStep()) {
                        pw.println("        ContextManager contextManager = new ContextManager(deserializeData.context);");
                        pw.println("        MethodManager.preDeserializationStep(object, type(), contextManager);");
                    }
                    continue;
                }
                if (s.equals("%DESERIALIZATION%")) {
                    for (VariableElement e : elementList) {
                        generateFieldDeserializer(pw, e, deserializeFieldList.contains(e), deserializerContextManager);
                    }
                    continue;
                }
                if (s.equals("%DESERIALIZATION_FIELDS%")) {
                    if (unknownSuper) {
                        pw.println("        for (Field f : dFields) {");
                        pw.println("            if (contextManager.is(f.getAnnotationsByType(YAPIONLoadExclude.class))) continue;");
                        pw.println("            deserializeData.deserialize(object, f);");
                        pw.println("        }");
                    }
                    continue;
                }
                if (s.equals("%POST_DESERIALIZATION%")) {
                    if (yapionSerializing.deserializationStep()) {
                        pw.println("        MethodManager.postDeserializationStep(object, type(), contextManager);");
                    }
                    continue;
                }
                pw.println(s.replace("%PACKAGE%", clazz.getQualifiedName().toString().substring(0, clazz.getQualifiedName().toString().lastIndexOf('.'))).replace("%NAME%", clazz.getSimpleName()).replace("%CLASS%", clazz.getQualifiedName()));
            }
            pw.flush();
            w.close();
        }
        return true;
    }

    private void generateFieldSerializer(PrintWriter printWriter, VariableElement e, boolean reflective, AtomicBoolean contextManager) {
        YAPIONOptimize[] yapionOptimize = e.getAnnotationsByType(YAPIONOptimize.class);
        String yapionOptimizeContext = Arrays.stream(yapionOptimize).flatMap(o -> Arrays.stream(o.context())).distinct().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));

        YAPIONSaveExclude[] yapionSaveExclude = e.getAnnotationsByType(YAPIONSaveExclude.class);
        String yapionSaveExcludeContext = Arrays.stream(yapionSaveExclude).flatMap(o -> Arrays.stream(o.context())).distinct().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));
        if (yapionSaveExcludeContext.isEmpty() && yapionSaveExclude.length != 0) {
            return;
        }

        List<String> conditions = new ArrayList<>();
        boolean b = false;
        int indent = 0;
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
            newLine(printWriter, indent, "ContextManager contextManager = new ContextManager(serializeData.context);");
            contextManager.set(true);
        }
        if (b) {
            if (reflective) {
                newLine(printWriter, indent, "Object " + e.getSimpleName() + "Object = serializeData.getField(" + e.getSimpleName() + ");");
            } else {
                newLine(printWriter, indent, "Object " + e.getSimpleName() + "Object = serializeData.object." + e.getSimpleName() + ";");
            }
        }
        if (!conditions.isEmpty()) {
            newLine(printWriter, indent, "if (" + String.join(" && ", conditions) + ") {");
            indent++;
        }
        if (b) {
            newLine(printWriter, indent, "yapionObject.add(\"" + e.getSimpleName() + "\", serializeData.serialize(" + e.getSimpleName() + "Object));");
        } else {
            if (reflective) {
                newLine(printWriter, indent, "serializeData.serialize(yapionObject, " + e.getSimpleName() + ");");
            } else {
                newLine(printWriter, indent, "yapionObject.add(\"" + e.getSimpleName() + "\", serializeData.serialize(serializeData.object." + e.getSimpleName() + "));");
            }
        }
        if (!conditions.isEmpty()) {
            indent--;
            newLine(printWriter, indent, "}");
        }
    }

    private void generateFieldDeserializer(PrintWriter printWriter, VariableElement e, boolean reflective, AtomicBoolean contextManager) {
        YAPIONLoadExclude[] yapionLoadExclude = e.getAnnotationsByType(YAPIONLoadExclude.class);
        String yapionSaveExcludeContext = Arrays.stream(yapionLoadExclude).flatMap(o -> Arrays.stream(o.context())).distinct().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));
        if (yapionSaveExcludeContext.isEmpty() && yapionLoadExclude.length != 0) {
            return;
        }
        List<String> conditions = new ArrayList<>();
        int indent = 0;
        boolean contextManagerNeeded = false;
        if (yapionLoadExclude.length != 0) {
            conditions.add("!contextManager.is(" + yapionSaveExcludeContext + ")");
            contextManagerNeeded = true;
        }
        if (contextManagerNeeded && !contextManager.get()) {
            newLine(printWriter, indent, "ContextManager contextManager = new ContextManager(serializeData.context);");
            contextManager.set(true);
        }
        if (!conditions.isEmpty()) {
            newLine(printWriter, indent, "if (" + String.join(" && ", conditions) + ") {");
            indent++;
        }
        if (reflective) {
            newLine(printWriter, indent, "deserializeData.deserialize(object, " + e.getSimpleName() + ");");
        } else {
            newLine(printWriter, indent, "object." + e.getSimpleName() + " = deserializeData.deserialize(\"" + e.getSimpleName() + "\");");
        }
        if (!conditions.isEmpty()) {
            indent--;
            newLine(printWriter, indent, "}");
        }
    }

    private void newLine(PrintWriter printWriter, int indent, String toAdd) {
        printWriter.println(indent(indent) + toAdd);
    }

    private String indent(int indent) {
        return Indentator.QUAD_SPACE.indent(indent + 2);
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
}

