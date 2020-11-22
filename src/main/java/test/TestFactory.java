// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package test;

import lombok.ToString;
import yapion.annotations.object.YAPIONData;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.api.InstanceFactory;

import java.util.HashMap;
import java.util.Map;

@YAPIONData
@ToString
public class TestFactory {

    private static final Map<Integer, long[]> longMap = new HashMap<>();
    private static final int round = 100;
    private static final InstanceFactory<TestFactory> instanceFactory = new InstanceFactory<TestFactory>() {
        @Override
        public Class<TestFactory> type() {
            return TestFactory.class;
        }

        @Override
        public TestFactory instance() {
            return new TestFactory();
        }
    };

    public static void main(String[] args) {
        if (false) {
            instanceFactory.add();
        }

        benchmarkWithOffset(0, round);
        instanceFactory.add();
        benchmarkWithOffset(2, round);
        System.out.println(YAPIONSerializer.serialize(longMap).toYAPIONPrettified(new StringOutput()).getResult());
        System.out.println();
        System.out.println(YAPIONSerializer.serialize(longMap).toYAPION(new StringOutput()).getResult());
    }

    private static void benchmarkTest1() {
        long[] serializeWithoutFactory = new long[]{0, 0};
        long[] serializeWithFactory = new long[]{0, 0};

        boolean b = false;
        for (int i = 0; i < round; i++) {
            if (i > round / 2 && !b) {
                instanceFactory.add();
                b = true;
                System.out.println();
            }
            long[] longs = benchmark(12);
            if (!b) {
                serializeWithoutFactory[0] += longs[0];
                serializeWithoutFactory[1] += longs[1];
            } else {
                serializeWithFactory[0] += longs[0];
                serializeWithFactory[1] += longs[1];
            }
        }

        System.out.println();
        System.out.println("Without Factory SERIALIZE");
        System.out.println("SERIALIZE: " + (serializeWithoutFactory[0] / (round / 2)) + "   DESERIALIZE: " + (serializeWithoutFactory[1] / (round / 2)));
        System.out.println("With Factory SERIALIZE");
        System.out.println("SERIALIZE: " + (serializeWithFactory[0] / (round / 2)) + "   DESERIALIZE: " + (serializeWithFactory[1] / (round / 2)));
    }

    private static void benchmarkWithOffset(int offset, int rounds) {
        int benchmarkOffset = 128 * 2;
        for (int i = 0; i < 128; i++) {
            System.out.println("ID: " + (i + benchmarkOffset));
            long[] benchmark = new long[]{0, 0};
            for (int j = 0; j < rounds; j++) {
                long[] longs = benchmark(i + benchmarkOffset);
                benchmark[0] += longs[0];
                benchmark[1] += longs[1];
            }
            if (!longMap.containsKey(i + benchmarkOffset)) {
                longMap.put(i + benchmarkOffset, new long[4]);
            }
            longMap.get(i + benchmarkOffset)[offset + 0] = benchmark[0] / rounds;
            longMap.get(i + benchmarkOffset)[offset + 1] = benchmark[1] / rounds;
        }
    }

    private static long[] benchmark(int size) {
        TestFactory testFactorySerialize = new TestFactory(size);

        long serializeTime = System.currentTimeMillis();
        YAPIONObject yapionObject = YAPIONSerializer.serialize(testFactorySerialize);
        serializeTime = System.currentTimeMillis() - serializeTime;

        // System.out.println(yapionObject);

        long deserializeTime = System.currentTimeMillis();
        TestFactory testFactoryDeserialize = (TestFactory) YAPIONDeserializer.deserialize(yapionObject);
        deserializeTime = System.currentTimeMillis() - deserializeTime;

        // System.out.println(testFactoryDeserialize);
        // System.out.println("SERIALIZE: " + serializeTime + "   DESERIALIZE: " + deserializeTime + "   " + testFactorySerialize.toString().equals(testFactoryDeserialize.toString()));
        return new long[]{serializeTime, deserializeTime};
    }

    private TestFactory() {

    }

    private TestFactory testFactory1;
    private TestFactory testFactory2;
    @ToString.Exclude
    private TestFactory itself;
    private int index;

    public TestFactory(int index) {
        this.index = index;
        itself = this;
        if (index > 0) {
            testFactory1 = new TestFactory(index - 1);
            // testFactory2 = new TestFactory(index - 1);
        }
    }

}