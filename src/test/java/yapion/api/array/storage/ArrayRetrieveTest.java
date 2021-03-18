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

package yapion.api.array.storage;

import org.junit.Test;
import yapion.exceptions.utils.YAPIONArrayIndexOutOfBoundsException;
import yapion.hierarchy.types.*;

import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class ArrayRetrieveTest {

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testNotHasValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.hasValue(0), is(false));
    }

    @Test
    public void testHasValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        assertThat(yapionArray.hasValue(0), is(true));
    }

    @Test
    public void testNotHasValueOfSpecificType() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        assertThat(yapionArray.hasValue(0, YAPIONType.OBJECT), is(false));
    }

    @Test
    public void testHasValueOfSpecificType() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.hasValue(0, YAPIONType.OBJECT), is(true));
    }

    @Test
    public void testHasValueOfClassType() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        assertThat(yapionArray.hasValue(0, String.class), is(true));
    }

    @Test
    public void testGetYAPIONAnyType() {
        YAPIONArray yapionArray = new YAPIONArray();
        YAPIONObject yapionAnyTypes = new YAPIONObject();
        yapionArray.add(yapionAnyTypes);
        assertThat(yapionArray.getYAPIONAnyType(0), is(yapionAnyTypes));
    }

    @Test
    public void testGetObject() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        assertThat(yapionArray.getObject(0), is(new YAPIONObject()));
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetObjectNull() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getObject(0), nullValue());
    }

    @Test
    public void testGetObjectWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        yapionArray.getObject(0, yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONObject()));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetObjectNullWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.getObject(0, yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetArray() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONArray());
        assertThat(yapionArray.getArray(0), is(new YAPIONArray()));
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetArrayNull() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getArray(0), nullValue());
    }

    @Test
    public void testGetArrayWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONObject());
        yapionArray.getArray(0, yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONArray()));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetArrayNullWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.getArray(0, yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetMap() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONMap());
        assertThat(yapionArray.getMap(0), is(new YAPIONMap()));
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetMapNull() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getMap(0), nullValue());
    }

    @Test
    public void testGetMapWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONMap());
        yapionArray.getMap(0, yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONMap()));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetMapNullWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.getMap(0, yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetPointer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONPointer("0000000000000000"));
        assertThat(yapionArray.getPointer(0), is(new YAPIONPointer("0000000000000000")));
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetPointerNull() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getPointer(0), nullValue());
    }

    @Test
    public void testGetPointerWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(new YAPIONPointer("0000000000000000"));
        yapionArray.getPointer(0, yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONPointer("0000000000000000")));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetPointerNullWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.getPointer(0, yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetValueWithoutType() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(0);
        assertThat(yapionArray.getValue(0), notNullValue());
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetValueNullWithoutType() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getValue(0), nullValue());
    }

    @Test
    public void testGetValueWithoutTypeWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        yapionArray.getValue(0, yapionAnyTypes -> {
            assertThat(yapionAnyTypes, notNullValue());
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetValueNullWithoutTypeWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.getValue(0, yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetValueByBoxed() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(0);
        assertThat(yapionArray.getValue(0, Integer.class), notNullValue());
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetValueNullByBoxed() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getValue(0, Integer.class), nullValue());
    }

    @Test
    public void testGetValueByPrimitive() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(0);
        assertThat(yapionArray.getValue(0, int.class), notNullValue());
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetValueNullByPrimitive() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getValue(0, int.class), nullValue());
    }

    @Test
    public void testGetValueOrDefault() {
        YAPIONArray yapionObject = new YAPIONArray();
        yapionObject.add("");
        assertThat(yapionObject.getValueOrDefault(0, String.class, "Hello World").get(), is(""));
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetValueNullOrDefault() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getValueOrDefault(0, String.class, "Hello World").get(), is("Hello World"));
    }

    @Test
    public void testGetValueOrDefaultByPrimitive() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add(0);
        assertThat(yapionArray.getValueOrDefault(0, int.class, 1).get(), is(0));
    }

    @Test
    public void testGetValueWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        yapionArray.getValue(0, String.class, new Consumer<YAPIONValue<String>>() {
            @Override
            public void accept(YAPIONValue<String> yapionValue) {
                assertThat(yapionValue.get(), is(""));
            }
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetValueNullWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.getValue(0, String.class, new Consumer<YAPIONValue<String>>() {
            @Override
            public void accept(YAPIONValue<String> yapionValue) {
                throw new AssertionError();
            }
        }, () -> {
            throw new SecurityException();
        });
    }


    @Test
    public void testGetValueByValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        assertThat(yapionArray.getValue(0, "").get(), is(""));
    }

    @Test
    public void testGetValueOrDefaultByValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        assertThat(yapionArray.getValueOrDefault(0, "Hello World").get(), is(""));
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetValueNullOrDefaultByValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        assertThat(yapionArray.getValueOrDefault(0, "Hello World").get(), is("Hello World"));
    }

    @Test
    public void testGetPlainValue() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        String s = yapionArray.getPlainValue(0);
        assertThat(s, is(""));
    }

    @Test
    public void testGetPlainValueOrDefault() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("");
        String s = yapionArray.getPlainValueOrDefault(0, "Hello World");
        assertThat(s, is(""));
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetPlainValueNullOrDefault() {
        YAPIONArray yapionArray = new YAPIONArray();
        String s = yapionArray.getPlainValueOrDefault(0, "Hello World");
        assertThat(s, is("Hello World"));
    }

    @Test
    public void testGetPlainValueWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.add("Hello World");
        yapionArray.getPlainValue(0, s -> {
            assertThat(s, is("Hello World"));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = YAPIONArrayIndexOutOfBoundsException.class)
    public void testGetPlainValueNullWithConsumer() {
        YAPIONArray yapionArray = new YAPIONArray();
        yapionArray.getPlainValue(0, s -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

}
