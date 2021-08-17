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
import yapion.annotations.api.InternalAPI;
import yapion.serializing.zar.ZarOutputStream;

import java.io.*;
import java.util.function.Predicate;
import java.util.zip.GZIPOutputStream;

/**
 * This class will be deleted by compile process
 */
@InternalAPI
@UtilityClass
public class Packer {

    public void pack(File source, File destination, Predicate<File> filter, boolean deleteAfterPack) throws Exception {
        if (!source.exists()) return;

        destination.getParentFile().mkdirs();
        destination.delete();
        destination.createNewFile();
        System.out.println(destination.getAbsolutePath());

        ZarOutputStream zarOutputStream = new ZarOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(destination))));
        int substringLength = source.getAbsolutePath().length() + 1;
        packIntoZarFile(zarOutputStream, substringLength, source, filter, deleteAfterPack);
        zarOutputStream.close();
        System.out.println("Destination Size: " + destination.length());
    }

    private void packIntoZarFile(ZarOutputStream zarOutputStream, int substringLength, File file, Predicate<File> filter, boolean deleteAfterPack) throws IOException {
        if (file == null) return;
        File[] files = file.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (!f.isFile()) continue;
            if (!filter.test(f)) continue;

            String fileName = f.getAbsolutePath().replace('\\', '/');
            System.out.println("Packing: " + fileName);
            fileName = fileName.substring(substringLength);

            zarOutputStream.addFile(fileName, f.length());
            FileInputStream fileInputStream = new FileInputStream(f);
            for (int i = 0; i < f.length(); i++) {
                zarOutputStream.write(fileInputStream.read());
            }
            if (deleteAfterPack) {
                f.delete();
            }
        }

        for (File f : files) {
            if (!f.isDirectory()) continue;
            packIntoZarFile(zarOutputStream, substringLength, f, filter, deleteAfterPack);
            if (deleteAfterPack) {
                f.delete();
            }
        }
    }

}
