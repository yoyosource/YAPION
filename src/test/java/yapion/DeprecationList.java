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
import yapion.annotations.DeprecationInfo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class DeprecationList {

    @Test
    public void deprecated() throws IOException {
        Set<String> known = new HashSet<>();
        AnnotationDetector annotationDetector = new AnnotationDetector(new AnnotationDetector.MethodReporter() {
            @Override
            public void reportMethodAnnotation(Class<? extends Annotation> annotation, String className, String methodName) {
                if (known.contains(className + "@" + methodName)) return;
                try {
                    Class<?> clazz = Class.forName(className);
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (!method.getName().equals(methodName)) continue;
                        Deprecated deprecated = method.getAnnotation(Deprecated.class);
                        if (deprecated == null) continue;
                        DeprecationInfo deprecationInfo = method.getAnnotation(DeprecationInfo.class);
                        System.out.println("# " + method);
                        if (deprecationInfo != null) {
                            System.out.println("Since       " + deprecationInfo.since());
                            System.out.println("Alternative " + deprecationInfo.alternative());
                        } else {
                            System.out.println("  * Unknown *");
                        }
                        System.out.println();
                    }
                    known.add(className + "@" + methodName);
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

}
