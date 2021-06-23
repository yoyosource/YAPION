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

package yapion;

import eu.infomas.annotation.AnnotationDetector;
import org.junit.Test;
import yapion.annotations.api.DeprecationInfo;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class DeprecationList {

    @Test
    public void deprecated() throws IOException {
        File file = new File("gradle.properties");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        AtomicInteger major = new AtomicInteger(0);
        AtomicInteger minor = new AtomicInteger(0);
        AtomicInteger patch = new AtomicInteger(0);
        bufferedReader.lines().forEach(s -> {
            if (s.startsWith("major = ")) {
                major.set(Integer.parseInt(s.substring(8)));
            }
            if (s.startsWith("minor = ")) {
                minor.set(Integer.parseInt(s.substring(8)));
            }
            if (s.startsWith("patch = ")) {
                patch.set(Integer.parseInt(s.substring(8)));
            }
        });
        System.out.println("Current Version: " + major.get() + "." + minor.get() + "." + patch.get());

        AnnotationDetector annotationDetector = new AnnotationDetector(new AnnotationDetector.MethodReporter() {
            @Override
            public void reportMethodAnnotation(Class<? extends Annotation> annotation, String className, String methodName) {
                Set<String> classes = new HashSet<>();
                if (!classes.add(className)) {
                    return;
                }
                try {
                    Class<?> clazz = Class.forName(className);
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (!method.getName().equals(methodName)) continue;
                        Deprecated deprecated = method.getAnnotation(Deprecated.class);
                        if (deprecated == null) continue;
                        DeprecationInfo deprecationInfo = method.getAnnotation(DeprecationInfo.class);
                        System.out.println("# " + method);
                        if (deprecationInfo != null) {
                            if (outputIfRemove(major, minor, patch, parseVersion(deprecationInfo.since()))) {
                                System.out.println("Remove      " + deprecationInfo.since());
                            } else {
                                System.out.println("Since       " + deprecationInfo.since());
                            }
                            System.out.println("Alternative " + deprecationInfo.alternative());
                        } else {
                            System.out.println("  * Unknown *");
                        }
                        System.out.println();
                    }
                } catch (ClassNotFoundException e) {

                }
            }

            @Override
            public Class<? extends Annotation>[] annotations() {
                return new Class[]{Deprecated.class};
            }
        });
        annotationDetector.detect("yapion");
    }

    private int[] parseVersion(String since) throws ClassNotFoundException {
        String[] strings = since.split("\\.");
        if (strings.length != 3) {
            throw new ClassNotFoundException();
        }
        int[] ints = new int[3];
        for (int i = 0; i < strings.length; i++) {
            ints[i] = Integer.parseInt(strings[i]);
        }
        return ints;
    }

    private boolean outputIfRemove(AtomicInteger major, AtomicInteger minor, AtomicInteger patch, int[] version) {
        if (version[0] < major.get()) {
            return true;
        }
        if (version[1] < minor.get() - 2) {
            return true;
        }
        return false;
    }

}
