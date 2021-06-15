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

            TypeElement clazz = (TypeElement) element;
            if (clazz.getEnclosingElement().getKind() == ElementKind.CLASS) {
                error(element, "Element cannot be inner class");
            }
            // JavaFileObject f = processingEnv.getFiler().createSourceFile(clazz.getQualifiedName().toString().substring(0, clazz.getQualifiedName().toString().lastIndexOf('.')) + "." + clazz.getSimpleName() + "$" + clazz.getSimpleName() + "Serializer");
            JavaFileObject f = processingEnv.getFiler().createSourceFile(clazz.getQualifiedName() + "Serializer");
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Creating " + f.toUri());

            List<VariableElement> elementList = new ArrayList<>();
            Element current = element;
            while (current != null && current.getKind() == ElementKind.CLASS) {
                elementList.addAll(current.getEnclosedElements()
                        .stream()
                        .filter(VariableElement.class::isInstance)
                        .map(VariableElement.class::cast)
                        .filter(e -> !(e.getModifiers().contains(Modifier.STATIC) || element.getModifiers().contains(Modifier.TRANSIENT)))
                        .collect(Collectors.toList()));
                Element finalCurrent = current;
                current = roundEnv.getRootElements().stream().filter(e -> e.toString().equals(((TypeElement) finalCurrent).getSuperclass().toString())).findFirst().orElse(null);
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
                    if (serializeFieldList.isEmpty() && deserializeFieldList.isEmpty()) {
                        continue;
                    }
                    for (VariableElement e : serializeFieldList) {
                        pw.println("        " + e.getSimpleName() + " = loadField(\"" + e.getSimpleName() + "\");");
                    }
                    for (VariableElement e : deserializeFieldList) {
                        if (!serializeFieldList.contains(e)) {
                            pw.println("        " + e.getSimpleName() + " = loadField(\"" + e.getSimpleName() + "\");");
                        }
                    }
                    continue;
                }
                if (s.equals("%FIELDS_LOAD%")) {
                    if (serializeFieldList.isEmpty() && deserializeFieldList.isEmpty()) {
                        continue;
                    }
                    pw.println("    private Field loadField(String fieldName) {");
                    pw.println("        try {");
                    pw.println("            return getField(" + clazz.getSimpleName() + ".class, fieldName);");
                    pw.println("        } catch (Exception e) {");
                    pw.println("            throw new YAPIONReflectionException(e.getMessage(), e);");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println("");
                    continue;
                }
                if (s.equals("%SERIALIZATION%")) {
                    for (VariableElement e : elementList) {
                        generateFieldSerializer(pw, e, serializeFieldList.contains(e));
                    }
                    continue;
                }
                if (s.equals("%DESERIALIZATION%")) {
                    for (VariableElement e : elementList) {
                        generateFieldDeserializer(pw, e, deserializeFieldList.contains(e));
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

    private void generateFieldSerializer(PrintWriter printWriter, VariableElement e, boolean reflective) {
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
        if (yapionOptimize.length != 0) {
            if (!yapionOptimizeContext.isEmpty()) {
                conditions.add("contextManager.is(" + yapionOptimizeContext + ")");
            }
            conditions.add(e.getSimpleName() + "Object != null");
            b = true;
        }
        if (yapionSaveExclude.length != 0) {
            conditions.add("!contextManager.is(" + yapionSaveExcludeContext + ")");
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

    private void generateFieldDeserializer(PrintWriter printWriter, VariableElement e, boolean reflective) {
        YAPIONLoadExclude[] yapionLoadExclude = e.getAnnotationsByType(YAPIONLoadExclude.class);
        String yapionSaveExcludeContext = Arrays.stream(yapionLoadExclude).flatMap(o -> Arrays.stream(o.context())).distinct().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));
        if (yapionSaveExcludeContext.isEmpty() && yapionLoadExclude.length != 0) {
            return;
        }
        List<String> conditions = new ArrayList<>();
        int indent = 0;
        if (yapionLoadExclude.length != 0) {
            conditions.add("!contextManager.is(" + yapionSaveExcludeContext + ")");
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

