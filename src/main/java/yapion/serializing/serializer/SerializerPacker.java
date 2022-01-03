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

package yapion.serializing.serializer;

import yapion.serializing.InternalSerializer;
import yapion.serializing.api.YAPIONSerializerRegistrator;
import yapion.utils.Packer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * This class will be deleted by compile process
 */
public class SerializerPacker {

    // Entry End    : 0x00
    // File         : 0x10 String
    // Type         : 0x11 String
    // PrimitiveType: 0x12 String
    // InterfaceType: 0x13 String
    // ClassType    : 0x14 String
    // Start        : 0x20 int
    // Length       : 0x21 int
    // DirectLoad   : 0x30

    public static void main(String[] args) throws Exception {
        String s = SerializerPacker.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        s += SerializerPacker.class.getPackage().getName().replace('.', '/');

        File source = new File(s);
        if (!source.exists()) return;

        File destination = new File(s.substring(0, s.lastIndexOf('/')), "serializer.pack");
        File metaDestination = new File(s.substring(0, s.lastIndexOf('/')), "serializer.pack.meta");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(metaDestination));
        Packer.pack(source, destination, file -> !file.getName().equals("SerializerPacker.class"), true, false, name -> {
            name = name.substring(0, name.indexOf('.'));
            return name.replace('/', '.');
        }, (file, start, length) -> {
            try {
                String name = file.getAbsolutePath().substring(source.getAbsolutePath().length() + 1).replace('/', '.').replace(".class", "");
                String current = "yapion.serializing.serializer." + file.getAbsolutePath().substring(source.getAbsolutePath().length() + 1).replace('/', '.').replace(".class", "");
                objectOutputStream.writeByte(0x10);
                objectOutputStream.writeUTF(name);
                objectOutputStream.writeByte(0x20);
                objectOutputStream.writeInt(start);
                objectOutputStream.writeByte(0x21);
                objectOutputStream.writeInt(length);

                Class<?> clazz = Class.forName(current);
                if (!clazz.isInterface() && !clazz.isMemberClass()) {
                    Object object = clazz.getConstructor().newInstance();
                    if (object instanceof InternalSerializer internalSerializer) {
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
                if (current.contains("special")) {
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
        });
        objectOutputStream.close();
    }
}
