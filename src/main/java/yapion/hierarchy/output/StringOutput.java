package yapion.hierarchy.output;

public class StringOutput extends StringBuilderOutput {

    public StringOutput() {
        super(new StringBuilder());
    }

    @Override
    protected void internalConsume(String s) {
        super.internalConsume(s);
    }

    public String getResult() {
        return super.st.toString();
    }

}
