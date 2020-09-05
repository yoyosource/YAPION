package yapion.serializing;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.object.YAPIONData;
import yapion.annotations.serialize.YAPIONSave;

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
        private final StringBuilder stringB = new StringBuilder();
        private final StringBuffer stringb = new StringBuffer();

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

    static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static void main(String[] args) {
        String s1 = YAPIONSerializer.serialize(new TestObject()).toString();
        String s2 = YAPIONSerializer.serialize(new TestMap()).toString();
        String s3 = YAPIONSerializer.serialize(new TestArray()).toString();
        String s4 = YAPIONSerializer.serialize(new TestPrimitive()).toString();

        System.out.println(encodeBase64(s1));
        System.out.println(encodeBase64(s2));
        System.out.println(encodeBase64(s3));
        System.out.println(encodeBase64(s4));

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
