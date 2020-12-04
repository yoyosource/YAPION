package yapion.serializing.serializer.object.security;

import sun.security.provider.DSAPublicKeyImpl;
import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.typegroups.YAPIONAnyType;
import yapion.hierarchy.types.YAPIONObject;
import yapion.serializing.InternalSerializer;
import yapion.serializing.data.DeserializeData;
import yapion.serializing.data.SerializeData;
import yapion.serializing.serializer.SerializerImplementation;

import java.security.InvalidKeyException;
import java.util.Base64;

import static yapion.utils.IdentifierUtils.TYPE_IDENTIFIER;

@YAPIONSaveExclude
@YAPIONLoadExclude
@SerializerImplementation(since = "0.20.1")
public class DSAPublicKeySerializer implements InternalSerializer<DSAPublicKeyImpl> {

    @Override
    public String type() {
        return "sun.security.provider.DSAPublicKeyImpl";
    }

    @Override
    public YAPIONAnyType serialize(SerializeData<DSAPublicKeyImpl> serializeData) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(TYPE_IDENTIFIER, type());

        byte[] encoded = serializeData.object.getEncoded();
        String encodedString;
        if (encoded == null) {
            encodedString = null;
        } else {
            encodedString = Base64.getEncoder().encodeToString(serializeData.object.getEncoded());
        }
        yapionObject.add("encoded", encodedString);

        return yapionObject;
    }

    @Override
    public DSAPublicKeyImpl deserialize(DeserializeData<? extends YAPIONAnyType> deserializeData) {
        YAPIONObject yapionObject = (YAPIONObject) deserializeData.object;

        String encodedString = yapionObject.getValue("encoded", "").get();
        byte[] encoded;
        if (encodedString == null) {
            throw new YAPIONException("'encoded' was null");
        } else {
            encoded = Base64.getDecoder().decode(encodedString);
        }

        try {
            return new DSAPublicKeyImpl(encoded);
        } catch (InvalidKeyException e) {
            throw new YAPIONException(e.getMessage(), e.getCause());
        }
    }
}
