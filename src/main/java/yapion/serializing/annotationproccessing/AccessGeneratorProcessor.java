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

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import yapion.annotations.api.ProcessorImplementation;
import yapion.annotations.registration.YAPIONAccessGenerator;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONType;
import yapion.hierarchy.types.YAPIONValue;
import yapion.parser.YAPIONParser;
import yapion.serializing.annotationproccessing.generator.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ProcessorImplementation
@SupportedAnnotationTypes("yapion.annotations.registration.YAPIONAccessGenerator")
public class AccessGeneratorProcessor extends AbstractProcessor {

    private Messager messager;
    private Element currentElement;
    private AtomicInteger index = new AtomicInteger(0);
    private AtomicBoolean lombokToString = new AtomicBoolean(false);
    private AtomicBoolean setter = new AtomicBoolean(false);
    private AtomicBoolean lombokExtensionMethod = new AtomicBoolean(false);

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
            currentElement = element;
            if (element.getKind() != ElementKind.FIELD) {
                error("Element needs to be field");
                continue;
            }
            if (!element.getModifiers().contains(Modifier.FINAL)) {
                error("Element needs to be final");
                continue;
            }

            YAPIONAccessGenerator yapionAccessGenerator = element.getAnnotation(YAPIONAccessGenerator.class);
            lombokToString.set(yapionAccessGenerator.lombokToString());
            setter.set(yapionAccessGenerator.setter());
            lombokExtensionMethod.set(yapionAccessGenerator.lombokExtensionMethods());
            index.set(0);

            TypeElement clazz = (TypeElement) element.getEnclosingElement();
            String packageName = clazz.getQualifiedName().toString();
            packageName = packageName.substring(0, packageName.lastIndexOf('.'));

            VariableElement variableElement = (VariableElement) element;
            YAPIONObject yapionObject;
            if (yapionAccessGenerator.inline()) {
                yapionObject = YAPIONParser.parse(variableElement.getConstantValue().toString());
            } else {
                File file = new File("./" + variableElement.getConstantValue().toString());
                try {
                    yapionObject = YAPIONParser.parse(file);
                } catch (IOException e) {
                    error("Parsing of file '" + file.getAbsolutePath() + "' failed. Please specify a valid path relative from '" + new File(".").getAbsolutePath() + "'");
                    continue;
                }
            }
            if (!yapionObject.containsKey("@name", String.class)) {
                error("Root Element needs to have a proper name. Use '@name' to specify one in the YAPIONObject");
                continue;
            }

            ObjectContainer objectContainer = new ObjectContainer(yapionObject.getPlainValue("@name"), yapionObject);

