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
import org.objectweb.asm.*;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.annotationproccessing.serializingdata.ClassData;
import yapion.serializing.annotationproccessing.serializingdata.FieldData;
import yapion.serializing.api.SerializerObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Modifier;
import java.util.Set;

import static org.objectweb.asm.Opcodes.*;

public class SerializingApplier {

    private static File source;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new SecurityException("program args need to contain the class folder (example: './build/classes/java/main' or './target/classes')");
        }
        File file = new File("./YAPIONAnnotationProcessor.yapion");
        if (!file.exists()) {
            return;
        }
        source = new File(args[0]);
        if (!source.exists()) {
            throw new SecurityException("program args need to contain the class folder (example: './build/classes/java/main' or './target/classes')");
        }

        Set<ClassData> classDatas = YAPIONDeserializer.deserialize(YAPIONParser.parse(file));
        classDatas.forEach(SerializingApplier::process);
        file.delete();
    }

    @SneakyThrows
    private static void process(ClassData classData) {
        File file = new File(source, classData.getQualifiedName().replace('.', '/') + ".class");
        if (!file.exists()) return;

        String innerClassIdentifier = classData.getQualifiedName().replace('.', '/') + "$" + classData.getSimpleName() + "Serializer";
        String outerClassIdentifier = classData.getQualifiedName().replace('.', '/');
        String innerClassName = classData.getSimpleName() + "Serializer";

        ClassReader classReader = new ClassReader(new FileInputStream(file));
        ClassWriter classWriter = new ClassWriter(classReader, 0);
        classReader.accept(new ClassVisitor(Opcodes.ASM9, classWriter) {
            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                classData.setFieldType(name, descriptor);
                if (Modifier.isPrivate(access) && false) {
                    return super.visitField(access ^ Modifier.PRIVATE, name, descriptor, signature, value);
                } else {
                    return super.visitField(access, name, descriptor, signature, value);
                }
            }
        }, 0);
        classWriter.visitNestMember(innerClassIdentifier);
        classWriter.visitInnerClass(innerClassIdentifier, outerClassIdentifier, innerClassName, ACC_PROTECTED | ACC_STATIC);
        output(classWriter, file);

        classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, innerClassIdentifier, "L" + SerializerObject.class.getTypeName().replace('.', '/') + "<L" + outerClassIdentifier + ";>;", SerializerObject.class.getTypeName().replace('.', '/'), null);
        classWriter.visitSource(outerClassIdentifier, null);
        classWriter.visitNestHost(outerClassIdentifier);
        classWriter.visitInnerClass(innerClassIdentifier, outerClassIdentifier, innerClassName, ACC_PROTECTED | ACC_STATIC);
        objectInitMethod(classWriter, innerClassIdentifier);
        create(classWriter, classData);
        output(classWriter, new File(source, innerClassIdentifier + ".class"));
    }

    private static void objectInitMethod(ClassWriter classWriter, String innerClassIdentifier) {
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PROTECTED, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        Label methodStart = new Label();
        methodVisitor.visitLabel(methodStart);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "yapion/serializing/api/SerializerObject", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        Label methodEnd = new Label();
        methodVisitor.visitLabel(methodEnd);
        methodVisitor.visitLocalVariable("this", "L" + innerClassIdentifier + ";", null, methodStart, methodEnd, 0);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

    @SneakyThrows
    private static void output(ClassWriter classWriter, File file) {
        System.out.println(file);
        file.delete();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(classWriter.toByteArray());
        fileOutputStream.close();
    }

    private static void create(ClassWriter classWriter, ClassData classData) {
        createFields(classWriter, classData);
        createInit(classWriter, classData);
        createType(classWriter, classData);
        createSerialize(classWriter, classData);
        createDeserialize(classWriter, classData);
        createBridges(classWriter, classData);
    }

    private static void createFields(ClassWriter classWriter, ClassData classData) {
        for (FieldData fieldData : classData.getFieldDataList()) {
            if (Modifier.isFinal(fieldData.getModifiers())) {
                classWriter.visitField(ACC_PRIVATE | ACC_FINAL, fieldData.getFieldName(), "Ljava/lang/reflect/Field;", null, null).visitEnd();
            }
        }
    }

    private static void createInit(ClassWriter classWriter, ClassData classData) {
        if (!classData.isInitNeeded()) return;
        String owner = classData.getQualifiedName().replace('.', '/') + "$" + classData.getSimpleName() + "Serializer";

        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "init", "()V", null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        Label label1 = new Label();
        Label label2 = new Label();
        methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
        methodVisitor.visitLdcInsn(Type.getType("L" + classData.getSimpleName().replace('.', '/') + ";"));
        methodVisitor.visitVarInsn(ASTORE, 1);
        methodVisitor.visitLabel(label0);

        Label label3 = new Label();
        methodVisitor.visitLabel(label3);
        for (FieldData fieldData : classData.getFieldDataList()) {
            if (Modifier.isFinal(fieldData.getModifiers()) && (fieldData.getLoadExclude() == null || fieldData.getLoadExclude().length != 0)) {
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitLdcInsn(fieldData.getFieldName());
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;", false);
                methodVisitor.visitFieldInsn(PUTFIELD, owner, fieldData.getFieldName(), "Ljava/lang/reflect/Field;");
            }
        }

        methodVisitor.visitLabel(label1);
        Label label5 = new Label();
        methodVisitor.visitJumpInsn(GOTO, label5);
        methodVisitor.visitLabel(label2);
        methodVisitor.visitFrame(Opcodes.F_NEW, 2, new Object[]{owner, "java/lang/Class"}, 1, new Object[]{"java/lang/Exception"});
        methodVisitor.visitVarInsn(ASTORE, 2);
        Label label6 = new Label();
        methodVisitor.visitLabel(label6);
        methodVisitor.visitTypeInsn(NEW, "java/lang/SecurityException");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "getMessage", "()Ljava/lang/String;", false);
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/SecurityException", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);
        methodVisitor.visitInsn(ATHROW);
        methodVisitor.visitLabel(label5);
        methodVisitor.visitFrame(Opcodes.F_NEW, 2, new Object[]{owner, "java/lang/Class"}, 0, new Object[]{});
        methodVisitor.visitInsn(RETURN);
        Label label7 = new Label();
        methodVisitor.visitLabel(label7);
        methodVisitor.visitLocalVariable("e", "Ljava/lang/Exception;", null, label6, label5, 2);
        methodVisitor.visitLocalVariable("this", "L" + owner + ";", null, label3, label7, 0);
        methodVisitor.visitLocalVariable("clazz", "Ljava/lang/Class;", "Ljava/lang/Class<*>;", label0, label7, 1);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

    private static void createType(ClassWriter classWriter, ClassData classData) {
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "type", "()Ljava/lang/Class;", "()Ljava/lang/Class<L" + classData.getQualifiedName().replace('.', '/') + ";>;", null);
        methodVisitor.visitCode();
        Label methodStart = new Label();
        methodVisitor.visitLabel(methodStart);
        methodVisitor.visitLdcInsn(Type.getType("L" + classData.getQualifiedName().replace('.', '/') + ";"));
        methodVisitor.visitInsn(ARETURN);
        Label methodEnd = new Label();
        methodVisitor.visitLabel(methodEnd);
        methodVisitor.visitLocalVariable("this", "L" + classData.getQualifiedName().replace('.', '/') + "$" + classData.getSimpleName() + "Serializer;", null, methodStart, methodEnd, 0);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

    private static void createSerialize(ClassWriter classWriter, ClassData classData) {
        String owner = classData.getQualifiedName().replace('.', '/') + "$" + classData.getSimpleName() + "Serializer";
        String outerClass = classData.getQualifiedName().replace('.', '/');

        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "serialize", "(Lyapion/serializing/data/SerializeData;)Lyapion/hierarchy/types/YAPIONObject;", "(Lyapion/serializing/data/SerializeData<Lde/yoyosource/Test;>;)Lyapion/hierarchy/types/YAPIONObject;", null);
        methodVisitor.visitCode();
        Label methodStart = new Label();
        methodVisitor.visitLabel(methodStart);

        methodVisitor.visitTypeInsn(NEW, "yapion/hierarchy/types/YAPIONObject");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, owner, "type", "()Ljava/lang/Class;", false);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "yapion/hierarchy/types/YAPIONObject", "<init>", "(Ljava/lang/Class;)V", false);
        methodVisitor.visitVarInsn(ASTORE, 2);

        if (classData.isSerializerContextManager() || classData.isSerializerMethods()) {
            methodVisitor.visitTypeInsn(NEW, "yapion/serializing/ContextManager");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitFieldInsn(GETFIELD, "yapion/serializing/data/SerializeData", "context", "Ljava/lang/String;");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "yapion/serializing/ContextManager", "<init>", "(Ljava/lang/String;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
        }

        if (classData.isSerializerMethods()) {
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitFieldInsn(GETFIELD, "yapion/serializing/data/SerializeData", "object", "Ljava/lang/Object;");
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, owner, "type", "()Ljava/lang/Class;", false);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(NEW, "yapion/serializing/data/SerializationContext");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/data/SerializeData", "getSerializer", "()Lyapion/serializing/YAPIONSerializer;", false);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "yapion/serializing/data/SerializationContext", "<init>", "(Lyapion/serializing/YAPIONSerializer;Lyapion/hierarchy/types/YAPIONObject;)V", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "yapion/serializing/MethodManager", "preSerializationStep", "(Ljava/lang/Object;Ljava/lang/Class;Lyapion/serializing/ContextManager;Lyapion/serializing/data/SerializationContext;)V", false);
        }

        for (FieldData fieldData : classData.getFieldDataList()) {
            if (fieldData.getSaveExclude() != null && fieldData.getSaveExclude().length == 0) {
                continue;
            }
            Label jump = new Label();
            if (fieldData.getOptimize() != null) {
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitFieldInsn(GETFIELD, "yapion/serializing/data/SerializeData", "object", "Ljava/lang/Object;");
                methodVisitor.visitTypeInsn(CHECKCAST, outerClass);
                methodVisitor.visitFieldInsn(GETFIELD, outerClass, fieldData.getFieldName(), fieldData.getFieldType());
                methodVisitor.visitJumpInsn(IFNULL, jump);
                if (fieldData.getOptimize().length != 0) {
                    methodVisitor.visitVarInsn(ALOAD, 3);
                    methodVisitor.visitLdcInsn(fieldData.getOptimize().length);
                    methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/String");
                    for (int i = 0; i < fieldData.getOptimize().length; i++) {
                        methodVisitor.visitInsn(DUP);
                        methodVisitor.visitLdcInsn(i);
                        methodVisitor.visitLdcInsn(fieldData.getOptimize()[i]);
                        methodVisitor.visitInsn(AASTORE);
                    }
                    methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/ContextManager", "is", "([Ljava/lang/String;)Z", false);
                    methodVisitor.visitJumpInsn(IFEQ, jump);
                }
            }
            if (fieldData.getSaveExclude() != null) {
                methodVisitor.visitVarInsn(ALOAD, 3);
                methodVisitor.visitLdcInsn(fieldData.getSaveExclude().length);
                methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/String");
                for (int i = 0; i < fieldData.getSaveExclude().length; i++) {
                    methodVisitor.visitInsn(DUP);
                    methodVisitor.visitLdcInsn(i);
                    methodVisitor.visitLdcInsn(fieldData.getSaveExclude()[i]);
                    methodVisitor.visitInsn(AASTORE);
                }
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/ContextManager", "is", "([Ljava/lang/String;)Z", false);
                methodVisitor.visitJumpInsn(IFNE, jump);
            }

            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitLdcInsn(fieldData.getFieldName());
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitFieldInsn(GETFIELD, "yapion/serializing/data/SerializeData", "object", "Ljava/lang/Object;");
            methodVisitor.visitTypeInsn(CHECKCAST, outerClass);
            methodVisitor.visitFieldInsn(GETFIELD, outerClass, fieldData.getFieldName(), fieldData.getFieldType());
            if ("ZCBSIFJD".contains(fieldData.getFieldType())) {
                switch (fieldData.getFieldType()) {
                    case "Z" -> {
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                    }
                    case "C" -> {
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
                    }
                    case "B" -> {
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                    }
                    case "S" -> {
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                    }
                    case "I" -> {
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                    }
                    case "F" -> {
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                    }
                    case "J" -> {
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                    }
                    case "D" -> {
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                    }
                    default -> throw new SecurityException();
                }
            }
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/data/SerializeData", "serialize", "(Ljava/lang/Object;)Lyapion/hierarchy/api/groups/YAPIONAnyType;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/hierarchy/types/YAPIONObject", "add", "(Ljava/lang/Object;Lyapion/hierarchy/api/groups/YAPIONAnyType;)Ljava/lang/Object;", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitLabel(jump);
        }

        if (classData.isSerializerMethods()) {
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitFieldInsn(GETFIELD, "yapion/serializing/data/SerializeData", "object", "Ljava/lang/Object;");
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, owner, "type", "()Ljava/lang/Class;", false);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(NEW, "yapion/serializing/data/SerializationContext");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/data/SerializeData", "getSerializer", "()Lyapion/serializing/YAPIONSerializer;", false);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "yapion/serializing/data/SerializationContext", "<init>", "(Lyapion/serializing/YAPIONSerializer;Lyapion/hierarchy/types/YAPIONObject;)V", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "yapion/serializing/MethodManager", "postSerializationStep", "(Ljava/lang/Object;Ljava/lang/Class;Lyapion/serializing/ContextManager;Lyapion/serializing/data/SerializationContext;)V", false);
        }

        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitInsn(ARETURN);

        Label methodEnd = new Label();
        methodVisitor.visitLabel(methodEnd);
        methodVisitor.visitLocalVariable("this", "L" + owner + ";", null, methodStart, methodEnd, 0);
        methodVisitor.visitLocalVariable("serializeData", "Lyapion/serializing/data/SerializeData;", "Lyapion/serializing/data/SerializeData<L" + outerClass + ";>;", methodStart, methodEnd, 1);
        methodVisitor.visitLocalVariable("yapionObject", "Lyapion/hierarchy/types/YAPIONObject;", null, methodStart, methodEnd, 2);
        methodVisitor.visitLocalVariable("contextManager", "Lyapion/serializing/ContextManager;", null, methodStart, methodEnd, 3);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

    private static void createDeserialize(ClassWriter classWriter, ClassData classData) {
        String owner = classData.getQualifiedName().replace('.', '/') + "$" + classData.getSimpleName() + "Serializer";
        String outerClass = classData.getQualifiedName().replace('.', '/');

        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "deserialize", "(Lyapion/serializing/data/DeserializeData;)L" + outerClass + ";", "(Lyapion/serializing/data/DeserializeData<Lyapion/hierarchy/types/YAPIONObject;>;)L" + outerClass + ";", null);
        methodVisitor.visitCode();

        Label methodStart = new Label();
        methodVisitor.visitLabel(methodStart);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, owner, "type", "()Ljava/lang/Class;", false);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "yapion/utils/ReflectionsUtils", "constructObjectObjenesis", "(Ljava/lang/Class;)Ljava/lang/Object;", false);
        methodVisitor.visitTypeInsn(CHECKCAST, outerClass);
        methodVisitor.visitVarInsn(ASTORE, 2);

        if (classData.isDeserializerContextManager() || classData.isDeserializerMethods()) {
            methodVisitor.visitTypeInsn(NEW, "yapion/serializing/ContextManager");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitFieldInsn(GETFIELD, "yapion/serializing/data/DeserializeData", "context", "Ljava/lang/String;");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "yapion/serializing/ContextManager", "<init>", "(Ljava/lang/String;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
        }

        if (classData.isDeserializerMethods()) {
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, owner, "type", "()Ljava/lang/Class;", false);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(NEW, "yapion/serializing/data/DeserializationContext");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/data/DeserializeData", "getDeserializer", "()Lyapion/serializing/YAPIONDeserializer;", false);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitFieldInsn(GETFIELD, "yapion/serializing/data/DeserializeData", "object", "Lyapion/hierarchy/api/groups/YAPIONAnyType;");
            methodVisitor.visitTypeInsn(CHECKCAST, "yapion/hierarchy/types/YAPIONObject");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "yapion/serializing/data/DeserializationContext", "<init>", "(Lyapion/serializing/YAPIONDeserializer;Lyapion/hierarchy/types/YAPIONObject;)V", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "yapion/serializing/MethodManager", "preDeserializationStep", "(Ljava/lang/Object;Ljava/lang/Class;Lyapion/serializing/ContextManager;Lyapion/serializing/data/DeserializationContext;)V", false);
        }

        for (FieldData fieldData : classData.getFieldDataList()) {
            if (fieldData.getLoadExclude() != null && fieldData.getLoadExclude().length == 0) {
                continue;
            }
            Label jump = new Label();
            if (fieldData.getLoadExclude() != null) {
                methodVisitor.visitVarInsn(ALOAD, 3);
                methodVisitor.visitLdcInsn(fieldData.getLoadExclude().length);
                methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/String");
                for (int i = 0; i < fieldData.getLoadExclude().length; i++) {
                    methodVisitor.visitInsn(DUP);
                    methodVisitor.visitLdcInsn(i);
                    methodVisitor.visitLdcInsn(fieldData.getLoadExclude()[i]);
                    methodVisitor.visitInsn(AASTORE);
                }
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/ContextManager", "is", "([Ljava/lang/String;)Z", false);
                methodVisitor.visitJumpInsn(IFNE, jump);
            }

            if (Modifier.isFinal(fieldData.getModifiers())) {
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitVarInsn(ALOAD, 2);
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitFieldInsn(GETFIELD, owner, fieldData.getFieldName(), "Ljava/lang/reflect/Field;");
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/data/DeserializeData", "deserialize", "(Ljava/lang/Object;Ljava/lang/reflect/Field;)Z", false);
                methodVisitor.visitInsn(POP);
            } else {
                methodVisitor.visitVarInsn(ALOAD, 2);
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitLdcInsn(fieldData.getFieldName());
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/data/DeserializeData", "deserialize", "(Ljava/lang/String;)Ljava/lang/Object;", false);
                if ("ZCBSIFJD".contains(fieldData.getFieldType())) {
                    switch (fieldData.getFieldType()) {
                        case "Z" -> {
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
                        }
                        case "C" -> {
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Character");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
                        }
                        case "B" -> {
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
                        }
                        case "S" -> {
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Short");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
                        }
                        case "I" -> {
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                        }
                        case "F" -> {
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Float");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                        }
                        case "J" -> {
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Long");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
                        }
                        case "D" -> {
                            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Double");
                            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                        }
                        default -> throw new SecurityException();
                    }
                } else {
                    methodVisitor.visitTypeInsn(CHECKCAST, Type.getType(fieldData.getFieldType()).getInternalName());
                }
                methodVisitor.visitFieldInsn(PUTFIELD, outerClass, fieldData.getFieldName(), fieldData.getFieldType());
            }
            methodVisitor.visitLabel(jump);
        }

        if (classData.isDeserializerMethods()) {
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, owner, "type", "()Ljava/lang/Class;", false);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(NEW, "yapion/serializing/data/DeserializationContext");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "yapion/serializing/data/DeserializeData", "getDeserializer", "()Lyapion/serializing/YAPIONDeserializer;", false);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitFieldInsn(GETFIELD, "yapion/serializing/data/DeserializeData", "object", "Lyapion/hierarchy/api/groups/YAPIONAnyType;");
            methodVisitor.visitTypeInsn(CHECKCAST, "yapion/hierarchy/types/YAPIONObject");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "yapion/serializing/data/DeserializationContext", "<init>", "(Lyapion/serializing/YAPIONDeserializer;Lyapion/hierarchy/types/YAPIONObject;)V", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "yapion/serializing/MethodManager", "postDeserializationStep", "(Ljava/lang/Object;Ljava/lang/Class;Lyapion/serializing/ContextManager;Lyapion/serializing/data/DeserializationContext;)V", false);
        }

        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitInsn(ARETURN);

        Label methodEnd = new Label();
        methodVisitor.visitLabel(methodEnd);
        methodVisitor.visitLocalVariable("this", "L" + owner + ";", null, methodStart, methodEnd, 0);
        methodVisitor.visitLocalVariable("deserializeData", "Lyapion/serializing/data/DeserializeData;", "Lyapion/serializing/data/DeserializeData<Lyapion/hierarchy/types/YAPIONObject;>;", methodStart, methodEnd, 1);
        methodVisitor.visitLocalVariable("object", "L" + outerClass + ";", null, methodStart, methodEnd, 2);
        methodVisitor.visitLocalVariable("contextManager", "Lyapion/serializing/ContextManager;", null, methodStart, methodEnd, 3);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

    private static void createBridges(ClassWriter classWriter, ClassData classData) {
        String owner = classData.getQualifiedName().replace('.', '/') + "$" + classData.getSimpleName() + "Serializer";
        String outerClass = classData.getQualifiedName().replace('.', '/');

        {
            MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_BRIDGE | ACC_SYNTHETIC, "serialize", "(Lyapion/serializing/data/SerializeData;)Lyapion/hierarchy/api/groups/YAPIONAnyType;", null, null);
            methodVisitor.visitCode();
            Label methodStart = new Label();
            methodVisitor.visitLabel(methodStart);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, owner, "serialize", "(Lyapion/serializing/data/SerializeData;)Lyapion/hierarchy/types/YAPIONObject;", false);
            methodVisitor.visitInsn(ARETURN);
            Label methodEnd = new Label();
            methodVisitor.visitLabel(methodEnd);
            methodVisitor.visitLocalVariable("this", "L" + owner + ";", null, methodStart, methodEnd, 0);
            methodVisitor.visitMaxs(0, 0);
            methodVisitor.visitEnd();
        }
        {
            MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_BRIDGE | ACC_SYNTHETIC, "deserialize", "(Lyapion/serializing/data/DeserializeData;)Ljava/lang/Object;", null, null);
            methodVisitor.visitCode();
            Label methodStart = new Label();
            methodVisitor.visitLabel(methodStart);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, owner, "deserialize", "(Lyapion/serializing/data/DeserializeData;)L" + outerClass + ";", false);
            methodVisitor.visitInsn(ARETURN);
            Label methodEnd = new Label();
            methodVisitor.visitLabel(methodEnd);
            methodVisitor.visitLocalVariable("this", "L" + owner + ";", null, methodStart, methodEnd, 0);
            methodVisitor.visitMaxs(0, 0);
            methodVisitor.visitEnd();
        }
    }
}
