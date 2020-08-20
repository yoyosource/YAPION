package yapion.serializing;

import system.ObjenesisTest;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class YAPIONTestObjects {

    @YAPIONSave
    @YAPIONLoad
    public static class TestObject {
        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestObject;
        }
    }

    @YAPIONSave
    @YAPIONLoad
    public static class TestMap {

        private final Map<String, String> stringStringMap = new HashMap<>();
        private final Map<String, String> hugoStringMap = new HashMap<>();
        private final HashMap<String, String> hashMap = new HashMap<>();
        private final LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>();
        {
            stringStringMap.put("Hello", "Hello");
            stringStringMap.put("Hello1", "Hello1");
            stringStringMap.put("Hello2", "Hello2");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestMap)) return false;
            TestMap testMap = (TestMap) o;
            return Objects.equals(stringStringMap, testMap.stringStringMap) &&
                    Objects.equals(hugoStringMap, testMap.hugoStringMap) &&
                    Objects.equals(hashMap, testMap.hashMap) &&
                    Objects.equals(linkedHashMap, testMap.linkedHashMap);
        }

        @Override
        public int hashCode() {
            return Objects.hash(stringStringMap, hugoStringMap, hashMap, linkedHashMap);
        }
    }

    @YAPIONSave
    @YAPIONLoad
    public static class TestArray {

        private final String[] strings = new String[2];
        private final String[][] strings1 = new String[3][3];
        private final List<String> strings2 = new ArrayList<>();
        private final List<String> strings3 = new LinkedList<>();
        {
            strings[0] = "Hello World";
            strings1[0][0] = "Hello World";
            strings1[1][1] = "Hello World2";
            strings1[2][2] = "Hello World3";

            strings2.add("Hello World");
            strings2.add("Hello World1");
            strings2.add("Hello World2");
            strings2.add("Hello World3");

            strings3.add("Hello World");
            strings3.add("Hello World1");
            strings3.add("Hello World2");
            strings3.add("Hello World3");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestArray)) return false;
            TestArray testArray = (TestArray) o;
            return Arrays.equals(strings, testArray.strings) &&
                    Arrays.equals(strings1, testArray.strings1) &&
                    Objects.equals(strings2, testArray.strings2) &&
                    Objects.equals(strings3, testArray.strings3);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(strings2, strings3);
            result = 31 * result + Arrays.hashCode(strings);
            result = 31 * result + Arrays.hashCode(strings1);
            return result;
        }
    }

    @YAPIONSave
    @YAPIONLoad
    public static class TestPrimitive {

        private final byte b = 0;
        private final Byte B = 0;
        private final short s = 0;
        private final Short S = 0;
        private final int i = 0;
        private final Integer I = 0;
        private final long l = 0;
        private final Long L = 0L;
        private final double d = 0;
        private final Double D = 0D;
        private final float f = 0;
        private final Float F = 0F;
        private final BigInteger bi = BigInteger.valueOf(0);
        private final BigDecimal bd = BigDecimal.valueOf(0);

        private final char c = ' ';
        private final Character C = ' ';

        private final String string = "";
        private final StringBuilder stringB = new StringBuilder();
        private final StringBuffer stringb = new StringBuffer();

        private final File file = new File(getUserHome() + "/YAPI");

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestPrimitive)) return false;
            TestPrimitive that = (TestPrimitive) o;
            return b == that.b &&
                    s == that.s &&
                    i == that.i &&
                    l == that.l &&
                    Double.compare(that.d, d) == 0 &&
                    Float.compare(that.f, f) == 0 &&
                    c == that.c &&
                    Objects.equals(B, that.B) &&
                    Objects.equals(S, that.S) &&
                    Objects.equals(I, that.I) &&
                    Objects.equals(L, that.L) &&
                    Objects.equals(D, that.D) &&
                    Objects.equals(F, that.F) &&
                    Objects.equals(bi, that.bi) &&
                    Objects.equals(bd, that.bd) &&
                    Objects.equals(C, that.C) &&
                    Objects.equals(string, that.string) &&
                    Objects.equals(stringB, that.stringB) &&
                    Objects.equals(stringb, that.stringb) &&
                    Objects.equals(file, that.file);
        }

        @Override
        public int hashCode() {
            return Objects.hash(b, B, s, S, i, I, l, L, d, D, f, F, bi, bd, c, C, string, stringB, stringb, file);
        }
    }

    static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static void main(String[] args) {
        String s1 = YAPIONSerializer.serialize(new TestObject()).toString();
        String s2 = YAPIONSerializer.serialize(new TestMap()).toString();
        String s3 = YAPIONSerializer.serialize(new TestArray()).toString();
        String s4 = YAPIONSerializer.serialize(new TestPrimitive()).toString();

        System.out.println(ObjenesisTest.encodeBase64(s1));
        System.out.println(ObjenesisTest.encodeBase64(s2));
        System.out.println(ObjenesisTest.encodeBase64(s3));
        System.out.println(ObjenesisTest.encodeBase64(s4));

        System.out.println(ObjenesisTest.decodeBase64("e0B0eXBlKH"));
        System.out.println(ObjenesisTest.encodeBase64("{@type("));
    }

}
