package yapion.hierarchy.output;

import yapion.exceptions.YAPIONException;

public class StringOutput extends AbstractOutput {

    private final StringBuilder st = new StringBuilder();

    @Override
    public AbstractOutput consume(String s) {
        validCall();
        st.append(s);
        return this;
    }

    public String getResult() {
        return st.toString();
    }

    public int length() {
        return st.length();
    }

}
