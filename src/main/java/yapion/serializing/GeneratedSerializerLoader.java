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

package yapion.serializing;

import lombok.experimental.UtilityClass;
import yapion.annotations.api.InternalAPI;
import yapion.annotations.registration.YAPIONSerializing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class GeneratedSerializerLoader {

    private static Set<Class<?>> allowedClasses = new HashSet<>();
    private static Set<String> allowedPackages = new HashSet<>();

    /**
     * Add a {@link Class} to the allowed list of generated serializer to be loaded. This class does not represent
     * the generated class but should represent the class for which the class was generated.
     *
     * @param clazz the {@link Class} to allow
     */
    public void addClass(Class<?> clazz) {
        allowedClasses.add(clazz);
    }

    /**
     * Add a {@link Package} to the allowed list of generated serializer to be loaded. Any {@link Class} lying in
     * this {@link Package} and any sub package are being allowed to be loaded.
     *
     * @param p the {@link Package} to allow
     */
    public void addPackage(Package p) {
        allowedPackages.add(p.getName() + ".");
    }

    /**
     * Add a {@link Class#getPackage()} to the allowed list of generated serializer to be loaded. Any {@link Class} lying in
     * this {@link Package} and any sub package are being allowed to be loaded.
     *
     * @param clazz the {@link Class#getPackage()} to allow
     */
    public void addPackage(Class<?> clazz) {
        allowedPackages.add(clazz.getPackage().getName() + ".");
    }

    @InternalAPI
    public boolean allowed(Class<?> clazz) {
        if (allowedClasses.contains(clazz)) return true;
        String packageName = clazz.getPackage().getName() + ".";
        return allowedPackages.stream().anyMatch(packageName::startsWith);
    }

    @InternalAPI
    public static boolean loadSerializerIfNeeded(Class<?> clazz) {
        if (clazz.getAnnotation(YAPIONSerializing.class) == null) {
            return false;
        }
        if (!allowed(clazz)) {
            return false;
        }
        try {
            System.out.println(Arrays.toString(clazz.getDeclaredClasses()));
            Class<?> serializerClass = Class.forName(clazz.getTypeName() + "Serializer");
            SerializeManager.add(serializerClass);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
