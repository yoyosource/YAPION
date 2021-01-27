// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020,2021 yoyosource

package test;

import lombok.ToString;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;
import yapion.hierarchy.output.StringOutput;
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
        System.out.println(YAPIONParser.mapJSON(YAPIONParser.parseJSON(yapionObject.toJSON(new StringOutput()).getResult())));
        System.out.println(yapionObject.toJSON(new StringOutput()).getResult());
        System.out.println(yapionObject.toJSONLossy(new StringOutput()).getResult());
        Object object = YAPIONDeserializer.deserialize(yapionObject);
        System.out.println(object);
    }

    private final int[] ints = new int[1];
    private final int[][] intss = new int[1][2];
    private final byte[] bytes = new byte[2];
    private final byte[][] bytess = new byte[2][3];
    private final double[] doubles = new double[3];
    private final double[][] doubless = new double[3][4];
    private final float[] floats = new float[4];
    private final float[][] floatss = new float[4][5];
    private final short[] shorts = new short[5];
    private final short[][] shortss = new short[5][6];
    private final char[] chars = new char[6];
    private final char[][] charss = new char[6][7];
    private final long[] longs = new long[7];
    private final long[][] longss = new long[7][8];
    private final boolean[] booleans = new boolean[8];
    private final boolean[][] booleanss = new boolean[8][9];

}