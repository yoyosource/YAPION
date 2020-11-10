package test;

import lombok.ToString;
import yapion.annotations.object.YAPIONData;
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

    public static void main(String[] args) {
        InstanceFactory<TestFactory> instanceFactory = new InstanceFactory<TestFactory>() {
            @Override
            public Class<TestFactory> type() {
                return TestFactory.class;
            }

            @Override
            public TestFactory instance() {
                return new TestFactory();
            }
        };
        if (false) {
            instanceFactory.add();
        }

        int round = 100;

        /*
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
        */

        benchmarkWithOffset(0, round);
        instanceFactory.add();
        benchmarkWithOffset(2, round);
        System.out.println(longMap);
    }

    private static void benchmarkWithOffset(int offset, int rounds) {
        for (int i = 0; i < 1024; i++) {
            if (i != 0) {
                System.out.println("ID: " + i);
            }
            long[] benchmark = new long[]{0, 0};
            for (int j = 0; j < rounds; j++) {
                long[] longs = benchmark(i);
                benchmark[0] += longs[0];
                benchmark[1] += longs[1];
            }
            if (!longMap.containsKey(i)) {
                longMap.put(i, new long[4]);
            }
            longMap.get(i)[offset + 0] = benchmark[0];
            longMap.get(i)[offset + 1] = benchmark[1];
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
