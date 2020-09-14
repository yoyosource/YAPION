package test;

import lombok.ToString;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

@YAPIONLoad(context = "*")
@YAPIONSave(context = "*")
@ToString
public class TestArray {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONSerializer.serialize(new TestArray());
        System.out.println(yapionObject);
        System.out.println(YAPIONParser.mapJSON(YAPIONParser.parseJSON(yapionObject.toJSONString())));
        System.out.println(yapionObject.toJSONString());
        System.out.println(yapionObject.toLossyJSONString());
        Object object = YAPIONDeserializer.deserialize(yapionObject);
        System.out.println(object);
    }

    private int[] ints = new int[1];
    private int[][] intss = new int[1][2];
    private byte[] bytes = new byte[2];
    private byte[][] bytess = new byte[2][3];
    private double[] doubles = new double[3];
    private double[][] doubless = new double[3][4];
    private float[] floats = new float[4];
    private float[][] floatss = new float[4][5];
    private short[] shorts = new short[5];
    private short[][] shortss = new short[5][6];
    private char[] chars = new char[6];
    private char[][] charss = new char[6][7];
    private long[] longs = new long[7];
    private long[][] longss = new long[7][8];
    private boolean[] booleans = new boolean[8];
    private boolean[][] booleanss = new boolean[8][9];

}
