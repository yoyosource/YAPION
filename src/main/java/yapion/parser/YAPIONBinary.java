package yapion.parser;

import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.SerializeManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class YAPIONBinary {

    private static Map<String, String> encodingTable = new HashMap<>();

    static {
        addEncoding(0x02, SerializeManager.TYPE_IDENTIFIER);
        addEncoding(0x03, SerializeManager.ENUM_IDENTIFIER);
        addEncoding(0x04, JSONMapper.MAP_IDENTIFIER);
        addEncoding(0x05, JSONMapper.POINTER_IDENTIFIER);
        addEncoding(0x06, "java.security.");
        addEncoding(0x07, "java.lang.");
        addEncoding(0x08, "java.io.");
        addEncoding(0x0B, "java.util.");
        addEncoding(0x0C, "yapion.packet.YAPIONPointer");
        addEncoding(0x0E, "yapion.");
        addEncoding(0x0F, "null");
        addEncoding(0x10, "true");
        addEncoding(0x11, "false");
        addEncoding(0x12, "yapion.hierarchy.YAPIONAny");
        addEncoding(0x13, "yapion.hierarchy.YAPIONArray");
        addEncoding(0x14, "yapion.hierarchy.YAPIONMap");
        addEncoding(0x15, "yapion.hierarchy.YAPIONObject");
        addEncoding(0x16, "yapion.hierarchy.YAPIONPointer");
        addEncoding(0x17, "yapion.hierarchy.YAPIONValue");
        addEncoding(0x18, "yapion.hierarchy.YAPIONVariable");
    }

    private static void addEncoding(int value, String key) {
        if (encodingTable.size() > 22) {
            return;
        }
        if (value == 0x0A || value == 0x0D) return;
        if (value > 0x00 && value < 0x1F) {
            encodingTable.putIfAbsent(key, (char)value + "");
        }
    }

    private String toEncode;
    private StringBuilder output = new StringBuilder();

    private YAPIONBinary(String s) {
        this.toEncode = s;
    }

    private String createTriplet(int offset, int length, String d) {
        if (offset < length) return d;
        if (offset > 1 << DefaultValues.slidingWindow) return d;
        if (length <= 3) return d;
        if (length > 1 << DefaultValues.dataWindow) return d;

        byte[] bytes = new byte[4];
        bytes[0] = (byte) 0x1F;
        bytes[1] = (byte) (offset >> 8);
        bytes[2] = (byte) offset;
        bytes[3] = (byte) length;
        return new String(bytes);
    }

    private YAPIONBinary encode() {
        for (Map.Entry<String, String> e : encodingTable.entrySet()) {
            toEncode = toEncode.replace(e.getKey(), e.getValue());
        }

        SlidingWindow slidingWindow = new SlidingWindow();
        int i = 0;
        StringBuilder st = new StringBuilder();

        int realLength = toEncode.length();
        while (i < toEncode.length()) {
            if (slidingWindow.contains(st.toString()) != -1) {
                st.append(toEncode.charAt(i));
            } else if (st.length() > 4) {
                st.deleteCharAt(st.length() - 1);
                String triplet = createTriplet(slidingWindow.contains(st.toString()), st.length(), st.toString());
                realLength -= st.length() - triplet.length();
                System.out.println(slidingWindow.contains(st.toString()) + "/" + st.length() + "/" + i + " -> " + triplet);
                output.append(triplet);
                output.append(toEncode.charAt(i - 1));
                output.append(toEncode.charAt(i));
                st = new StringBuilder();
            } else {
                output.append(st);
                output.append(toEncode.charAt(i));
                st = new StringBuilder();
            }

            if (st.length() > 1 << DefaultValues.dataWindow) {
                output.append(st);
                output.append(toEncode.charAt(i));
                st = new StringBuilder();
            }

            slidingWindow.add(toEncode.charAt(i));
            i++;
        }
        if (st.length() > 0) {
            output.append(st);
        }

        System.out.println(output.toString());
        System.out.println(toEncode.length() + "/" + realLength);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("test.yapionc"));
            fileOutputStream.write(output.toString().getBytes(StandardCharsets.US_ASCII));
        } catch (IOException e) {

        }

        return this;
    }

    private StringBuilder parse(StringBuilder st) {
        output.append(st);
        return new StringBuilder();
    }

    private YAPIONBinary decode() {
        for (Map.Entry<String, String> e :encodingTable.entrySet()) {
            toEncode = toEncode.replace(e.getValue(), e.getKey());
        }
        return this;
    }

    private String result() {
        return toEncode;
    }

    static String toBinary(YAPIONObject yapionObject) {
        return new YAPIONBinary(yapionObject.toYAPIONString()).encode().result();
    }

    static YAPIONObject toYAPION(String binary) {
        return YAPIONParser.parse(new YAPIONBinary(binary).decode().result());
    }

}

class DefaultValues {

    // 9/7
    static int slidingWindow = 16;
    static int dataWindow = 8;

}

class SlidingWindow {

    private int length = 1 << DefaultValues.slidingWindow;
    private char[] chars = new char[length];
    private int index = 0;

    public void add(char c) {
        chars[index] = c;
        index = (index + 1) % length;
    }

    public void add(String s) {
        for (int i = 0; i < s.length(); i++) {
            add(s.charAt(i));
        }
    }

    public int contains(String s) {
        int containsIndex = (index + 1) % length;
        int cIndex = 0;
        while (containsIndex != index - s.length()) {
            if (cIndex >= s.length()) {
                int backOffset = 0;
                while (containsIndex != index) {
                    backOffset++;
                    containsIndex = (containsIndex + 1) % length;
                }
                return backOffset;
            }
            if (chars[containsIndex] == s.charAt(cIndex)) {
                cIndex++;
            } else {
                containsIndex -= cIndex;
                cIndex = 0;
                if (containsIndex < 0) {
                    containsIndex = length - Math.abs(containsIndex);
                }
            }
            containsIndex = (containsIndex + 1) % length;
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        int containsIndex = (index + 1) % length;
        while (containsIndex != index) {
            st.append(chars[containsIndex]);
            containsIndex = (containsIndex + 1) % length;
        }
        return "SlidingWindow{" + st.toString() + "}";
    }
}
