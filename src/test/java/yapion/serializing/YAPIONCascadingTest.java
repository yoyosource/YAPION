package yapion.serializing;

import org.junit.Test;
import yapion.exceptions.serializing.YAPIONSerializerException;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.YAPIONTestObjects.Cascading;
import yapion.serializing.YAPIONTestObjects.NonCascading;
import yapion.serializing.YAPIONTestObjects.NonSaved;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

// TODO: add more tests
public class YAPIONCascadingTest {

    @Test(expected = YAPIONSerializerException.class)
    public void testNonSavedInNonCascading() {
        NonCascading nonCascading = new NonCascading(new NonSaved(null));
        YAPIONSerializer.serialize(nonCascading, "empty");
    }

    @Test
    public void testNonCascadingInCascading() {
        Cascading cascading = new Cascading(new NonSaved(null));
        YAPIONObject yapionObject = YAPIONSerializer.serialize(cascading, "empty");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{@type(yapion.serializing.YAPIONTestObjects$Cascading)cnc1{@type(yapion.serializing.YAPIONTestObjects$NonSaved)cnc1(null)cnc2(null)}cnc2(null)}"));
    }

    @Test
    public void testNotUpCascading() {
        NonCascading nonCascading = new NonCascading(new Cascading(null), new NonSaved(null));
        YAPIONObject yapionObject = YAPIONSerializer.serialize(nonCascading, "empty");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{@type(yapion.serializing.YAPIONTestObjects$NonCascading)cnc1{@type(yapion.serializing.YAPIONTestObjects$Cascading)cnc1(null)cnc2(null)}}"));
    }

    @Test
    public void testNotUpCascadingOther() {
        NonCascading nonCascading = new NonCascading(new Cascading(null), new NonSaved(null));
        YAPIONObject yapionObject = YAPIONSerializer.serialize(nonCascading);
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{@type(yapion.serializing.YAPIONTestObjects$NonCascading)cnc1{@type(yapion.serializing.YAPIONTestObjects$Cascading)cnc1(null)cnc2(null)}}"));
    }

    @Test
    public void testNonSavedInNonSavedInCascading() {
        Cascading cascading = new Cascading(new NonSaved(new NonSaved(null)));
        YAPIONObject yapionObject = YAPIONSerializer.serialize(cascading, "empty");
        assertThat(yapionObject.toYAPION(new StringOutput()).getResult(), is("{@type(yapion.serializing.YAPIONTestObjects$Cascading)cnc1{@type(yapion.serializing.YAPIONTestObjects$NonSaved)cnc1{@type(yapion.serializing.YAPIONTestObjects$NonSaved)cnc1(null)cnc2(null)}cnc2(null)}cnc2(null)}"));
    }

}
