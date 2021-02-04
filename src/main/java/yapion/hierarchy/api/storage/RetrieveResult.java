package yapion.hierarchy.api.storage;

import lombok.NonNull;
import yapion.hierarchy.api.groups.YAPIONAnyType;
import yapion.hierarchy.types.*;

public class RetrieveResult {

    private YAPIONAnyType[] yapionAnyTypes;

    RetrieveResult(YAPIONAnyType... yapionAnyTypes) {
        this.yapionAnyTypes = yapionAnyTypes;
    }

    public int size() {
        return yapionAnyTypes.length;
    }

    public int length() {
        return yapionAnyTypes.length;
    }

    public YAPIONType getType(int index) {
        return yapionAnyTypes[index].getType();
    }

    public YAPIONAnyType getRaw(int index) {
        return yapionAnyTypes[index];
    }

    @SuppressWarnings({"unchecked"})
    public <T extends YAPIONAnyType> T get(int index) {
        return (T) yapionAnyTypes[index];
    }

    @SuppressWarnings({"unchecked"})
    public <T extends YAPIONAnyType> T get(int index, T t) {
        return (T) yapionAnyTypes[index];
    }

    @SuppressWarnings({"unchecked"})
    public <T extends YAPIONAnyType> T get(int index, Class<T> clazz) {
        return (T) yapionAnyTypes[index];
    }

    public YAPIONObject getObject(int index) {
        return get(index);
    }

    public YAPIONArray getArray(int index) {
        return get(index);
    }

    public YAPIONMap getMap(int index) {
        return get(index);
    }

    public YAPIONPointer getPointer(int index) {
        return get(index);
    }

    public <T> YAPIONValue<T> getValue(int index) {
        return get(index);
    }

    @SuppressWarnings({"unchecked"})
    public <T> YAPIONValue<T> getValue(int index, @NonNull T t) {
        return (YAPIONValue<T>) getValue(index);
    }

    @SuppressWarnings({"unchecked"})
    public <T> YAPIONValue<T> getValue(int index, @NonNull Class<T> t) {
        return (YAPIONValue<T>) getValue(index);
    }

}
