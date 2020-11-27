package yapion.hierarchy.output;

public class StringBuilderPrettifiedOutput extends StringBuilderOutput {

    public StringBuilderPrettifiedOutput(StringBuilder st) {
        super(st);
    }

    @Override
    protected boolean prettified() {
        return true;
    }

}
