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

import yapion.serializing.data.DeserializationMutationContext;
import yapion.serializing.views.Mutator;
import yapion.utils.MethodReturnValue;
import yapion.utils.ReflectionsUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class MutationManager {

    private Object mutator;
    private Map<String, Method> mutatorMethods = new HashMap<>();

    MutationManager(Class<? extends Mutator> mutatorClass, Class<?> mutationType) {
        if (mutatorClass != null) {
            mutator = ReflectionsUtils.constructObjectObjenesis(mutatorClass);
            for (Method method : mutatorClass.getDeclaredMethods()) {
                if (method.getReturnType() != mutationType && method.getReturnType() != void.class) {
                    continue;
                }
                Parameter[] parameters = method.getParameters();
                if (parameters.length != 1) {
                    continue;
                }
                if (parameters[0].getType() != mutationType) {
                    continue;
                }
                mutatorMethods.put(method.getName(), method);
            }
        }
    }

    public boolean hasMutation(String name) {
        return mutatorMethods.containsKey(name);
    }

    public MethodReturnValue<Object> mutate(String name, Object object) {
        return ReflectionsUtils.invokeMethodObjectSystem(mutatorMethods.get(name), mutator, object);
    }
}
