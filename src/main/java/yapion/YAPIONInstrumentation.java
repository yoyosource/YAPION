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

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class YAPIONInstrumentation {

    private List<MethodCall> finishedMethodCalls = new ArrayList<>();
    private Map<Thread, MethodCall> instrumentations = new HashMap<>();

    private static class MethodCall {

        private MethodCall parent = null;

        private long startTime;
        private long endTime;

        private String className;
        private String methodName;

        private List<MethodCall> methodCallList = new LinkedList<>();

        @Override
        public String toString() {
            StringBuilder st = new StringBuilder();
            st.append("MethodCall{");
            if (parent == null) {
                st.append(startTime).append("-").append(endTime).append(" (").append(endTime - startTime).append("ms)");
            } else {
                st.append("+").append(startTime - parent.startTime).append(" (").append(endTime - startTime).append("ms)");
            }
            st.append(", ");
            st.append(className).append(" ").append(methodName);
            if (!methodCallList.isEmpty()) {
                st.append(", ").append(methodCallList);
            }
            return st.toString();
        }
    }

    private StackTraceElement stackTraceElement(Thread thread) {
        return thread.getStackTrace()[3];
    }

    public void METHOD_START() {
        long currentTime = System.currentTimeMillis();
        Thread thread = Thread.currentThread();
        methodStart(currentTime, thread, stackTraceElement(thread));
    }

    public void METHOD_END() {
        long currentTime = System.currentTimeMillis();

        Thread thread = Thread.currentThread();
        methodStop(currentTime, thread, stackTraceElement(thread));
    }

    public void METHOD_INFO() {
        long currentTime = System.currentTimeMillis();

        Thread thread = Thread.currentThread();
        StackTraceElement stackTraceElement = stackTraceElement(thread);
        methodStart(currentTime, thread, stackTraceElement);
        methodStop(currentTime, thread, stackTraceElement);
    }

    private void methodStart(long currentTime, Thread thread, StackTraceElement stackTraceElement) {
        MethodCall methodCall = instrumentations.get(thread);
        if (methodCall == null) {
            methodCall = new MethodCall();
            methodCall.methodName = stackTraceElement.getMethodName();
            methodCall.className = stackTraceElement.getClassName();
            methodCall.startTime = currentTime;
            instrumentations.put(thread, methodCall);
            return;
        }

        MethodCall methodCallStart = new MethodCall();
        methodCallStart.methodName = stackTraceElement.getMethodName();
        methodCallStart.className = stackTraceElement.getClassName();
        methodCallStart.startTime = currentTime;
        methodCallStart.parent = methodCall;
        methodCall.methodCallList.add(methodCallStart);
        instrumentations.put(thread, methodCallStart);
    }

    private void methodStop(long currentTime, Thread thread, StackTraceElement stackTraceElement) {
        MethodCall methodCall = instrumentations.get(thread);
        if (methodCall == null) {
            return;
        }
        if (!methodCall.className.equals(stackTraceElement.getClassName())) {
            return;
        }
        if (!methodCall.methodName.equals(stackTraceElement.getMethodName())) {
            return;
        }
        methodCall.endTime = currentTime;
        if (methodCall.parent == null) {
            finishedMethodCalls.add(methodCall);
            instrumentations.remove(thread);
            return;
        }
        instrumentations.put(thread, methodCall.parent);
    }

    public void main(String[] args) {
        METHOD_START();
        System.out.println("Hello World");
        test();
        test();
        test();
        test();
        test();
        METHOD_END();
        System.out.println(finishedMethodCalls);
    }

    private void test() {
        METHOD_START();
        System.out.println("This is another Hello World");
        test2();
        METHOD_END();
    }

    private void test2() {
        METHOD_INFO();
    }
}
