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

package yapion.serializing.zar;

import yapion.hierarchy.types.YAPIONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class ZarTool {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            sendHelp();
            return;
        }

        Map<String, String[]> parsedArgs = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-c") || args[i].equals("-u")) {
                if (i < args.length - 2 && !args[i + 2].startsWith("-")) {
                    parsedArgs.put(args[i], new String[]{args[i + 1], args[i + 2]});
                    continue;
                }
                if (i < args.length - 1) {
                    parsedArgs.put(args[i], new String[]{args[i + 1], "."});
                    continue;
                }
                sendHelp();
                return;
            }

            if (args[i].equals("-l")) {
                if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                    parsedArgs.put(args[i], new String[]{args[i + 1], "."});
                    continue;
                }
                sendHelp();
                return;
            }

            if (args[i].equals("-r") || args[i].equals("-m")) {
                parsedArgs.put(args[i], null);
            }
        }

        Set<String> stringSet = new HashSet<>(parsedArgs.keySet());
        stringSet.remove("-c");
        stringSet.remove("-u");
        stringSet.remove("-r");
        stringSet.remove("-l");
        stringSet.remove("-m");
        if (!stringSet.isEmpty()) {
            sendHelp();
            return;
        }

        if (parsedArgs.containsKey("-c")) {
            create(parsedArgs);
        } else if (parsedArgs.containsKey("-u")) {
            unpack(parsedArgs);
        } else if (parsedArgs.containsKey("-l")) {
            list(parsedArgs);
        } else {
            sendHelp();
        }
    }

    private static void sendHelp() {
        System.out.println("-c [file] [source] - Create a zar archive");
        System.out.println("-r - Pack recursively");
        System.out.println("-m - Save MetaData");
        System.out.println();
        System.out.println("-u [file] [destination] - Unpack a zar archive");
        System.out.println();
        System.out.println("-l [file] - List files of archive");
    }

    private static void create(Map<String, String[]> stringMap) throws IOException {
        String[] createArgs = stringMap.get("-c");
        File archive = new File(createArgs[0]);
        File source = new File(createArgs[1]);

        archive.delete();
        archive.createNewFile();

        Stream<File> fileStream;
        if (stringMap.containsKey("-r")) {
            fileStream = Files.walk(source.toPath()).map(Path::toUri).map(File::new).filter(File::isFile);
        } else {
            if (source.listFiles() == null) return;
            fileStream = Stream.of(source.listFiles()).filter(File::isFile);
        }

        ZarOutputStream zarOutputStream = new ZarOutputStream(new BufferedOutputStream(new FileOutputStream(archive)));
        fileStream.forEach(file -> {
            try {
                YAPIONObject yapionObject = null;
                if (stringMap.containsKey("-m")) {
                    yapionObject = new YAPIONObject();
                    yapionObject.add("modified", file.lastModified());
                    yapionObject.add("read", file.canRead());
                    yapionObject.add("write", file.canWrite());
                    yapionObject.add("execute", file.canExecute());
                }
                zarOutputStream.addFile(file.getAbsolutePath().substring(source.getAbsolutePath().length() + 1), file.length(), yapionObject);

                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                while (bufferedInputStream.available() > 0) {
                    zarOutputStream.write(bufferedInputStream.read());
                }
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        });
        zarOutputStream.close();
    }

    private static void unpack(Map<String, String[]> stringMap) throws IOException {
        String[] createArgs = stringMap.get("-u");
        File archive = new File(createArgs[0]);
        File destination = new File(createArgs[1]);
        destination.mkdirs();

        ZarInputStream zarInputStream = new ZarInputStream(new BufferedInputStream(new FileInputStream(archive)));
        while (zarInputStream.hasFile()) {
            File file = new File(destination, zarInputStream.getFile());
            System.out.println(file);
            file.getParentFile().mkdirs();
            file.createNewFile();

            if (stringMap.containsKey("-m")) {
                YAPIONObject yapionObject = zarInputStream.getMetaData();
                if (yapionObject.containsKey("modified", Long.class)) {
                    file.setLastModified(yapionObject.getPlainValueOrDefault("modified", 0L));
                }
                if (yapionObject.containsKey("read", Long.class)) {
                    file.setReadable(yapionObject.getPlainValueOrDefault("read", true));
                }
                if (yapionObject.containsKey("write", Long.class)) {
                    file.setWritable(yapionObject.getPlainValueOrDefault("write", true));
                }
                if (yapionObject.containsKey("execute", Long.class)) {
                    file.setExecutable(yapionObject.getPlainValueOrDefault("execute", true));
                }
            }

            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                for (long i = 0; i < zarInputStream.getSize(); i++) {
                    bufferedOutputStream.write(zarInputStream.read());
                }
            }
        }
    }

    private static void list(Map<String, String[]> stringMap) throws IOException {
        String[] createArgs = stringMap.get("-l");
        File archive = new File(createArgs[0]);

        ZarInputStream zarInputStream = new ZarInputStream(new BufferedInputStream(new FileInputStream(archive)));
        while (zarInputStream.hasFile()) {
            System.out.println(zarInputStream.getFile() + "   " + zarInputStream.getSize() + "   " + zarInputStream.getMetaData());
            for (long i = 0; i < zarInputStream.getSize(); i++) {
                zarInputStream.read();
            }
        }
    }

}