            ClassGenerator classGenerator = new ClassGenerator(packageName, objectContainer.getClassName());
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE, ModifierType.STATIC), "checkValue", "<T> void", new ParameterGenerator(YAPIONValue.class.getTypeName() + "<?>", "value"), new ParameterGenerator("boolean", "nonNull"), new ParameterGenerator("Class<T>", "type"), new ParameterGenerator(Supplier.class.getTypeName() + "<T>", "defaultValue"), new ParameterGenerator(Consumer.class.getTypeName() + "<T>", "callback"), new ParameterGenerator(Predicate.class.getTypeName() + "<T>", "checkPredicate"));
            functionGenerator.add("if (value == null) {");
            functionGenerator.add("    if (defaultValue != null) {");
            functionGenerator.add("        callback.accept(defaultValue.get());");
            functionGenerator.add("        return;");
            functionGenerator.add("    }");
            functionGenerator.add("    if (nonNull) {");
            functionGenerator.add("        throw new yapion.exceptions.YAPIONException(\"Value is not present\");");
            functionGenerator.add("    } else {");
            functionGenerator.add("        callback.accept(null);");
            functionGenerator.add("        return;");
            functionGenerator.add("    }");
            functionGenerator.add("}");
            functionGenerator.add("T t = (T) value.get();");
            functionGenerator.add("if (t == null) {");
            functionGenerator.add("    if (defaultValue != null) {");
            functionGenerator.add("        callback.accept(defaultValue.get());");
            functionGenerator.add("        return;");
            functionGenerator.add("    }");
            functionGenerator.add("    if (nonNull) {");
            functionGenerator.add("        throw new yapion.exceptions.YAPIONException(\"Value is null\");");
            functionGenerator.add("    } else {");
            functionGenerator.add("        callback.accept(null);");
            functionGenerator.add("        return;");
            functionGenerator.add("    }");
            functionGenerator.add("}");
            functionGenerator.add("if (!checkPredicate.test(t)) {");
            functionGenerator.add("    if (defaultValue != null) {");
            functionGenerator.add("        callback.accept(defaultValue.get());");
            functionGenerator.add("        return;");
            functionGenerator.add("    }");
            functionGenerator.add("    throw new yapion.exceptions.YAPIONException(\"Checks failed\");");
            functionGenerator.add("}");
            functionGenerator.add("callback.accept(t);");
            classGenerator.add(functionGenerator);

            functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE, ModifierType.STATIC), "checkObject", "<T> void", new ParameterGenerator(YAPIONObject.class.getTypeName(), "object"), new ParameterGenerator("boolean", "nonNull"), new ParameterGenerator(Function.class.getTypeName() + "<" + YAPIONObject.class.getTypeName() + ", T>", "converter"), new ParameterGenerator(Consumer.class.getTypeName() + "<T>", "callback"));
            functionGenerator.add("if (object == null) {");
            functionGenerator.add("    if (nonNull) {");
            functionGenerator.add("        throw new yapion.exceptions.YAPIONException(\"Object is not present\");");
            functionGenerator.add("    } else {");
            functionGenerator.add("        callback.accept(null);");
            functionGenerator.add("        return;");
            functionGenerator.add("    }");
            functionGenerator.add("}");
            functionGenerator.add("T t = converter.apply(object);");
            functionGenerator.add("if (t == null && nonNull) {");
            functionGenerator.add("    throw new yapion.exceptions.YAPIONException(\"Object is null\");");
            functionGenerator.add("}");
            functionGenerator.add("callback.accept(t);");
            classGenerator.add(functionGenerator);

            functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PRIVATE, ModifierType.STATIC), "checkArray", "<T, K extends " + YAPIONAnyType.class.getTypeName() + "> void", new ParameterGenerator(YAPIONArray.class.getTypeName(), "array"), new ParameterGenerator("boolean", "nonNull"), new ParameterGenerator("Class<K>", "yapionType"), new ParameterGenerator("Class<T>", "type"), new ParameterGenerator(Function.class.getTypeName() + "<K, T>", "converter"), new ParameterGenerator(Predicate.class.getTypeName() + "<T>", "checkPredicate"), new ParameterGenerator(Consumer.class.getTypeName() + "<" + List.class.getTypeName() + "<T>>", "callback"));
            functionGenerator.add("if (array == null) {");
            functionGenerator.add("    if (nonNull) {");
            functionGenerator.add("        throw new yapion.exceptions.YAPIONException(\"Array is not present\");");
            functionGenerator.add("    } else {");
            functionGenerator.add("        callback.accept(null);");
            functionGenerator.add("        return;");
            functionGenerator.add("    }");
            functionGenerator.add("}");
            functionGenerator.add("java.util.List<T> list = new java.util.ArrayList<>();");
            functionGenerator.add("array.forEach(yapionAnyType -> {");
            functionGenerator.add("    if (!yapionType.isInstance(yapionAnyType)) {");
            functionGenerator.add("        throw new yapion.exceptions.YAPIONException(\"Illegal Instance\");");
            functionGenerator.add("    }");
            functionGenerator.add("    T converted = converter.apply((K) yapionAnyType);");
            functionGenerator.add("    if (checkPredicate != null && !checkPredicate.test(converted)) {");
            functionGenerator.add("        throw new yapion.exceptions.YAPIONException(\"Checks failed\");");
            functionGenerator.add("    }");
            functionGenerator.add("    list.add(converted);");
            functionGenerator.add("});");
            functionGenerator.add("callback.accept(list);");
            classGenerator.add(functionGenerator);

            if (lombokExtensionMethod.get()) {
                classGenerator.addAnnotation("@lombok.experimental.ExtensionMethod(yapion.serializing.annotationproccessing.ConstraintUtils.class)");
            }
            if (lombokToString.get()) {
                classGenerator.addAnnotation(ToString.class);
            }
            classGenerator.addImport("static yapion.serializing.annotationproccessing.ConstraintUtils.*");
            objectContainer.outputRoot(classGenerator);

            JavaFileObject f = processingEnv.getFiler().createSourceFile(classGenerator.getPackageName() + "." + classGenerator.getClassName());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Creating " + f.toUri());
            PrintWriter pw = new PrintWriter(f.openWriter());
            classGenerator.output(0).forEach(pw::println);
            pw.close();
        }

        return true;
    }

    private void error(String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                currentElement);
    }

    @ToString
    @Getter
    private abstract class ContainerElement {
        private String name;
        private boolean nonNull = false;

        public ContainerElement(String name) {
            if (name != null && name.endsWith("!")) {
                nonNull = true;
                name = name.substring(0, name.length() - 1);
            }
            this.name = name;
        }

        public abstract String constructorCall();
        public abstract void output(ClassGenerator classGenerator);
    }

    @ToString(callSuper = true)
    @Getter
    private class ObjectContainer extends ContainerElement {

        private List<ContainerElement> containerElementList = new ArrayList<>();
        private String className;
        private boolean hidden;

        public ObjectContainer(String name, YAPIONObject yapionObject) {
            super(name);
            if (yapionObject.containsKey("@name", String.class)) {
                className = yapionObject.getPlainValue("@name");
            } else {
                className = "_" + index.incrementAndGet();
            }
            hidden = yapionObject.getPlainValueOrDefault("@hidden", false);
            checkElements(yapionObject, containerElementList::add);
        }

        @Override
        public String constructorCall() {
            if (hidden) {
                return "";
            }
            return "checkObject(yapionObject.getObject(\"" + getName() + "\"), " + isNonNull() + ", " + getClassName() + "::new, value -> this." + getName() + " = value);";
        }

        public void outputRoot(ClassGenerator classGenerator) {
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "yapionObject"));
            classGenerator.add(functionGenerator);
            ClassGenerator finalClassGenerator = classGenerator;
            containerElementList.forEach(containerElement -> {
                String constructorCall = containerElement.constructorCall();
                if (!constructorCall.isEmpty()) {
                    for (String s : constructorCall.split("\n")) {
                        functionGenerator.add(s);
                    }
                }
                containerElement.output(finalClassGenerator);
            });
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            if (!hidden) {
                methodsAndField(classGenerator, getName(), className, isNonNull());
            }

            ClassGenerator currentGenerator = new ClassGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName());
            if (lombokToString.get()) {
                currentGenerator.addAnnotation(ToString.class);
            }
            classGenerator.add(currentGenerator);
            classGenerator = currentGenerator;
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "yapionObject"));
            classGenerator.add(functionGenerator);
            ClassGenerator finalClassGenerator = classGenerator;
            containerElementList.forEach(containerElement -> {
                String constructorCall = containerElement.constructorCall();
                if (!constructorCall.isEmpty()) {
                    for (String s : constructorCall.split("\n")) {
                        functionGenerator.add(s);
                    }
                }
                containerElement.output(finalClassGenerator);
            });
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class ObjectReferenceContainer extends ContainerElement {

        private List<ContainerElement> containerElementList = new ArrayList<>();
        private String className;
        private boolean hidden;
        private String reference;

        public ObjectReferenceContainer(String name, YAPIONObject yapionObject) {
            super(name);
            if (yapionObject.containsKey("@name", String.class)) {
                className = yapionObject.getPlainValue("@name");
            } else {
                className = "_" + index.incrementAndGet();
            }
            hidden = yapionObject.getPlainValueOrDefault("@hidden", false);
            reference = yapionObject.getPlainValue("@reference");
            checkElements(yapionObject, containerElementList::add);
        }

        public ObjectReferenceContainer(String name, YAPIONValue<String> yapionValue) {
            super(name);
            reference = yapionValue.get().substring(1);
        }

        @Override
        public String constructorCall() {
            if (containerElementList.isEmpty()) {
                return "checkObject(yapionObject.getObject(\"" + getName() + "\"), " + isNonNull() + ", " + reference + "::new, value -> this." + getName() + " = value);";
            }
            if (hidden) {
                return "";
            }
            return "checkObject(yapionObject.getObject(\"" + getName() + "\"), " + isNonNull() + ", " + getClassName() + "::new, value -> this." + getName() + " = value);";
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            if (containerElementList.isEmpty()) {
                methodsAndField(classGenerator, getName(), reference, isNonNull());
                return;
            }
            if (!hidden) {
                methodsAndField(classGenerator, getName(), className, isNonNull());
            }

            ClassGenerator currentGenerator = new ClassGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName());
            if (lombokToString.get()) {
                currentGenerator.addAnnotation(ToString.class);
            }
            currentGenerator.setExtendsString(reference);
            classGenerator.add(currentGenerator);
            classGenerator = currentGenerator;
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "yapionObject"));
            functionGenerator.add("super(yapionObject);");
            classGenerator.add(functionGenerator);
            ClassGenerator finalClassGenerator = classGenerator;
            containerElementList.forEach(containerElement -> {
                String constructorCall = containerElement.constructorCall();
                if (!constructorCall.isEmpty()) {
                    for (String s : constructorCall.split("\n")) {
                        functionGenerator.add(s);
                    }
                }
                containerElement.output(finalClassGenerator);
            });
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class ArrayReferenceContainer extends ContainerElement {

        private List<ContainerElement> containerElementList = new ArrayList<>();
        private String className;
        private String reference;

        public ArrayReferenceContainer(String name, YAPIONObject yapionObject) {
            super(name);
            if (yapionObject.containsKey("@name", String.class)) {
                className = yapionObject.getPlainValue("@name");
            } else {
                className = "_" + index.incrementAndGet();
            }
            reference = yapionObject.getPlainValue("@arrayReference");
            checkElements(yapionObject, containerElementList::add);
        }

        public ArrayReferenceContainer(String name, YAPIONValue<String> yapionValue) {
            super(name);
            reference = yapionValue.get().substring(1, yapionValue.get().length() - 2);
        }

        @Override
        public String constructorCall() {
            if (containerElementList.isEmpty()) {
                return "checkArray(yapionObject.getArray(\"" + getName() + "\"), " + isNonNull() + ", " + YAPIONObject.class.getTypeName() + ".class, " + reference + ".class, " + reference + "::new, null, value -> this." + getName() + " = value);";
            }
            return "checkArray(yapionObject.getArray(\"" + getName() + "\"), " + isNonNull() + ", " + YAPIONObject.class.getTypeName() + ".class, " + className + ".class, " + className + "::new, null, value -> this." + getName() + " = value);";
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            if (containerElementList.isEmpty()) {
                methodsAndField(classGenerator, getName(), "java.util.List<" + reference + ">", isNonNull());
                return;
            }
            methodsAndField(classGenerator, getName(), "java.util.List<" + className + ">", isNonNull());

            ClassGenerator currentGenerator = new ClassGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName());
            if (lombokToString.get()) {
                currentGenerator.addAnnotation(ToString.class);
            }
            currentGenerator.setExtendsString(reference);
            classGenerator.add(currentGenerator);
            classGenerator = currentGenerator;
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), null, getClassName(), new ParameterGenerator(YAPIONObject.class.getTypeName(), "yapionObject"));
            functionGenerator.add("super(yapionObject);");
            classGenerator.add(functionGenerator);
            ClassGenerator finalClassGenerator = classGenerator;
            containerElementList.forEach(containerElement -> {
                String constructorCall = containerElement.constructorCall();
                if (!constructorCall.isEmpty()) {
                    for (String s : constructorCall.split("\n")) {
                        functionGenerator.add(s);
                    }
                }
                containerElement.output(finalClassGenerator);
            });
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class ArrayContainer extends ContainerElement {

        private String type;
        private String constraints;

        public ArrayContainer(String name, YAPIONObject yapionObject) {
            super(name);
            type = yapionObject.getPlainValueOrDefault("@arrayType", "OBJECT");
            constraints = yapionObject.getPlainValueOrDefault("constraints", "ignored -> true");
            constraints = Arrays.stream(constraints.split("\n")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.joining("\n"));
        }

        public ArrayContainer(String name, YAPIONValue<String> yapionValue) {
            super(name);
            type = yapionValue.get().substring(0, yapionValue.get().length() - 2);
            constraints = "ignored -> true";
        }

        @Override
        public String constructorCall() {
            return "checkArray(yapionObject.getArray(\"" + getName() + "\"), " + isNonNull() + ", " + YAPIONValue.class.getTypeName() + ".class, " + toClass(type).getTypeName() + ".class, value -> (" + toClass(type).getTypeName() + ") value.get(), " + constraints + ", value -> this." + getName() + " = value);";
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            methodsAndField(classGenerator, getName(), "java.util.List<" + toClass(type).getTypeName() + ">", isNonNull());
        }
    }

    @ToString(callSuper = true)
    @Getter
    private class ValueContainer extends ContainerElement {

        private String type;
        private String constraints;
        private Optional<String> defaultValue = Optional.empty();

        public ValueContainer(String name, YAPIONObject yapionObject) {
            super(name);
            type = yapionObject.getPlainValueOrDefault("@type", "OBJECT");
            constraints = yapionObject.getPlainValueOrDefault("constraints", "ignored -> true");
            constraints = Arrays.stream(constraints.split("\n")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.joining("\n"));

            if (yapionObject.containsKey("default", YAPIONType.VALUE)) {
                Object object = yapionObject.getValue("default").get();
                if (object.getClass() != toClass(type)) {
                    error("The type of the default value did not match the given variable type");
                    return;
                }
                if (object instanceof String s) {
                    defaultValue = Optional.of("\"" + s + "\"");
                } else if (object instanceof Byte b) {
                    defaultValue = Optional.of("(byte) " + b);
                } else if (object instanceof Short s) {
                    defaultValue = Optional.of("(short) " + s);
                } else if (object instanceof Integer i) {
                    defaultValue = Optional.of("" + i);
                } else if (object instanceof Long l) {
                    defaultValue = Optional.of("" + l + "L");
                } else if (object instanceof BigInteger bi) {
                    defaultValue = Optional.of("new " + BigInteger.class.getTypeName() + "(" + bi.toString() + ")");
                } else if (object instanceof Float f) {
                    defaultValue = Optional.of("" + f + "F");
                } else if (object instanceof Double d) {
                    defaultValue = Optional.of("" + d + "D");
                } else if (object instanceof BigDecimal bd) {
                    defaultValue = Optional.of("new " + BigDecimal.class.getTypeName() + "(" + bd.toString() + ")");
                } else if (object instanceof Character c) {
                    defaultValue = Optional.of("'" + c + "'");
                } else if (object instanceof Boolean b) {
                    defaultValue = Optional.of("" + b);
                } else {
                    defaultValue = Optional.of("null");
                }
            }
        }

        public ValueContainer(String name, YAPIONValue<String> yapionValue) {
            super(name);
            type = yapionValue.get();
            constraints = "ignored -> true";
        }

        @Override
        public String constructorCall() {
            return "checkValue(yapionObject.getValue(\"" + getName() + "\"), " + isNonNull() + ", " + toClass(type).getTypeName() + ".class, " + (defaultValue.map(s -> "() -> " + s).orElse("null")) + ", value -> this." + getName() + " = value, " + constraints + ");";
        }

        @Override
        public void output(ClassGenerator classGenerator) {
            methodsAndField(classGenerator, getName(), toClass(type).getTypeName(), isNonNull());
        }
    }

    public void checkElements(YAPIONObject yapionObject, Consumer<ContainerElement> resultConsumer) {
        yapionObject.forEach((s, yapionAnyType) -> {
            if (s.startsWith("@")) {
                return;
            }
            if (yapionAnyType instanceof YAPIONObject object) {
                if (object.containsKey("@type", String.class)) {
                    resultConsumer.accept(new ValueContainer(s, object));
                } else if (object.containsKey("@arrayType", String.class)) {
                    resultConsumer.accept(new ArrayContainer(s, object));
                } else if (object.containsKey("@reference", String.class)) {
                    resultConsumer.accept(new ObjectReferenceContainer(s, object));
                } else if (object.containsKey("@arrayReference", String.class)) {
                    resultConsumer.accept(new ArrayReferenceContainer(s, object));
                } else {
                    resultConsumer.accept(new ObjectContainer(s, object));
                }
            } else if (yapionAnyType instanceof YAPIONValue value) {
                String v = value.get().toString();
                if (v.endsWith("[]")) {
                    if (v.startsWith("@")) {
                        resultConsumer.accept(new ArrayReferenceContainer(s, value));
                    } else {
                        resultConsumer.accept(new ArrayContainer(s, value));
                    }
                } else {
                    if (v.startsWith("@")) {
                        resultConsumer.accept(new ObjectReferenceContainer(s, value));
                    } else {
                        resultConsumer.accept(new ValueContainer(s, value));
                    }
                }
            } else {
                error("Only YAPIONObjects are allowed to specify types: {}", yapionAnyType);
            }
        });
    }

    public static String toCamelCase(String in) {
        if (in.isEmpty()) return in;
        return "" + Character.toTitleCase(in.charAt(0)) + in.substring(1);
    }

    public static Class<?> toClass(String type) {
        return switch (type.toLowerCase()) {
            case "string" -> String.class;
            case "byte" -> Byte.class;
            case "short" -> Short.class;
            case "int", "integer" -> Integer.class;
            case "long" -> Long.class;
            case "biginteger" -> BigInteger.class;
            case "float" -> Float.class;
            case "double" -> Double.class;
            case "bigdecimal" -> BigDecimal.class;
            case "char", "character" -> Character.class;
            case "bool", "boolean" -> Boolean.class;
            default -> Object.class;
        };
    }

    public void methodsAndField(ClassGenerator classGenerator, String name, String type, boolean nonNull) {
        classGenerator.add(new FieldGenerator(new ModifierGenerator(ModifierType.PRIVATE), type, name, null));

        if (!nonNull) {
            FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "is" + toCamelCase(name) + "Present", "boolean");
            functionGenerator.add("return " + name + " != null;");
            classGenerator.add(functionGenerator);
        }

        FunctionGenerator functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "get" + toCamelCase(name), type);
        functionGenerator.add("return " + name + ";");
        classGenerator.add(functionGenerator);

        if (setter.get()) {
            functionGenerator = new FunctionGenerator(new ModifierGenerator(ModifierType.PUBLIC), "set" + toCamelCase(name), void.class, new ParameterGenerator(type, name));
            functionGenerator.add("this." + name + " = " + name + ";");
            classGenerator.add(functionGenerator);
        }
    }
}
