package yapion.serializing;

import system.ObjenesisTest;
import yapion.annotations.YAPIONData;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class YAPIONTestObjects {

    @YAPIONData
    public static class TestObject {
        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestObject;
        }

        @Override
        public String toString() {
            return "TestObject{}";
        }

    }

    @YAPIONData
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

        @Override
        public String toString() {
            return "TestMap{" +
                    "stringStringMap=" + stringStringMap +
                    ", hugoStringMap=" + hugoStringMap +
                    ", hashMap=" + hashMap +
                    ", linkedHashMap=" + linkedHashMap +
                    '}';
        }

    }

    @YAPIONData
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

        @Override
        public String toString() {
            return "TestArray{" +
                    "strings=" + Arrays.toString(strings) +
                    ", strings1=" + Arrays.toString(strings1) +
                    ", strings2=" + strings2 +
                    ", strings3=" + strings3 +
                    '}';
        }

    }

    @YAPIONData
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
                    B.compareTo(that.B) == 0 &&
                    S.compareTo(that.S) == 0 &&
                    I.compareTo(that.I) == 0 &&
                    L.compareTo(that.L) == 0 &&
                    D.compareTo(that.D) == 0&&
                    F.compareTo(that.F) == 0 &&
                    bi.compareTo(that.bi) == 0 &&
                    bd.compareTo(that.bd) == 0 &&
                    C.compareTo(that.C) == 0 &&
                    string.equals(that.string) &&
                    stringB.toString().equals(that.stringB.toString()) &&
                    stringb.toString().equals(that.stringb.toString()) &&
                    file.equals(that.file);
        }

        @Override
        public int hashCode() {
            return Objects.hash(b, B, s, S, i, I, l, L, d, D, f, F, bi, bd, c, C, string, stringB.toString(), stringb.toString(), file);
        }

        @Override
        public String toString() {
            return "TestPrimitive{" +
                    "b=" + b +
                    ", B=" + B +
                    ", s=" + s +
                    ", S=" + S +
                    ", i=" + i +
                    ", I=" + I +
                    ", l=" + l +
                    ", L=" + L +
                    ", d=" + d +
                    ", D=" + D +
                    ", f=" + f +
                    ", F=" + F +
                    ", bi=" + bi +
                    ", bd=" + bd +
                    ", c=" + c +
                    ", C=" + C +
                    ", string='" + string + '\'' +
                    ", stringB=" + stringB +
                    ", stringb=" + stringb +
                    ", file=" + file +
                    '}';
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
