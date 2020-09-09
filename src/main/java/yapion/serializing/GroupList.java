package yapion.serializing;

import yapion.annotations.deserialize.YAPIONLoadExclude;
import yapion.annotations.serialize.YAPIONSaveExclude;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@YAPIONSaveExclude(context = "*")
@YAPIONLoadExclude(context = "*")
class GroupList extends ArrayList<InternalSerializerGroup> {

    private List<String> stringStream;
    private boolean builded = false;

    public void build() {
        if (builded) return;
        stringStream = stream().map(InternalSerializerGroup::group).collect(Collectors.toList());
        builded = true;
    }

    public boolean contains(String s) {
        if (!builded) return false;
        if (s == null) return false;
        return stringStream.stream().anyMatch(s::startsWith);
    }


    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InternalSerializerGroup set(int index, InternalSerializerGroup element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(InternalSerializerGroup internalSerializerGroup) {
        if (builded) {
            throw new UnsupportedOperationException();
        }
        return super.add(internalSerializerGroup);
    }

    @Override
    public void add(int index, InternalSerializerGroup element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InternalSerializerGroup remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends InternalSerializerGroup> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends InternalSerializerGroup> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<InternalSerializerGroup> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<InternalSerializerGroup> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<InternalSerializerGroup> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<InternalSerializerGroup> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super InternalSerializerGroup> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<InternalSerializerGroup> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super InternalSerializerGroup> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
