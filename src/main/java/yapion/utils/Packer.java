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
import yapion.hierarchy.types.YAPIONObject;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.zip.GZIPOutputStream;

@InternalAPI
@UtilityClass
public class Packer {

    public void pack(File source, File destination, Predicate<File> filter, boolean deleteAfterPack, boolean gzip, UnaryOperator<String> fileMapper, BiConsumer<File, YAPIONObject> metaDataConsumer) throws Exception {
        if (!source.exists()) return;
        if (!source.isDirectory()) return;

        destination.getParentFile().mkdirs();
        destination.delete();
        destination.createNewFile();
        System.out.println(destination.getAbsolutePath());

        YAPIONObject metaData = new YAPIONObject();
        AtomicInteger index = new AtomicInteger();

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination));
        OutputStream outputStream = gzip ? new GZIPOutputStream(bufferedOutputStream) : bufferedOutputStream;

        int substringLength = source.getAbsolutePath().length() + 1;
        internalPack(outputStream, substringLength, source, filter, deleteAfterPack, metaData, index, fileMapper, metaDataConsumer);
        outputStream.close();

        metaData.toFile(new File(destination.getAbsolutePath() + ".meta"));
    }

    private void internalPack(OutputStream outputStream, int substringLength, File file, Predicate<File> filter, boolean deleteAfterPack, YAPIONObject metaData, AtomicInteger index, UnaryOperator<String> fileMapper, BiConsumer<File, YAPIONObject> metaDataConsumer) throws IOException {
        if (file == null) return;
        File[] files = file.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (!f.isFile()) continue;
            if (!filter.test(f)) continue;

            String fileName = f.getAbsolutePath().replace('\\', '/');
            fileName = fileName.substring(substringLength);
            System.out.println("Packing: " + fileName);
            fileName = fileMapper.apply(fileName);

            metaData.add(fileName, new YAPIONObject()
                    .add("start", index.get())
                    .add("length", (int) f.length())
            );
            metaDataConsumer.accept(f, metaData.getObject(fileName));

            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(f));) {
                for (int i = 0; i < f.length(); i++) {
                    outputStream.write(bufferedInputStream.read());
                }
                index.getAndAdd((int) f.length());
            }
            if (deleteAfterPack) {
                f.delete();
            }
        }

        for (File f : files) {
            if (!f.isDirectory()) continue;
            internalPack(outputStream, substringLength, f, filter, deleteAfterPack, metaData, index, fileMapper, metaDataConsumer);
            if (deleteAfterPack) {
                f.delete();
            }
        }
    }
}
