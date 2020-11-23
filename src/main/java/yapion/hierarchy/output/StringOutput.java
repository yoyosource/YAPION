package yapion.hierarchy.output;

public class StringOutput extends AbstractOutput {

    private final StringBuilder st = new StringBuilder();

    @Override
    protected void internalConsume(String s) {
        st.append(s);
    }

    public String getResult() {
        return st.toString();
    }

    public int length() {
        return st.length();
    }

}
