package yapion.serializing.serializer.number;

import yapion.annotations.YAPIONLoadExclude;
import yapion.annotations.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.Serializer;
import yapion.serializing.YAPIONSerializer;

import java.math.BigInteger;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
public class BigIntegerSerializer implements Serializer<BigInteger> {

    @Override
    public String type() {
        return "java.math.BigInteger";
    }

    @Override
    public YAPIONAny serialize(BigInteger object, YAPIONSerializer yapionSerializer) {
        return new YAPIONValue<>(object);
    }

    @Override
    public BigInteger deserialize(YAPIONAny yapionAny) {
        return ((YAPIONValue<BigInteger>) yapionAny).get();
    }
}
