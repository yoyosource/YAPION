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

import yapion.serializing.zar.ZarOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * This is a compile time class to pack the serializer into a .tar.gz
 */
public class SerializerPacker {

    public static void main(String[] args) throws Exception {
        String s = SerializerPacker.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        s += SerializerPacker.class.getPackage().getName().replace('.', '/');

        File source = new File(s);
        if (!source.exists()) return;

        File destination = new File(s.substring(0, s.lastIndexOf('/')), "serializer.zar.gz");
        System.out.println(destination.getAbsolutePath());
        destination.delete();
        destination.createNewFile();
        ZarOutputStream zarOutputStream = new ZarOutputStream(new GZIPOutputStream(new FileOutputStream(destination), 512));

        int substringLength = s.length() + 1;
        packIntoZarFile(zarOutputStream, substringLength, source);
        zarOutputStream.close();
        System.out.println("Destination Size: " + destination.length());
    }

    private static void packIntoZarFile(ZarOutputStream zarOutputStream, int substringLength, File file) throws IOException {
        if (file == null) return;
        File[] files = file.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (!f.isFile()) continue;

            System.out.println("Packing: " + f.getAbsolutePath());
            if (f.getName().equals("SerializerPacker.class")) continue;
            String fileName = f.getAbsolutePath().substring(substringLength);

            zarOutputStream.addFile(fileName, f.length());
            FileInputStream fileInputStream = new FileInputStream(f);
            for (int i = 0; i < f.length(); i++) {
                zarOutputStream.write(fileInputStream.read());
            }
        }

        for (File f : files) {
            if (!f.isDirectory()) continue;
            packIntoZarFile(zarOutputStream, substringLength, f);
        }
    }
}
