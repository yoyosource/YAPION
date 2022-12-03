/*
 * Copyright 2022 yoyosource
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

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ResolutionGraph<I, O> {

    private Map<I, O> results = new IdentityHashMap<>();
    private Map<I, Runnable> finalizers = new IdentityHashMap<>();
    private Map<I, Resolver<?, ?, O>> resolvers = new IdentityHashMap<>();
    private Map<I, List<Pair<?, I>>> dependencies = new IdentityHashMap<>();

    private BiConsumer<I, O> supplier;
    private BiFunction<I, O, O> mutator;

    public ResolutionGraph(BiConsumer<I, O> supplier, BiFunction<I, O, O> mutator) {
        this.supplier = supplier;
        this.mutator = mutator;
    }

    public <K, S extends O> DependencySupplier<K, I> register(I current, S result, Resolver<S, K, O> resolver) {
        results.put(current, result);
        supplier.accept(current, result);
        resolvers.put(current, resolver);
        return new DependencySupplier<>() {
            @Override
            public DependencySupplier<K, I> depends(K key, I value) {
                ResolutionGraph.this.dependencies.computeIfAbsent(current, k -> new LinkedList<>()).add(new Pair<>(key, value));
                return this;
            }

            @Override
            public DependencySupplier<K, I> finalizer(Runnable runnable) {
                finalizers.put(current, runnable);
                return this;
            }
        };
    }

    public void register(I current, O result) {
        results.put(current, result);
        supplier.accept(current, result);
        resolve();
    }

    public boolean isFinished() {
        resolve();
        return dependencies.values().stream().allMatch(List::isEmpty);
    }

    public I getUnresolved() {
        return dependencies.values().stream().filter(set -> !set.isEmpty()).findFirst().map(pairs -> {
            for (Pair<?, I> pair : pairs) {
                if (!results.containsKey(pair.v)) {
                    return pair.v;
                }
            }
            return null;
        }).orElse(null);
    }

    private void resolve() {
        for (Map.Entry<I, List<Pair<?, I>>> entry : dependencies.entrySet()) {
            List<Pair<?, I>> values = entry.getValue();
            if (values.isEmpty()) continue;
            Pair<?, I> pair = values.get(0);
            if (results.containsKey(pair.v)) {
                Resolver resolver = resolvers.get(entry.getKey());
                resolver.accept(results.get(entry.getKey()), new Pair(pair.k, mutator.apply(pair.v, results.get(pair.v))));
                System.out.println("Resolved " + results.get(entry.getKey()) + " with " + pair);
                values.remove(0);
            }
            if (values.isEmpty()) {
                Runnable runnable = finalizers.get(entry.getKey());
                if (runnable != null) runnable.run();
            }
        }
    }
}
