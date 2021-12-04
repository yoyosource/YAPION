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

/**
 * This class will be deleted by compile process
 */
public class SerializerPacker {

    public static void main(String[] args) throws Exception {
        String s = SerializerPacker.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        s += SerializerPacker.class.getPackage().getName().replace('.', '/');

        File source = new File(s);
        if (!source.exists()) return;

        File destination = new File(s.substring(0, s.lastIndexOf('/')), "serializer.pack");
        Packer.pack(source, destination, file -> !file.getName().equals("SerializerPacker.class"), true, false, name -> {
            name = name.substring(0, name.indexOf('.'));
            return name.replace('/', '.');
        }, (file, metaData) -> {
            try {
                String current = "yapion.serializing.serializer." + file.getAbsolutePath().substring(source.getAbsolutePath().length() + 1).replace('/', '.').replace(".class", "");
                Class<?> clazz = Class.forName(current);
                if (!clazz.isInterface() && !clazz.isMemberClass()) {
                    Object object = clazz.getConstructor().newInstance();
                    if (object instanceof InternalSerializer internalSerializer) {
                        if (internalSerializer.type() != null) {
                            metaData.put("t", internalSerializer.type().getTypeName()); // t -> type
                        }
                        if (internalSerializer.primitiveType() != null) {
                            metaData.put("pt", internalSerializer.primitiveType().getTypeName()); // pt -> primitiveType
                        }
                        if (internalSerializer.interfaceType() != null) {
                            metaData.put("it", internalSerializer.interfaceType().getTypeName()); // it -> interfaceType
                        }
                        if (internalSerializer.classType() != null) {
                            metaData.put("ct", internalSerializer.classType().getTypeName()); // ct -> classType
                        }
                    } else if (object instanceof YAPIONSerializerRegistrator) {
                        metaData.put("dl", true); // dl -> directLoad
                    }
                }
                if (current.contains("special")) {
                    metaData.put("dl", true); // dl -> directLoad
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }
}
