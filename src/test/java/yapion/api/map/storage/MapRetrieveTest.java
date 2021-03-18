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

package yapion.api.map.storage;

import org.junit.Test;
import yapion.hierarchy.types.*;

import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class MapRetrieveTest {

    @Test
    public void testNotHasValue() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.hasValue(new YAPIONValue<>("")), is(false));
    }

    @Test
    public void testHasValue() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        assertThat(yapionMap.hasValue(new YAPIONValue<>("")), is(true));
    }

    @Test
    public void testNotHasValueOfSpecificType() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        assertThat(yapionMap.hasValue(new YAPIONValue<>(""), YAPIONType.OBJECT), is(false));
    }

    @Test
    public void testHasValueOfSpecificType() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), new YAPIONObject());
        assertThat(yapionMap.hasValue(new YAPIONValue<>(""), YAPIONType.OBJECT), is(true));
    }

    @Test
    public void testHasValueOfClassType() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        assertThat(yapionMap.hasValue(new YAPIONValue<>(""), String.class), is(true));
    }

    @Test
    public void testGetYAPIONAnyType() {
        YAPIONMap yapionMap = new YAPIONMap();
        YAPIONObject yapionAnyTypes = new YAPIONObject();
        yapionMap.add(new YAPIONValue<>(""), yapionAnyTypes);
        assertThat(yapionMap.getYAPIONAnyType(new YAPIONValue<>("")), is(yapionAnyTypes));
    }

    @Test
    public void testGetObject() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), new YAPIONObject());
        assertThat(yapionMap.getObject(new YAPIONValue<>("")), is(new YAPIONObject()));
    }

    @Test
    public void testGetObjectNull() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getObject(new YAPIONValue<>("")), nullValue());
    }

    @Test
    public void testGetObjectWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), new YAPIONObject());
        yapionMap.getObject(new YAPIONValue<>(""), yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONObject()));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetObjectNullWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.getObject(new YAPIONValue<>(""), yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetArray() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), new YAPIONArray());
        assertThat(yapionMap.getArray(new YAPIONValue<>("")), is(new YAPIONArray()));
    }

    @Test
    public void testGetArrayNull() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getArray(new YAPIONValue<>("")), nullValue());
    }

    @Test
    public void testGetArrayWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), new YAPIONObject());
        yapionMap.getArray(new YAPIONValue<>(""), yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONArray()));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetArrayNullWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.getArray(new YAPIONValue<>(""), yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetMap() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), new YAPIONMap());
        assertThat(yapionMap.getMap(new YAPIONValue<>("")), is(new YAPIONMap()));
    }

    @Test
    public void testGetMapNull() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getMap(new YAPIONValue<>("")), nullValue());
    }

    @Test
    public void testGetMapWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), new YAPIONMap());
        yapionMap.getMap(new YAPIONValue<>(""), yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONMap()));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetMapNullWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.getMap(new YAPIONValue<>(""), yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetPointer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), new YAPIONPointer("0000000000000000"));
        assertThat(yapionMap.getPointer(new YAPIONValue<>("")), is(new YAPIONPointer("0000000000000000")));
    }

    @Test
    public void testGetPointerNull() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getPointer(new YAPIONValue<>("")), nullValue());
    }

    @Test
    public void testGetPointerWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), new YAPIONPointer("0000000000000000"));
        yapionMap.getPointer(new YAPIONValue<>(""), yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONPointer("0000000000000000")));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetPointerNullWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.getPointer(new YAPIONValue<>(""), yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetValueWithoutType() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>("test"), 0);
        assertThat(yapionMap.getValue(new YAPIONValue<>("test")), notNullValue());
    }

    @Test
    public void testGetValueNullWithoutType() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getValue(new YAPIONValue<>("test")), nullValue());
    }

    @Test
    public void testGetValueWithoutTypeWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        yapionMap.getValue(new YAPIONValue<>(""), yapionAnyTypes -> {
            assertThat(yapionAnyTypes, notNullValue());
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetValueNullWithoutTypeWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.getValue(new YAPIONValue<>(""), yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetValueByBoxed() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>("test"), 0);
        assertThat(yapionMap.getValue(new YAPIONValue<>("test"), Integer.class), notNullValue());
    }

    @Test
    public void testGetValueNullByBoxed() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getValue(new YAPIONValue<>("test"), Integer.class), nullValue());
    }

    @Test
    public void testGetValueByPrimitive() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>("test"), 0);
        assertThat(yapionMap.getValue(new YAPIONValue<>("test"), int.class), notNullValue());
    }

    @Test
    public void testGetValueNullByPrimitive() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getValue(new YAPIONValue<>("test"), int.class), nullValue());
    }

    @Test
    public void testGetValueOrDefault() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        assertThat(yapionMap.getValueOrDefault(new YAPIONValue<>(""), String.class, "Hello World").get(), is(""));
    }

    @Test
    public void testGetValueNullOrDefault() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getValueOrDefault(new YAPIONValue<>(""), String.class, "Hello World").get(), is("Hello World"));
    }

    @Test
    public void testGetValueOrDefaultByPrimitive() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>("test"), 0);
        assertThat(yapionMap.getValueOrDefault(new YAPIONValue<>("test"), int.class, 1).get(), is(0));
    }

    @Test
    public void testGetValueWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        yapionMap.getValue(new YAPIONValue<>(""), String.class, new Consumer<YAPIONValue<String>>() {
            @Override
            public void accept(YAPIONValue<String> yapionValue) {
                assertThat(yapionValue.get(), is(""));
            }
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetValueNullWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.getValue(new YAPIONValue<>(""), String.class, new Consumer<YAPIONValue<String>>() {
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
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        assertThat(yapionMap.getValue(new YAPIONValue<>(""), "").get(), is(""));
    }

    @Test
    public void testGetValueOrDefaultByValue() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        assertThat(yapionMap.getValueOrDefault(new YAPIONValue<>(""), "Hello World").get(), is(""));
    }

    @Test
    public void testGetValueNullOrDefaultByValue() {
        YAPIONMap yapionMap = new YAPIONMap();
        assertThat(yapionMap.getValueOrDefault(new YAPIONValue<>(""), "Hello World").get(), is("Hello World"));
    }

    @Test
    public void testGetPlainValue() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        String s = yapionMap.getPlainValue(new YAPIONValue<>(""));
        assertThat(s, is(""));
    }

    @Test
    public void testGetPlainValueOrDefault() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "");
        String s = yapionMap.getPlainValueOrDefault(new YAPIONValue<>(""), "Hello World");
        assertThat(s, is(""));
    }

    @Test
    public void testGetPlainValueNullOrDefault() {
        YAPIONMap yapionMap = new YAPIONMap();
        String s = yapionMap.getPlainValueOrDefault(new YAPIONValue<>(""), "Hello World");
        assertThat(s, is("Hello World"));
    }

    @Test
    public void testGetPlainValueWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.add(new YAPIONValue<>(""), "Hello World");
        yapionMap.getPlainValue(new YAPIONValue<>(""), s -> {
            assertThat(s, is("Hello World"));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetPlainValueNullWithConsumer() {
        YAPIONMap yapionMap = new YAPIONMap();
        yapionMap.getPlainValue(new YAPIONValue<>(""), s -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

}
