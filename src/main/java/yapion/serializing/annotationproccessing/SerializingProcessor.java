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
import yapion.annotations.registration.YAPIONSerializing;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ProcessorImplementation
@SupportedAnnotationTypes("yapion.annotations.registration.YAPIONSerializing")
public class SerializingProcessor extends AbstractProcessor {

    private String[] defaultSerializer = new String[]{
            "package %PACKAGE%;",
            "",
            "import lombok.SneakyThrows;",
            "import yapion.hierarchy.api.groups.YAPIONAnyType;",
            "import yapion.hierarchy.types.YAPIONObject;",
            "import yapion.serializing.InternalSerializer;",
            "import yapion.serializing.data.DeserializeData;",
            "import yapion.serializing.data.SerializeData;",
            "",
            "import java.lang.reflect.Field;",
            "",
            "import static yapion.utils.ReflectionsUtils.getField;",
            "",
            "public class %NAME%Serializer implements InternalSerializer<%NAME%> {",
            "    private %NAME%Serializer() {}",
            "",
            "%FIELDS%",
            "%FIELDS_INIT%",
            "    @Override",
            "    public Class<%NAME%> type() {",
            "        return %NAME%.class;",
            "    }",
            "",
            "    @Override",
            "    public YAPIONAnyType serialize(SerializeData<%NAME%> serializeData) {",
            "        YAPIONObject yapionObject = new YAPIONObject(type());",
            "%SERIALIZATION%",
            "        return yapionObject;",
            "    }",
            "",
            "    @Override",
            "    public %NAME% deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {",
            "        %NAME% object = deserializeData.getInstanceByFactoryOrObjenesis(type());",
            "%DESERIALIZATION%",
            "        return object;",
            "    }",
            "}"
    };

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
                    pw.println("    @Override");
                    pw.println("    @SneakyThrows");
                    pw.println("    public void init() {");
                    for (VariableElement e : serializeFieldList) {
                        pw.println("        " + e.getSimpleName() + " = getField(" + clazz.getSimpleName() + ".class, \"" + e.getSimpleName() + "\");");
                    }
                    for (VariableElement e : deserializeFieldList) {
                        if (!serializeFieldList.contains(e)) {
                            pw.println("        " + e.getSimpleName() + " = getField(" + clazz.getSimpleName() + ".class, \"" + e.getSimpleName() + "\");");
                        }
                    }
                    pw.println("    }");
                    pw.println("");
                    continue;
                }
                if (s.equals("%SERIALIZATION%")) {
                    for (VariableElement e : elementList) {
                        if (serializeFieldList.contains(e)) {
                            pw.println("        serializeData.serialize(yapionObject, " + e.getSimpleName() + ");");
                        } else {
                            pw.println("        yapionObject.add(\"" + e.getSimpleName() + "\", serializeData.serialize(serializeData.object." + e.getSimpleName() + "));");
                        }
                    }
                    continue;
                }
                if (s.equals("%DESERIALIZATION%")) {
                    for (VariableElement e : elementList) {
                        if (deserializeFieldList.contains(e)) {
                            pw.println("        deserializeData.deserialize(object, " + e.getSimpleName() + ");");
                        } else {
                            pw.println("        object." + e.getSimpleName() + " = deserializeData.deserialize(\"" + e.getSimpleName() + "\");");
                        }
                    }
                    continue;
                }
                pw.println(s.replace("%PACKAGE%", clazz.getQualifiedName().toString().substring(0, clazz.getQualifiedName().toString().lastIndexOf('.'))).replace("%NAME%", clazz.getSimpleName()).replace("%CLASS%", clazz.getQualifiedName()));
            }
            pw.flush();
            w.close();
        }
        return false;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
}

