package yapion.serializing.serializer.other;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.hierarchy.YAPIONAny;
import yapion.hierarchy.YAPIONVariable;
import yapion.hierarchy.types.YAPIONObject;
import yapion.hierarchy.types.YAPIONValue;
import yapion.serializing.InternalSerializer;
import yapion.serializing.SerializeManager;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;
import yapion.serializing.serializer.SerializerImplementation;

import java.math.MathContext;
import java.math.RoundingMode;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
@SerializerImplementation
public class MathContextSerializer implements InternalSerializer<MathContext> {

    @Override
    public String type() {
        return "java.math.MathContext";
    }

    @Override
    public YAPIONAny serialize(MathContext object, YAPIONSerializer yapionSerializer) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable(SerializeManager.TYPE_IDENTIFIER, new YAPIONValue<>(type())));
        yapionObject.add(new YAPIONVariable("precision", new YAPIONValue<>(object.getPrecision())));
        yapionObject.add(new YAPIONVariable("roundMode", yapionSerializer.parse(object.getRoundingMode())));
        return yapionObject;
    }

    @Override
    public MathContext deserialize(YAPIONAny yapionAny, YAPIONDeserializer yapionDeserializer) {
        YAPIONObject yapionObject = (YAPIONObject) yapionAny;
        int precision = yapionObject.getValue("precision", 0).get();
        RoundingMode roundingMode = (RoundingMode) yapionDeserializer.parse(yapionObject.getObject("roundMode"));
        return new MathContext(precision, roundingMode);
    }
}
