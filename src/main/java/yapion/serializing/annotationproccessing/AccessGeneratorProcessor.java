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
import lombok.ToString;
import yapion.annotations.api.ProcessorImplementation;
import yapion.annotations.registration.YAPIONAccessGenerator;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONPointer;
import yapion.hierarchy.types.YAPIONValue;
import yapion.parser.YAPIONParser;
import yapion.serializing.annotationproccessing.generator.*;
import yapion.utils.ClassUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

@ProcessorImplementation
@SupportedAnnotationTypes("yapion.annotations.registration.YAPIONAccessGenerator")
public class AccessGeneratorProcessor extends AbstractProcessor {

    private Messager messager;
    private AtomicInteger index = new AtomicInteger(0);
    private AtomicBoolean lombokToString = new AtomicBoolean(false);

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
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(YAPIONAccessGenerator.class);
        for (Element element : elements) {
            if (element.getKind() != ElementKind.FIELD) {
                error(element, "Element needs to be field");
                continue;
            }
            if (!element.getModifiers().contains(Modifier.FINAL)) {
                error(element, "Element needs to be final");
                continue;
            }

            YAPIONAccessGenerator yapionAccessGenerator = element.getAnnotation(YAPIONAccessGenerator.class);
            lombokToString.set(yapionAccessGenerator.lombokToString());

            TypeElement clazz = (TypeElement) element.getEnclosingElement();
            String packageName = clazz.getQualifiedName().toString();
            packageName = packageName.substring(0, packageName.lastIndexOf('.'));

            VariableElement variableElement = (VariableElement) element;
            YAPIONObject yapionObject = YAPIONParser.parse(variableElement.getConstantValue().toString());
            if (!yapionObject.containsKey("@name", String.class)) {
                error(element, "Root Element needs to have a proper name. Use '@name' to specify one in the YAPIONObject");
                continue;
            }
            index.set(0);
            ClassGenerator classGenerator = generate(element, yapionObject, packageName);
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE, ModifierType.STATIC), "get", "<T> T", new ParameterGenerator(YAPIONObject.class.getTypeName(), "data"), new ParameterGenerator("String", "name"));
            functionGenerator.add("return data.getPlainValueOrDefault(name, null);");
            classGenerator.add(functionGenerator);

            functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE, ModifierType.STATIC), "getNonNull", "<T> T", new ParameterGenerator(YAPIONObject.class.getTypeName(), "data"), new ParameterGenerator("String", "name"));
            functionGenerator.add("T value = get(data, name);");
            functionGenerator.add("if (value == null) throw new yapion.exceptions.YAPIONException(\"Null Value not allowed\");");
            functionGenerator.add("return value;");
            classGenerator.add(functionGenerator);

            functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE, ModifierType.STATIC), "getObject", "<T> T", new ParameterGenerator(YAPIONObject.class.getTypeName(), "data"), new ParameterGenerator("String", "name"), new ParameterGenerator(BiFunction.class.getTypeName() + "<" + YAPIONObject.class.getTypeName() + ", java.util.Map<yapion.hierarchy.api.groups.YAPIONDataType<?, ?>, Object>, T>", "mapper"), new ParameterGenerator("java.util.Map<yapion.hierarchy.api.groups.YAPIONDataType<?, ?>, Object>", "_pointerData"));
            functionGenerator.add("if (data.containsKey(name, yapion.hierarchy.types.YAPIONType.POINTER)) {");
            functionGenerator.add("    return (T) _pointerData.get(data.getPointer(name));");
            functionGenerator.add("}");
            functionGenerator.add(YAPIONObject.class.getTypeName() + " value = data.getObjectOrDefault(name, null);");
            functionGenerator.add("if (value == null) return null;");
            functionGenerator.add("T returnValue = mapper.apply(value, _pointerData);");
            functionGenerator.add("_pointerData.put(value, returnValue);");
            functionGenerator.add("return returnValue;");
            classGenerator.add(functionGenerator);

            functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE, ModifierType.STATIC), "getNonNullObject", "<T> T", new ParameterGenerator(YAPIONObject.class.getTypeName(), "data"), new ParameterGenerator("String", "name"), new ParameterGenerator(BiFunction.class.getTypeName() + "<" + YAPIONObject.class.getTypeName() + ", java.util.Map<yapion.hierarchy.api.groups.YAPIONDataType<?, ?>, Object>, T>", "mapper"), new ParameterGenerator("java.util.Map<yapion.hierarchy.api.groups.YAPIONDataType<?, ?>, Object>", "_pointerData"));
            functionGenerator.add("if (data.containsKey(name, yapion.hierarchy.types.YAPIONType.POINTER)) {");
            functionGenerator.add("    return (T) _pointerData.get(data.getPointer(name));");
            functionGenerator.add("}");
            functionGenerator.add(YAPIONObject.class.getTypeName() + " value = data.getObjectOrDefault(name, null);");
            functionGenerator.add("if (value == null) throw new yapion.exceptions.YAPIONException(\"Null Value not allowed\");");
            functionGenerator.add("T returnValue = mapper.apply(value, _pointerData);");
            functionGenerator.add("_pointerData.put(value, returnValue);");
            functionGenerator.add("return returnValue;");
            classGenerator.add(functionGenerator);

            functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE, ModifierType.STATIC), "getArray", "<T> T[]", new ParameterGenerator(YAPIONObject.class.getTypeName(), "data"), new ParameterGenerator("String", "name"), new ParameterGenerator("Class<?>", "clazz"), new ParameterGenerator(BiFunction.class.getTypeName() + "<" + YAPIONObject.class.getTypeName() + ", java.util.Map<yapion.hierarchy.api.groups.YAPIONDataType<?, ?>, Object>, T>", "mapper"), new ParameterGenerator("java.util.Map<yapion.hierarchy.api.groups.YAPIONDataType<?, ?>, Object>", "_pointerData"));
            functionGenerator.add("if (data.containsKey(name, yapion.hierarchy.types.YAPIONType.POINTER)) {");
            functionGenerator.add("    return (T[]) _pointerData.get(data.getPointer(name));");
            functionGenerator.add("}");
            functionGenerator.add(YAPIONArray.class.getTypeName() + " value = data.getArrayOrDefault(name, null);");
            functionGenerator.add("if (value == null) return null;");
            functionGenerator.add("java.util.List<T> list = value.stream().map(yapionAnyType -> {");
            functionGenerator.add("    if (yapionAnyType instanceof " + YAPIONPointer.class.getTypeName() + ") return (T) _pointerData.get(((" + YAPIONPointer.class.getTypeName() + ") yapionAnyType).get());");
            functionGenerator.add("    if (" + ClassUtils.class.getTypeName() + ".isPrimitive(clazz)) return (T) ((" + YAPIONValue.class.getTypeName() + "<?>) yapionAnyType).get();");
            functionGenerator.add("    return mapper.apply((" + YAPIONObject.class.getTypeName() + ") yapionAnyType, _pointerData);");
            functionGenerator.add("}).collect(java.util.stream.Collectors.toList());");
            functionGenerator.add("Object array = java.lang.reflect.Array.newInstance(clazz, list.size());");
            functionGenerator.add("for (int i = 0; i < list.size(); i++) {");
            functionGenerator.add("    java.lang.reflect.Array.set(array, i, list.get(i));");
            functionGenerator.add("}");
            functionGenerator.add("_pointerData.put(value, array);");
            functionGenerator.add("return (T[]) array;");
            classGenerator.add(functionGenerator);

            functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE, ModifierType.STATIC), "getNonNullArray", "<T> T[]", new ParameterGenerator(YAPIONObject.class.getTypeName(), "data"), new ParameterGenerator("String", "name"), new ParameterGenerator("Class<?>", "clazz"), new ParameterGenerator(BiFunction.class.getTypeName() + "<" + YAPIONObject.class.getTypeName() + ", java.util.Map<yapion.hierarchy.api.groups.YAPIONDataType<?, ?>, Object>, T>", "mapper"), new ParameterGenerator("java.util.Map<yapion.hierarchy.api.groups.YAPIONDataType<?, ?>, Object>", "_pointerData"));
            functionGenerator.add("if (data.containsKey(name, yapion.hierarchy.types.YAPIONType.POINTER)) {");
            functionGenerator.add("    return (T[]) _pointerData.get(data.getPointer(name));");
            functionGenerator.add("}");
            functionGenerator.add(YAPIONArray.class.getTypeName() + " value = data.getArrayOrDefault(name, null);");
            functionGenerator.add("if (value == null) throw new yapion.exceptions.YAPIONException(\"Null Value not allowed\");");
            functionGenerator.add("java.util.List<T> list = value.stream().map(yapionAnyType -> {");
            functionGenerator.add("    if (yapionAnyType instanceof " + YAPIONPointer.class.getTypeName() + ") return (T) _pointerData.get(((" + YAPIONPointer.class.getTypeName() + ") yapionAnyType).get());");
            functionGenerator.add("    if (" + ClassUtils.class.getTypeName() + ".isPrimitive(clazz)) return (T) ((" + YAPIONValue.class.getTypeName() + "<?>) yapionAnyType).get();");
            functionGenerator.add("    return mapper.apply((" + YAPIONObject.class.getTypeName() + ") yapionAnyType, _pointerData);");
            functionGenerator.add("}).collect(java.util.stream.Collectors.toList());");
            functionGenerator.add("Object array = java.lang.reflect.Array.newInstance(clazz, list.size());");
            functionGenerator.add("for (int i = 0; i < list.size(); i++) {");
            functionGenerator.add("    java.lang.reflect.Array.set(array, i, list.get(i));");
            functionGenerator.add("}");
            functionGenerator.add("_pointerData.put(value, array);");
            functionGenerator.add("return (T[]) array;");
            classGenerator.add(functionGenerator);

            JavaFileObject f = processingEnv.getFiler().createSourceFile(classGenerator.getPackageName() + "." + classGenerator.getClassName());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Creating " + f.toUri());
            PrintWriter pw = new PrintWriter(f.openWriter());
            classGenerator.output(0).forEach(pw::println);
            pw.close();
        }
        return true;
    }

    private ClassGenerator generate(Element element, YAPIONObject yapionObject, String packageName) {
        if (!yapionObject.containsKey("@name", String.class)) {
            yapionObject.put("@name", "_" + index.getAndIncrement());
        } else {
            String s = yapionObject.getPlainValue("@name");
            if (s.matches("_\\d+")) {
                error(element, "names matching '_\\d+' are not allowed");
                throw new SecurityException();
            }
        }
        ClassGenerator classGenerator = new ClassGenerator(packageName, yapionObject.getPlainValue("@name"));
        if (yapionObject.getDepth() == 0) {
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, classGenerator.getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "data"));
            functionGenerator.add("this(data, new " + HashMap.class.getTypeName() + "<>());");
            classGenerator.add(functionGenerator);
        }
        if (lombokToString.get()) {
            classGenerator.addAnnotation(ToString.class);
        }
        FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE), null, classGenerator.getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "data"), new ParameterGenerator("java.util.Map<yapion.hierarchy.api.groups.YAPIONDataType<?, ?>, Object>", "_pointerData"));
        classGenerator.add(functionGenerator);
        if (yapionObject.getDepth() != 0) {
            classGenerator.setModifierGenerator(new ModifierGenerator(ModifierType.PUBLIC, ModifierType.STATIC));
        }
        yapionObject.forEach((s, yapionAnyType) -> {
            if (s.equals("@name")) {
                return;
            }
            if (s.endsWith("!")) {
                generate(s.substring(0, s.length() - 1), yapionAnyType, element, classGenerator, functionGenerator, true);
            } else {
                generate(s, yapionAnyType, element, classGenerator, functionGenerator, false);
            }
        });
        return classGenerator;
    }

    private void generate(String s, YAPIONAnyType yapionAnyType, Element element, ClassGenerator classGenerator, FunctionGenerator functionGenerator, boolean forceNonNull) {
        if (yapionAnyType instanceof YAPIONValue) {
            String valueType = ((YAPIONValue<?>) yapionAnyType).getValueType();
            if (valueType.equals("null")) {
                valueType = "java.lang.Object";
            }
            classGenerator.add(new FieldGenerator(valueType, s, null), true, false, false);
            functionGenerator.add("this." + s + " = get" + (forceNonNull ? "NonNull" : "") + "(data, \"" + s + "\");");
        } else if (yapionAnyType instanceof YAPIONObject) {
            YAPIONObject current = (YAPIONObject) yapionAnyType;
            if (current.size() == 1 && current.containsKey("@reference", String.class)) {
                String name = current.getPlainValue("@reference");
                classGenerator.add(new FieldGenerator(name, s, null), true, false, false);
                functionGenerator.add("this." + s + " = get" + (forceNonNull ? "NonNull" : "") + "Object(data, \"" + s + "\", " + name + "::new, _pointerData);");
            } else {
                ClassGenerator currentGenerator = generate(element, current, "");
                classGenerator.add(currentGenerator);
                classGenerator.add(new FieldGenerator(currentGenerator.getClassName(), s, null), true, false, false);
                functionGenerator.add("this." + s + " = get" + (forceNonNull ? "NonNull" : "") + "Object(data, \"" + s + "\", " + currentGenerator.getClassName() + "::new, _pointerData);");
            }
        } else if (yapionAnyType instanceof YAPIONArray) {
            YAPIONArray yapionArray = (YAPIONArray) yapionAnyType;
            if (yapionArray.isEmpty()) {
                error(element, "YAPIONArray needs to have one Element");
                return;
            }
            YAPIONAnyType current = yapionArray.getYAPIONAnyType(0);
            if (current instanceof YAPIONValue) {
                String valueType = ((YAPIONValue<?>) current).getValueType();
                if (valueType.equals("null")) {
                    valueType = "java.lang.Object";
                }
                classGenerator.add(new FieldGenerator(valueType + "[]", s, null), true, false, false);
                functionGenerator.add("this." + s + " = get" + (forceNonNull ? "NonNull" : "") + "Array(data, \"" + s + "\", " + valueType + ".class, " + valueType + "::new, _pointerData);");
            } else if (current instanceof YAPIONObject) {
                YAPIONObject current1 = (YAPIONObject) current;
                if (current1.size() == 1 && current1.containsKey("@reference", String.class)) {
                    String name = current1.getPlainValue("@reference");
                    classGenerator.add(new FieldGenerator(name + "[]", s, null), true, false, false);
                    functionGenerator.add("this." + s + " = get" + (forceNonNull ? "NonNull" : "") + "Array(data, \"" + s + "\", " + name + ".class, " + name + "::new, _pointerData);");
                } else {
                    ClassGenerator currentGenerator = generate(element, current1, "");
                    classGenerator.add(currentGenerator);
                    classGenerator.add(new FieldGenerator(currentGenerator.getClassName() + "[]", s, null), true, false, false);
                    functionGenerator.add("this." + s + " = get" + (forceNonNull ? "NonNull" : "") + "Array(data, \"" + s + "\", " + currentGenerator.getClassName() + ".class, " + currentGenerator.getClassName() + "::new, _pointerData);");
                }
            }
        }
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
}
