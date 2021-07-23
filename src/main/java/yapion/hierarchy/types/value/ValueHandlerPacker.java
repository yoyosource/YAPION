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

package yapion.hierarchy.types.value;

import yapion.utils.Packer;

import java.io.File;

/**
 * This class will be deleted by compile process
 */
public class ValueHandlerPacker {

    public static void main(String[] args) throws Exception {
        String s = ValueHandlerPacker.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        s += ValueHandlerPacker.class.getPackage().getName().replace('.', '/');

        File source = new File(s);
        if (!source.exists()) return;

        File destination = new File(s, "valueHandler.zar.gz");

        Packer.pack(source, destination, file -> file.getName().endsWith("Handler.class") && !file.getName().equals("ValueHandler.class") || file.getName().equals("NumberSuffix.class"), true);
    }
}
