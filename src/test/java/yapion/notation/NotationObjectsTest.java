package yapion.notation;

import org.junit.Test;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONArray;
import yapion.hierarchy.types.YAPIONMap;
import yapion.hierarchy.types.YAPIONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
// import static yapion.annotation.AnnotationTestObjects.*;

public class NotationObjectsTest {

    @Test
    public void testObjectYAPION() {
        assertThat(new YAPIONObject().toYAPION(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testObjectJSON() {
        assertThat(new YAPIONObject().toJSONLossy(new StringOutput()).getResult(), is("{}"));
    }

    @Test
    public void testMapYAPION() {
        assertThat(new YAPIONMap().toYAPION(new StringOutput()).getResult(), is("<>"));
    }

    @Test
    public void testMapJSON() {
        assertThat(new YAPIONMap().toJSONLossy(new StringOutput()).getResult(), is("{\"@mapping\":[]}"));
    }

    @Test
    public void testArrayYAPION() {
        assertThat(new YAPIONArray().toYAPION(new StringOutput()).getResult(), is("[]"));
    }

    @Test
    public void testArrayJSON() {
        assertThat(new YAPIONArray().toJSONLossy(new StringOutput()).getResult(), is("[]"));
    }

}
