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

import lombok.EqualsAndHashCode;
import lombok.ToString;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.object.*;
import yapion.annotations.serialize.YAPIONSave;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;
import yapion.serializing.data.DeserializationContext;
import yapion.serializing.data.SerializationContext;
import yapion.serializing.views.View;

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

        public TestMap() {
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

        public TestArray() {
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

        private final List<long[][]> intList = new ArrayList<>();

        public TestInnerArray() {
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

    public interface CNC {

    }

    @YAPIONData(cascading = true)
    public static class Cascading implements CNC {

        private final CNC cnc1;
        private CNC cnc2 = null;

        public Cascading(CNC cnc1) {
            this.cnc1 = cnc1;
        }

        public Cascading(CNC cnc1, CNC cnc2) {
            this.cnc1 = cnc1;
            this.cnc2 = cnc2;
        }
    }

    // Class<? extends View>
    @YAPIONSave(context = EmptyView.class)
    @YAPIONLoad(context = EmptyView.class)
    public static class NonCascading implements CNC {

        @YAPIONField(context = EmptyView.class)
        private final CNC cnc1;

        private CNC cnc2 = null;

        public NonCascading(CNC cnc1) {
            this.cnc1 = cnc1;
        }

        public NonCascading(CNC cnc1, CNC cnc2) {
            this.cnc1 = cnc1;
            this.cnc2 = cnc2;
        }
    }

    public static class NonSaved implements CNC {

        private final CNC cnc1;
        private CNC cnc2 = null;

        public NonSaved(CNC cnc1) {
            this.cnc1 = cnc1;
        }

        public NonSaved(CNC cnc1, CNC cnc2) {
            this.cnc1 = cnc1;
            this.cnc2 = cnc2;
        }
    }

    @YAPIONData
    @ToString
    @EqualsAndHashCode
    public static class TestReduced {

        private final Map<String, String> stringStringMap = new HashMap<>();
        private final Map<String, String> hugoStringMap = new HashMap<>();
        private final HashMap<String, String> hashMap = new HashMap<>();
        private final LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>();

        private final String[] strings = new String[2];
        private final String[][] strings1 = new String[3][3];
        private final List<String> strings2 = new ArrayList<>();
        private final List<String> strings3 = new LinkedList<>();

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

        private final EnumerationTest test1 = EnumerationTest.Hello;
        private final EnumerationTest test2 = EnumerationTest.World;
        private final EnumerationTest test3 = EnumerationTest.Hugo;
        private final EnumerationTest test4 = EnumerationTest.My;
        private final EnumerationTest test5 = EnumerationTest.Name;

        public TestReduced() {
            stringStringMap.put("Hello", "Hello");
            stringStringMap.put("Hello1", "Hello1");
            stringStringMap.put("Hello2", "Hello2");


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
    public static class TestYAPIONAnyType {

        private final YAPIONObject yapionObject = new YAPIONObject().add("Test", "Test");
        private final YAPIONMap yapionMap = new YAPIONMap().add("Test", "Test");
        private final YAPIONArray yapionArray = new YAPIONArray().add("Test");
    }

    @YAPIONSave(context = HelloView.class)
    @YAPIONSave(context = HelloWorldView.class)
    public static class TestMultiContextAnnotation {

        private final int i = 10;
    }

    public static class EmptyView implements View {
    }

    public static class HelloView implements View {
    }

    public static class HelloWorldView implements View {
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

        System.out.println(yapionObject1);
        System.out.println(yapionObject1.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parse(yapionObject1.toJSONLossy(new StringOutput()).getResult()));
        System.out.println(encodeBase64(yapionObject1.toString()));
        System.out.println(encodeBase64(yapionObject1.toJSONLossy(new StringOutput()).getResult()));
        System.out.println();
        System.out.println(yapionObject2);
        System.out.println(yapionObject2.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parse(yapionObject2.toJSONLossy(new StringOutput()).getResult()));
        System.out.println(encodeBase64(yapionObject2.toString()));
        System.out.println(encodeBase64(yapionObject2.toJSONLossy(new StringOutput()).getResult()));
        System.out.println();
        System.out.println(yapionObject3);
        System.out.println(yapionObject3.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parse(yapionObject3.toJSONLossy(new StringOutput()).getResult()));
        System.out.println(encodeBase64(yapionObject3.toString()));
        System.out.println(encodeBase64(yapionObject3.toJSONLossy(new StringOutput()).getResult()));
        System.out.println();
        System.out.println(yapionObject4);
        System.out.println(yapionObject4.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parse(yapionObject4.toJSONLossy(new StringOutput()).getResult()));
        System.out.println(encodeBase64(yapionObject4.toString()));
        System.out.println(encodeBase64(yapionObject4.toJSONLossy(new StringOutput()).getResult()));
        System.out.println();
        System.out.println(yapionObject5);
        System.out.println(yapionObject5.toJSONLossy(new StringOutput()).getResult());
        System.out.println(YAPIONParser.parse(yapionObject5.toJSONLossy(new StringOutput()).getResult()));
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
