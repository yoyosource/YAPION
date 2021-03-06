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

package yapion.parser;

import org.junit.Test;
import yapion.exceptions.parser.YAPIONParserException;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static yapion.YAPIONAssertion.isYAPION;

public class YAPIONParserTest {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONParser.parse("1(#A000B)2(#a000B)");
        System.out.println(yapionObject);
    }

    @Test
    public void testParser() throws Exception {
        File file = new File(YAPIONParserTest.class.getResource("/parser").toURI());
        List<File> fileList = Arrays.asList(file.listFiles());
        fileList.sort(Comparator.comparing(File::getName));

        List<TestFile> testFiles = new ArrayList<>();
        fileList.forEach(f -> {
            try {
                testFiles.add(new TestFile(f));
            } catch (Exception e) {
                e.printStackTrace();
                assertThat("Exception in Initializer", false);
            }
        });
        testFiles.forEach(TestFile::test);
        testFiles.forEach(testFile -> testFile.output(true));
        if (testFiles.stream().anyMatch(testFile -> testFile.failed)) {
            assertThat("Tests failed", false);
        }
    }

    private static class TestFile {

        private File file;
        private List<TestCase> testCases = new ArrayList<>();
        private boolean failed = false;

        public TestFile(File file) throws Exception {
            this.file = file;

            List<String> strings = new BufferedReader(new InputStreamReader(new FileInputStream(file))).lines().collect(Collectors.toList());

            TestCase testCase = null;

            boolean inBlock = false;
            String blockType = "";
            StringBuilder st = new StringBuilder();

            for (String s : strings) {
                if (inBlock) {
                    if (s.equals("```")) {
                        if (blockType.contains("I")) {
                            testCase.input = st.toString();
                            if (blockType.contains("stream")) {
                                testCase.inputStream = true;
                            }
                            if (blockType.contains("chars")) {
                                testCase.chars = true;
                            }
                            if (blockType.contains("bytes")) {
                                testCase.bytes = true;
                            }
                            if (blockType.contains("type:")) {
                                if (blockType.contains("type:ascii")) {
                                    testCase.charsets = InputStreamCharsets.US_ASCII;
                                } else if (blockType.contains("type:utf8")) {
                                    testCase.charsets = InputStreamCharsets.UTF_8;
                                } else if (blockType.contains("type:latin1")) {
                                    testCase.charsets = InputStreamCharsets.LATIN_1;
                                }
                            }
                        } else if (blockType.contains("O")) {
                            testCase.output = st.toString();
                            if (blockType.contains("prettified")) {
                                testCase.prettified = true;
                            }
                        } else if (blockType.contains("E")) {
                            testCase.exception = Class.forName(st.toString());
                        }
                        st = new StringBuilder();
                        inBlock = false;
                        continue;
                    }
                    if (st.length() != 0) st.append("\n");
                    st.append(s);
                } else {
                    if (s.startsWith("```") && testCase != null) {
                        blockType = s;
                        inBlock = true;
                    }
                    if (s.startsWith("# ")) {
                        if (testCase != null && testCase.input != null && (testCase.output != null ^ testCase.exception != null)) {
                            testCases.add(testCase);
                        }
                        testCase = new TestCase(s.substring(2));
                    }
                }
            }
            if (testCase != null && testCase.input != null && (testCase.output != null ^ testCase.exception != null)) {
                testCases.add(testCase);
            }
        }

        public TestFile test() {
            testCases.forEach(testCase -> {
                failed |= !testCase.test().passed;
            });
            return this;
        }

        public void output(boolean onlyFailed) {
            if (!failed && onlyFailed) {
                return;
            }
            System.out.println("=== " + file.getName() + " ===");
            testCases.forEach(testCase -> {
                if (!testCase.passed || !onlyFailed) {
                    testCase.output();
                }
            });
        }
    }

    private static class TestCase {

        private String name;

        private String input;
        private String output;
        private Class<?> exception;

        private long time = 0;
        private boolean passed = false;
        private String actualOutput = null;
        private List<String> unstableOutput = new ArrayList<>();
        private Exception actualException = null;

        private InputStreamCharsets charsets = InputStreamCharsets.US_ASCII;
        private boolean inputStream = false;
        private boolean chars = false;
        private boolean bytes = false;

        private boolean prettified = false;

        private StringBuilder logOutput = new StringBuilder();

        public TestCase(String name) {
            this.name = name;
        }

        public TestCase test() {
            PrintStream printStream = System.out;
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    logOutput.append((char) b);
                }
            }));

            time = System.currentTimeMillis();
            try {
                if (inputStream) {
                    actualOutput = YAPIONParser.parse(new ByteArrayInputStream(input.getBytes()), charsets).toYAPION(prettified);
                } else {
                    if (chars) {
                        actualOutput = new YAPIONParser(input.toCharArray()).parse().result().toYAPION(prettified);
                    } else if (bytes) {
                        actualOutput = new YAPIONParser(input.getBytes()).parse().result().toYAPION(prettified);
                    } else {
                        actualOutput = YAPIONParser.parse(input).toYAPION(prettified);
                    }
                }
                if (output != null && output.equals(actualOutput)) passed = true;
            } catch (Exception e) {
                if (exception != null && e.getClass() == exception) passed = true;
                actualException = e;
            }
            time = System.currentTimeMillis() - time;

            if (output != null) {
                System.setOut(new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {

                    }
                }));

                do {
                    String testOutput = YAPIONParser.parse(actualOutput).toYAPION(prettified);
                    if (!testOutput.equals(unstableOutput.isEmpty() ? output : unstableOutput.get(unstableOutput.size() - 1))) {
                        unstableOutput.add(testOutput);
                        passed = false;
                    } else {
                        break;
                    }
                } while (true);
            }

            System.setOut(printStream);
            return this;
        }

        public void output() {
            StringBuilder st = new StringBuilder();
            st.append(name).append(" ");
            while (st.length() < 50) st.append(" ");
            st.append(time).append("ms ");
            while (st.length() < 60) st.append(" ");
            st.append(passed ? "passed" : (!unstableOutput.isEmpty() ? "unstable": "failed"));

            if (!passed) {
                st.append("\n").append("  Expected: ");
                st.append(exception != null ? exception : output);
                st.append("\n").append("  Got     : ");
                st.append(actualException != null ? actualException : actualOutput);
                for (String s : unstableOutput) {
                    st.append("\n").append("  Got     : ").append(s);
                }
                st.append("\n");
                st.append(logOutput);
                st.append("\n");
            }
            System.out.println(st);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullSafety() {
        YAPIONParser.parse((InputStream) null);
    }

    @Test(expected = YAPIONParserException.class)
    public void testStringError() {
        YAPIONParser.parse("{{}");
    }

    @Test(expected = YAPIONParserException.class)
    public void testTypeMismatch() {
        YAPIONParser.parse("{]");
    }

    @Test
    public void testInitialType() {
        assertThat(YAPIONParser.parse("[]"), isYAPION("{[]}"));
    }

    @Test
    public void testEmptyString() {
        assertThat(YAPIONParser.parse(""), isYAPION("{}"));
    }

    @Test
    public void testCharArray() {
        char[] chars = "{a()b{}c[]d<>}".toCharArray();
        YAPIONObject yapionObject = new YAPIONParser(chars).parse().result();
        assertThat(yapionObject, isYAPION("{a()b{}c[]d<>}"));
    }

    @Test
    public void testByteArray() {
        byte[] bytes = "{a()b{}c[]d<>}".getBytes();
        YAPIONObject yapionObject = new YAPIONParser(bytes).parse().result();
        assertThat(yapionObject, isYAPION("{a()b{}c[]d<>}"));
    }

    @Test
    public void testEmpty() {
        YAPIONObject yapionObject = YAPIONParser.parse("{}");
        assertThat(yapionObject, isYAPION("{}"));
    }

    @Test
    public void testVariable() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a()b(\"Hello\")c(true)d(\"true\")}");
        assertThat(yapionObject, isYAPION("{a()b(Hello)c(true)d(\"true\")}"));
    }

    @Test
    public void testBlank() {
        YAPIONObject yapionObject = YAPIONParser.parse("{   a()\\   b()()}");
        assertThat(yapionObject, isYAPION("{a()\\   b()()}"));
    }

    @Test
    public void testArray() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a[]b[[null,null],[],[]]}");
        assertThat(yapionObject, isYAPION("{a[]b[[null,null],[],[]]}"));
    }

    @Test
    public void testMap() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a<>}");
        assertThat(yapionObject, isYAPION("{a<>}"));
    }

    @Test
    public void testPointer() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a->0000000000000000}");
        assertThat(yapionObject, isYAPION("{a->0000000000000000}"));
    }

    @Test
    public void testBackslash() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a(\\\\)}");
        assertThat(yapionObject, isYAPION("{a(\\\\)}"));
    }

    @Test
    public void testUnicode() {
        YAPIONObject yapionObject = YAPIONParser.parse("{a(\\u0020)}");
        assertThat(yapionObject, isYAPION("{a( )}"));
    }

    @Test
    public void testUnicodeKey() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\u0020(a)}");
        assertThat(yapionObject, isYAPION("{\\ (a)}"));
    }

    @Test
    public void testUnicodeKeyReparse() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("\u0000", "a");
        assertThat(yapionObject, isYAPION("{\\u0000(a)}"));
        yapionObject = YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult());
        assertThat(yapionObject, isYAPION("{\\u0000(a)}"));
    }

    @Test
    public void testCharacters() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\ !\"#$%&'\\(\\)*+,-./0123456789:;\\<=\\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[\\\\]^_`abcdefghijklmnopqrstuvwxyz\\{|\\}~( !\"#$%&'\\(\\)*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}");
        assertThat(yapionObject, isYAPION("{\\ !\"#$%&'\\(\\)*+,-./0123456789:;\\<=\\>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[\\\\]^_`abcdefghijklmnopqrstuvwxyz\\{|\\}~( !\"#$%&'\\(\\)*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~)}"));
    }

    @Test
    public void testStringEscape() {
        YAPIONObject yapionObject = YAPIONParser.parse("{(\"\"\")}");
        assertThat(yapionObject.getPlainValue(""), is("\""));
        assertThat(yapionObject, isYAPION("{(\"\"\")}"));
    }

    @Test
    public void testStringStartsAndEndsWith() {
        YAPIONObject yapionObject = YAPIONParser.parse("{(\"\"\"\")}");
        assertThat(yapionObject.getPlainValue(""), is("\"\""));
        assertThat(yapionObject, isYAPION("{(\"\"\"\")}"));
    }

    @Test
    public void testStringStartsWith() {
        YAPIONObject yapionObject = YAPIONParser.parse("{(\"\"Hello world\")}");
        assertThat(yapionObject.getPlainValue(""), is("\"Hello world"));
        assertThat(yapionObject, isYAPION("{(\"\"Hello world\")}"));
    }

    @Test
    public void testStringEndsWith() {
        YAPIONObject yapionObject = YAPIONParser.parse("{(\"Hello world\"\")}");
        assertThat(yapionObject.getPlainValue(""), is("Hello world\""));
        assertThat(yapionObject, isYAPION("{(\"Hello world\"\")}"));
    }

    @Test
    public void testArrayStringWithPointer() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[a->b,b->c]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(0), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(1), instanceOf(YAPIONValue.class));
        assertThat(yapionObject, isYAPION("{[a->b,b->c]}"));
    }

    @Test
    public void testArrayWithAnyType() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[{},[],(),->0000000000000000,<>]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(0), instanceOf(YAPIONObject.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(1), instanceOf(YAPIONArray.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(2), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(3), instanceOf(YAPIONPointer.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(4), instanceOf(YAPIONMap.class));
        assertThat(yapionObject, isYAPION("{[{},[],(),->0000000000000000,<>]}"));
    }

    @Test
    public void testFileInputStream() {
        YAPIONParser.parse(YAPIONParserTest.class.getResourceAsStream("/test.yapion"));
    }

    @Test
    public void testYAPIONStartEnd() {
        assertThat(YAPIONParser.parse("hello()").toYAPION(new StringOutput()).getResult(), is("{hello()}"));
    }

    @Test
    public void testYAPIONStartEndObject() {
        assertThat(YAPIONParser.parse("hello{}").toYAPION(new StringOutput()).getResult(), is("{hello{}}"));
    }

    @Test(expected = YAPIONParserException.class)
    public void testYAPIONStartEndObjectError() {
        YAPIONParser.parse("hello{}}");
    }

    @Test
    public void testArraySeparatorSupport() {
        YAPIONObject yapionObject = new YAPIONObject();
        YAPIONArray yapionArray = new YAPIONArray();
        yapionObject.add("", yapionArray);
        yapionArray.add("Hello, World");
        assertThat(yapionObject, is(YAPIONParser.parse(yapionObject.toString())));
    }

    @Test
    public void testArraySupportWithoutSeparator() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[{}{}]}");
        assertThat(yapionObject, isYAPION("{[{},{}]}"));
    }

    @Test
    public void testArrayStringWithoutSeparator() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[{}{}HelloWorld,->0000000000000000]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(2), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.getArray("").getYAPIONAnyType(3), instanceOf(YAPIONPointer.class));
        assertThat(yapionObject.toString(), is("{[{},{},HelloWorld,->0000000000000000]}"));
    }

    @Test
    public void testArrayStringPointerStartInValue() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[\\->0000000000000000]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(0), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.toString(), is("{[\\->0000000000000000]}"));
    }

    @Test
    public void testArrayWithStringValue() {
        YAPIONObject yapionObject = new YAPIONObject().add("", new YAPIONArray().add(new YAPIONValue<>("{Hello World}")));
        yapionObject = YAPIONParser.parse(yapionObject.toYAPION(new StringOutput()).getResult());
        assertThat(yapionObject.getArray("").getValue(0), notNullValue());
        assertThat(yapionObject.getArray("").getValue(0), isYAPION(new YAPIONValue<>("{Hello World}")));
    }

    @Test
    public void testArrayStringWhitespaceInValue() {
        YAPIONObject yapionObject = YAPIONParser.parse("{[ Hugo Hello World]}");
        assertThat(yapionObject.getArray("").getYAPIONAnyType(0), instanceOf(YAPIONValue.class));
        assertThat(yapionObject.getArray("").getValue(0).get(), instanceOf(String.class));
        assertThat(yapionObject.getArray("").getValue(0).get(), is("Hugo Hello World"));
    }

    @Test
    public void testNewLine() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\r   n()}");
        assertThat(yapionObject.getPlainValue("n"), notNullValue());

        yapionObject = YAPIONParser.parse("{[\r   Hello World]}");
        assertThat(yapionObject.getArray("").getValue(0).get(), is("Hello World"));
    }

    @Test
    public void testEscapedNewLine() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\\r   n()}");
        assertThat(yapionObject.getPlainValue("\r   n"), notNullValue());

        yapionObject = YAPIONParser.parse("{[\\\r   Hello World]}");
        assertThat(yapionObject.getArray("").getValue(0).get(), is("\\\r   Hello World"));
    }

    @Test
    public void testNormalEscape() {
        YAPIONObject yapionObject = YAPIONParser.parse("{\\r\\n\\t()}");
        assertThat(yapionObject.getPlainValue("\r\n\t"), notNullValue());
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{\\r\\n\\t()}"));
    }

    @Test
    public void testComaKey() {
        assertThat(YAPIONParser.parse("{\\,hello()}").toYAPION(new StringOutput()).getResult(), is("{\\,hello()}"));
    }

    @Test
    public void testComaKeyAndSpace() {
        assertThat(YAPIONParser.parse("{\\, hello()}").toYAPION(new StringOutput()).getResult(), is("{\\, hello()}"));
    }

    @Test
    public void testComaSeparator() {
        assertThat(YAPIONParser.parse("{,hello()}").toYAPION(new StringOutput()).getResult(), is("{hello()}"));
    }

    @Test
    public void testComaSeparatorAndSpace() {
        assertThat(YAPIONParser.parse("{, hello()}").toYAPION(new StringOutput()).getResult(), is("{hello()}"));
    }

    @Test
    public void testMapPointer() {
        YAPIONParser.parse("{<():->0000000000000000>}");
    }

    @Test
    public void testENotation() {
        assertThat(YAPIONParser.parse("{(5E+5)}").toYAPION(new StringOutput()).getResult(), is("{(500000.0)}"));
    }

    @Test
    public void testParsingOfUTF8File() {
        YAPIONObject yapionObject = new YAPIONParser(YAPIONParserTest.class.getResourceAsStream("/test2.yapion"), InputStreamCharsets.UTF_8).parse().result();
        yapionObject = YAPIONParser.parse(yapionObject.toYAPION());
        assertThat(yapionObject.toYAPION(), is("{rosenduftgenießer(null)}"));
    }

}
