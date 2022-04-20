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

package yapion.utils;

import lombok.experimental.UtilityClass;
import yapion.serializing.InternalSerializer;
import yapion.serializing.api.YAPIONSerializerRegistrator;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@UtilityClass
public class Packer {

    // Entry End    : 0x00
    // File         : 0x10 String
    // Type         : 0x11 String
    // PrimitiveType: 0x12 String
    // InterfaceType: 0x13 String
    // ClassType    : 0x14 String
    // Start        : 0x20 int
    // Length       : 0x21 int
    // DirectLoad   : 0x30

    public void pack(File source, File destination, Predicate<File> filter, Predicate<String> directLoad) throws Exception {
        if (!source.exists()) return;
        if (!source.isDirectory()) return;
        if (!destination.getName().endsWith(".pack")) return;

        File metaDestination = new File(destination.getAbsolutePath() + ".meta");

        destination.getParentFile().mkdirs();
        destination.delete();
        destination.createNewFile();
        System.out.println(destination.getAbsolutePath());

        AtomicInteger index = new AtomicInteger();

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(metaDestination));

        int substringLength = source.getAbsolutePath().length() + 1;
        internalPack(bufferedOutputStream, objectOutputStream, substringLength, source, source, filter, index, directLoad);
        bufferedOutputStream.close();
        objectOutputStream.close();
    }

    private void internalPack(OutputStream outputStream, ObjectOutputStream objectOutputStream, int substringLength, File source, File file, Predicate<File> filter, AtomicInteger index, Predicate<String> directLoad) throws IOException {
        if (file == null) return;
        File[] files = file.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (!f.isFile()) continue;
            if (!filter.test(f)) continue;

            String fileName = f.getAbsolutePath().replace('\\', '/');
            fileName = fileName.substring(substringLength);
            System.out.println("Packing: " + fileName);

            try {
                String name = f.getAbsolutePath().substring(source.getAbsolutePath().length() + 1).replace('/', '.').replace('\\', '.').replace(".class", "");
                String current = "yapion.serializing.serializer." + f.getAbsolutePath().substring(source.getAbsolutePath().length() + 1).replace('/', '.').replace('\\', '.').replace(".class", "");
                objectOutputStream.writeByte(0x10);
                objectOutputStream.writeUTF(name);
                objectOutputStream.writeByte(0x20);
                objectOutputStream.writeInt(index.get());
                objectOutputStream.writeByte(0x21);
                objectOutputStream.writeInt((int) f.length());

                Class<?> clazz = Class.forName(current);
                if (!clazz.isInterface() && !clazz.isMemberClass()) {
                    Object object = clazz.getConstructor().newInstance();
                    if (object instanceof InternalSerializer internalSerializer) {
                        if (object.getClass().getSuperclass() != Object.class) {
                            try {
                                internalSerializer.init();
                            } catch (Exception e) {
                                // ignore
                            }
                        }
                        if (internalSerializer.type() != null) {
                            objectOutputStream.writeByte(0x11);
                            objectOutputStream.writeUTF(internalSerializer.type().getTypeName());
                        }
                        if (internalSerializer.primitiveType() != null) {
                            objectOutputStream.writeByte(0x12);
                            objectOutputStream.writeUTF(internalSerializer.primitiveType().getTypeName());
                        }
                        if (internalSerializer.interfaceType() != null) {
                            objectOutputStream.writeByte(0x13);
                            objectOutputStream.writeUTF(internalSerializer.interfaceType().getTypeName());
                        }
                        if (internalSerializer.classType() != null) {
                            objectOutputStream.writeByte(0x14);
                            objectOutputStream.writeUTF(internalSerializer.classType().getTypeName());
                        }
                    } else if (object instanceof YAPIONSerializerRegistrator) {
                        objectOutputStream.writeByte(0x30);
                    }
                }
                if (directLoad.test(current)) {
                    objectOutputStream.writeByte(0x30);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                try {
                    objectOutputStream.writeByte(0x00);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(f))) {
                for (int i = 0; i < f.length(); i++) {
                    outputStream.write(bufferedInputStream.read());
                }
                index.getAndAdd((int) f.length());
            }
            f.delete();
        }

        for (File f : files) {
            if (!f.isDirectory()) continue;
            internalPack(outputStream, objectOutputStream, substringLength, source, f, filter, index, directLoad);
            f.delete();
        }
    }
}
