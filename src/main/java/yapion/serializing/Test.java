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

import lombok.AllArgsConstructor;
import yapion.hierarchy.types.YAPIONObject;

import java.util.*;

public class Test {

    public static void main(String[] args) {
        // profile();
        Random random = new Random();
        YAPIONObject yapionObject = YAPIONSerializer.serialize(random);
        System.out.println(yapionObject);
        System.out.println((Random) YAPIONDeserializer.deserialize(yapionObject));

        /*
        long time = System.currentTimeMillis();
        System.out.println(SerializeManager.isRecord);
        System.out.println(System.currentTimeMillis() - time);
         */
    }

    private static void profile() {
        Map<String, Long> timings = new HashMap<>();
        Thread thread = new Thread(() -> {
            long time = 0;
            while (true) {
                time++;
                if (time > 10000L) {
                    break;
                }
                Thread.getAllStackTraces().forEach((thread1, stackTraceElements) -> {
                    if (thread1.getName().equals("YAPION-Profiler")) {
                        return;
                    }
                    if (!thread1.getName().equals("main")) {
                        return;
                    }
                    StackTraceElement stackTraceElement = stackTraceElements[0];
                    String name = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
                    if (name.startsWith("jdk.internal")) {
                        return;
                    }
                    if (name.startsWith("sun")) {
                        return;
                    }
                    if (name.startsWith("ch.qos.logback")) {
                        return;
                    }
                    if (name.startsWith("org.slf4j")) {
                        return;
                    }
                    if (timings.containsKey(name)) {
                        timings.put(name, timings.get(name) + 1);
                    } else {
                        timings.put(name, 1L);
                    }
                });
            }

            System.out.println();
            List<ProfilingTest> profilingTestList = new ArrayList<>();
            for (Map.Entry<String, Long> entry : timings.entrySet()) {
                if (entry.getValue() <= 5) {
                    continue;
                }
                profilingTestList.add(new ProfilingTest(entry.getKey(), entry.getValue()));
            }
            Collections.sort(profilingTestList);
            for (ProfilingTest profilingTest : profilingTestList) {
                System.out.println(profilingTest);
            }
        });
        thread.setName("YAPION-Profiler");
        thread.start();
    }

    @AllArgsConstructor
    private static class ProfilingTest implements Comparable<ProfilingTest> {
        private String name;
        private long time;

        @Override
        public int compareTo(Test.ProfilingTest o) {
            return Long.compare(time, o.time);
        }

        @Override
        public String toString() {
            return name + ": " + time;
        }
    }
}
