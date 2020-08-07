package yapion.serializing.serializer.number;

import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;

import java.math.BigDecimal;

public class BigDecimalSerializer implements Serializer<BigDecimal> {

    @Override
    public String type() {
        return "java.math.BigDecimal";
    }

    @Override
    public YAPIONAny serialize(BigDecimal object) {
        return new YAPIONValue<>(object);
    }
}
