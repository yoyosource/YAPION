package yapion.serializing;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.junit.BeforeClass;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.serialize.YAPIONSave;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class YAPIONTestObjects {

    @YAPIONData
    @ToString
    @EqualsAndHashCode
    public static class TestObject {

    }

    @YAPIONData
    @ToString
    @EqualsAndHashCode
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
    }

    @YAPIONData
    @ToString
    @EqualsAndHashCode
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
    }

    @YAPIONData
    @ToString
    @EqualsAndHashCode
    public static class TestPrimitive {

        private final boolean t = false;
        private final Boolean T = true;
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
        @EqualsAndHashCode.Exclude final StringBuilder stringB = new StringBuilder();
        @EqualsAndHashCode.Exclude final StringBuffer stringb = new StringBuffer();

        private final File file = new File(getUserHome() + "/YAPI");

        // file.getPath()
    }

    private enum EnumerationTest {
        Hello,
        World,
        Hugo,
        My,
        Name,
    }

    @YAPIONSave
    @YAPIONLoad
    @ToString
    @EqualsAndHashCode
    public static class TestEnum {

        private final EnumerationTest test1 = EnumerationTest.Hello;
        private final EnumerationTest test2 = EnumerationTest.World;
        private final EnumerationTest test3 = EnumerationTest.Hugo;
        private final EnumerationTest test4 = EnumerationTest.My;
        private final EnumerationTest test5 = EnumerationTest.Name;

    }

    public static class NoAnnotations {}

    @YAPIONData
    public static class TestInnerArray {

        private List<long[][]> intList = new ArrayList<>();

        {
            intList.add(new long[][]{{0}, {1}, {2}});
            intList.add(new long[][]{{1}, {2}, {3}});
            intList.add(new long[][]{{2}, {3}, {4}});
        }

        @Override
        public String toString() {
            StringBuilder st = new StringBuilder().append("Test{intList={");
            boolean b = false;
            for (long[][] ints : intList) {
                if (b) {
                    st.append(", ");
                }
                b = true;
                st.append(ints.getClass().getTypeName()).append(Arrays.deepToString(ints));
            }
            return st.append("}}").toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestInnerArray)) return false;
            TestInnerArray that = (TestInnerArray) o;
            if (intList.size() != that.intList.size()) return false;
            for (int i = 0; i < intList.size(); i++) {
                if (!Arrays.deepEquals(intList.get(i), that.intList.get(i))) return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(intList);
        }
    }

    static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static void main(String[] args) {
        YAPIONObject yapionObject1 = YAPIONSerializer.serialize(new TestObject());
        YAPIONObject yapionObject2 = YAPIONSerializer.serialize(new TestMap());
        YAPIONObject yapionObject3 = YAPIONSerializer.serialize(new TestArray());
        YAPIONObject yapionObject4 = YAPIONSerializer.serialize(new TestPrimitive());
        YAPIONObject yapionObject5 = YAPIONSerializer.serialize(new TestEnum());

        if (true) return;

        System.out.println(yapionObject1);
        System.out.println(yapionObject1.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parseJSON(yapionObject1.toJSONLossy(new StringOutput()).getResult()));
        System.out.println(encodeBase64(yapionObject1.toString()));
        System.out.println(encodeBase64(yapionObject1.toJSONLossy(new StringOutput()).getResult()));
        System.out.println();
        System.out.println(yapionObject2);
        System.out.println(yapionObject2.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parseJSON(yapionObject2.toJSONLossy(new StringOutput()).getResult()));
        System.out.println(encodeBase64(yapionObject2.toString()));
        System.out.println(encodeBase64(yapionObject2.toJSONLossy(new StringOutput()).getResult()));
        System.out.println();
        System.out.println(yapionObject3);
        System.out.println(yapionObject3.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parseJSON(yapionObject3.toJSONLossy(new StringOutput()).getResult()));
        System.out.println(encodeBase64(yapionObject3.toString()));
        System.out.println(encodeBase64(yapionObject3.toJSONLossy(new StringOutput()).getResult()));
        System.out.println();
        System.out.println(yapionObject4);
        System.out.println(yapionObject4.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parseJSON(yapionObject4.toJSONLossy(new StringOutput()).getResult()));
        System.out.println(encodeBase64(yapionObject4.toString()));
        System.out.println(encodeBase64(yapionObject4.toJSONLossy(new StringOutput()).getResult()));
        System.out.println();
        System.out.println(yapionObject5);
        System.out.println(yapionObject5.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parseJSON(yapionObject5.toJSONLossy(new StringOutput()).getResult()));
        System.out.println(encodeBase64(yapionObject5.toString()));
        System.out.println(encodeBase64(yapionObject5.toJSONLossy(new StringOutput()).getResult()));

        System.out.println(decodeBase64("e0B0eXBlKH"));
        System.out.println(encodeBase64("{@type("));
    }

    public static String encodeBase64(String s) {
        return new String(Base64.getEncoder().encode(s.getBytes()), StandardCharsets.UTF_8);
    }

    public static String decodeBase64(String base64) {
        return new String(Base64.getDecoder().decode(base64.getBytes()), StandardCharsets.UTF_8);
    }

}
