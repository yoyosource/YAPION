package yapion.hierarchy.types.value;

import java.util.Optional;

import static yapion.utils.ReferenceIDUtils.calc;

public class BooleanHandler implements ValueHandler<Boolean> {

    @Override
    public String output(Boolean aBoolean) {
        if (aBoolean) {
            return "true";
        } else {
            return "false";
        }
    }

    @Override
    public Optional<Boolean> preParse(String s) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> parse(String s) {
        if (s.equals("true")) {
            return Optional.of(true);
        }
        if (s.equals("false")) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    @Override
    public long referenceValue() {
        return calc("java.lang.Boolean");
    }

}
