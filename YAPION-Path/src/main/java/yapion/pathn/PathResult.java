/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.pathn;

import lombok.RequiredArgsConstructor;
import yapion.hierarchy.api.groups.YAPIONAnyType;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@RequiredArgsConstructor
public class PathResult implements List<YAPIONAnyType> {

    public final List<YAPIONAnyType> yapionAnyTypeList;
    public final long nanoTime;

    @Override
    public int size() {
        return yapionAnyTypeList.size();
    }

    @Override
    public boolean isEmpty() {
        return yapionAnyTypeList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return yapionAnyTypeList.contains(o);
    }

    @Override
    public Iterator<YAPIONAnyType> iterator() {
        return yapionAnyTypeList.iterator();
    }

    @Override
    public Object[] toArray() {
        return yapionAnyTypeList.toArray(new YAPIONAnyType[0]);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return yapionAnyTypeList.toArray(a);
    }

    @Override
    public boolean add(YAPIONAnyType yapionAnyType) {
        return yapionAnyTypeList.add(yapionAnyType);
    }

    @Override
    public boolean remove(Object o) {
        return yapionAnyTypeList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return yapionAnyTypeList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends YAPIONAnyType> c) {
        return yapionAnyTypeList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends YAPIONAnyType> c) {
        return yapionAnyTypeList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return yapionAnyTypeList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return yapionAnyTypeList.retainAll(c);
    }

    @Override
    public void clear() {
        yapionAnyTypeList.clear();
    }

    @Override
    public YAPIONAnyType get(int index) {
        return yapionAnyTypeList.get(index);
    }

    @Override
    public YAPIONAnyType set(int index, YAPIONAnyType element) {
        return yapionAnyTypeList.set(index, element);
    }

    @Override
    public void add(int index, YAPIONAnyType element) {
        yapionAnyTypeList.add(index, element);
    }

    @Override
    public YAPIONAnyType remove(int index) {
        return yapionAnyTypeList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return yapionAnyTypeList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return yapionAnyTypeList.lastIndexOf(o);
    }

    @Override
    public ListIterator<YAPIONAnyType> listIterator() {
        return yapionAnyTypeList.listIterator();
    }

    @Override
    public ListIterator<YAPIONAnyType> listIterator(int index) {
        return yapionAnyTypeList.listIterator(index);
    }

    @Override
    public List<YAPIONAnyType> subList(int fromIndex, int toIndex) {
        return yapionAnyTypeList.subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
        return "PathResult{" +
                "nanoTime=" + nanoTime + "ns" +
                ", milliseconds=" + (nanoTime / 1000000) + "ms" +
                ", count=" + yapionAnyTypeList.size() +
                ", elements=" + yapionAnyTypeList +
                '}';
    }
}
