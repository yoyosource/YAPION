package yapion.notation;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
// import static yapion.annotation.AnnotationTestObjects.*;

public class ComplexNotationObjectsTest {

    @Test
    public void testObjectObjectYAPION() {
        assertThat(new YAPIONObject().add("object", new YAPIONObject()).toYAPION(new StringOutput()).getResult(), is("{object{}}"));
    }

    @Test
    public void testObjectObjectJSON() {
        assertThat(new YAPIONObject().add("object", new YAPIONObject()).toJSONLossy(new StringOutput()).getResult(), is("{\"object\":{}}"));
    }

    @Test
    public void testObjectArrayYAPION() {
        assertThat(new YAPIONObject().add("array", new YAPIONArray()).toYAPION(new StringOutput()).getResult(), is("{array[]}"));
    }

    @Test
    public void testObjectArrayJSON() {
        assertThat(new YAPIONObject().add("array", new YAPIONArray()).toJSONLossy(new StringOutput()).getResult(), is("{\"array\":[]}"));
    }

    @Test
    public void testObjectMapYAPION() {
        assertThat(new YAPIONObject().add("map", new YAPIONMap()).toYAPION(new StringOutput()).getResult(), is("{map<>}"));
    }

    @Test
    public void testObjectMapJSON() {
        assertThat(new YAPIONObject().add("map", new YAPIONMap()).toJSONLossy(new StringOutput()).getResult(), is("{\"map\":{\"@mapping\":[]}}"));
    }

    @Test
    public void testArrayObjectYAPION() {
        assertThat(new YAPIONArray().add(new YAPIONObject()).toYAPION(new StringOutput()).getResult(), is("[{}]"));
    }

    @Test
    public void testArrayObjectJSON() {
        assertThat(new YAPIONArray().add(new YAPIONObject()).toJSONLossy(new StringOutput()).getResult(), is("[{}]"));
    }

    @Test
    public void testArrayArrayYAPION() {
        assertThat(new YAPIONArray().add(new YAPIONArray()).toYAPION(new StringOutput()).getResult(), is("[[]]"));
    }

    @Test
    public void testArrayArrayJSON() {
        assertThat(new YAPIONArray().add(new YAPIONArray()).toJSONLossy(new StringOutput()).getResult(), is("[[]]"));
    }

    @Test
    public void testArrayMapYAPION() {
        assertThat(new YAPIONArray().add(new YAPIONMap()).toYAPION(new StringOutput()).getResult(), is("[<>]"));
    }

    @Test
    public void testArrayMapJSON() {
        assertThat(new YAPIONArray().add(new YAPIONMap()).toJSONLossy(new StringOutput()).getResult(), is("[{\"@mapping\":[]}]"));
    }

    @Test
    public void testMapObjectYAPION() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>("object"), new YAPIONObject()).toYAPION(new StringOutput()).getResult(), is("<0:1#0(object)#1{}>"));
    }

    @Test
    public void testMapObjectJSON() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>("object"), new YAPIONObject()).toJSONLossy(new StringOutput()).getResult(), is("{\"@mapping\":[\"0:1\"],\"#0\":\"object\",\"#1\":{}}"));
    }

    @Test
    public void testMapArrayYAPION() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>("array"), new YAPIONArray()).toYAPION(new StringOutput()).getResult(), is("<0:1#0(array)#1[]>"));
    }

    @Test
    public void testMapArrayJSON() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>("array"), new YAPIONArray()).toJSONLossy(new StringOutput()).getResult(), is("{\"@mapping\":[\"0:1\"],\"#0\":\"array\",\"#1\":[]}"));
    }

    @Test
    public void testMapMapYAPION() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>("map"), new YAPIONMap()).toYAPION(new StringOutput()).getResult(), is("<0:1#0(map)#1<>>"));
    }

    @Test
    public void testMapMapJSON() {
        assertThat(new YAPIONMap().add(new YAPIONValue<>("map"), new YAPIONMap()).toJSONLossy(new StringOutput()).getResult(), is("{\"@mapping\":[\"0:1\"],\"#0\":\"map\",\"#1\":{\"@mapping\":[]}}"));
    }

}
