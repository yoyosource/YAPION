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

package yapion.api.object;

import org.junit.Test;
import yapion.hierarchy.types.*;

import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ObjectRetrieveAPITest {

    @Test
    public void testNotHasValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.hasValue(""), is(false));
    }

    @Test
    public void testHasValue() {
        YAPIONObject yapionObject = new YAPIONObject().add("", "");
        assertThat(yapionObject.hasValue(""), is(true));
    }

    @Test
    public void testNotHasValueOfSpecificType() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        assertThat(yapionObject.hasValue("", YAPIONType.OBJECT), is(false));
    }

    @Test
    public void testHasValueOfSpecificType() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONObject());
        assertThat(yapionObject.hasValue("", YAPIONType.OBJECT), is(true));
    }

    @Test
    public void testHasValueOfClassType() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        assertThat(yapionObject.hasValue("", String.class), is(true));
    }

    @Test
    public void testGetYAPIONAnyType() {
        YAPIONObject yapionObject = new YAPIONObject();
        YAPIONObject yapionAnyTypes = new YAPIONObject();
        yapionObject.add("", yapionAnyTypes);
        assertThat(yapionObject.getYAPIONAnyType(""), is(yapionAnyTypes));
    }

    @Test
    public void testGetObject() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONObject());
        assertThat(yapionObject.getObject(""), is(new YAPIONObject()));
    }

    @Test
    public void testGetObjectNull() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getObject(""), nullValue());
    }

    @Test
    public void testGetObjectWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONObject());
        yapionObject.getObject("", yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONObject()));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetObjectNullWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.getObject("", yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetArray() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONArray());
        assertThat(yapionObject.getArray(""), is(new YAPIONArray()));
    }

    @Test
    public void testGetArrayNull() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getArray(""), nullValue());
    }

    @Test
    public void testGetArrayWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONObject());
        yapionObject.getArray("", yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONArray()));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetArrayNullWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.getArray("", yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetMap() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONMap());
        assertThat(yapionObject.getMap(""), is(new YAPIONMap()));
    }

    @Test
    public void testGetMapNull() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getMap(""), nullValue());
    }

    @Test
    public void testGetMapWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONMap());
        yapionObject.getMap("", yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONMap()));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetMapNullWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.getMap("", yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetPointer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONPointer("0000000000000000"));
        assertThat(yapionObject.getPointer(""), is(new YAPIONPointer("0000000000000000")));
    }

    @Test
    public void testGetPointerNull() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getPointer(""), nullValue());
    }

    @Test
    public void testGetPointerWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", new YAPIONPointer("0000000000000000"));
        yapionObject.getPointer("", yapionAnyTypes -> {
            assertThat(yapionAnyTypes, is(new YAPIONPointer("0000000000000000")));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetPointerNullWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.getPointer("", yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetValueWithoutType() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("test", 0);
        assertThat(yapionObject.getValue("test"), notNullValue());
    }

    @Test
    public void testGetValueNullWithoutType() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getValue("test"), nullValue());
    }

    @Test
    public void testGetValueWithoutTypeWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        yapionObject.getValue("", yapionAnyTypes -> {
            assertThat(yapionAnyTypes, notNullValue());
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetValueNullWithoutTypeWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.getValue("", yapionAnyTypes -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

    @Test
    public void testGetValueByBoxed() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("test", 0);
        assertThat(yapionObject.getValue("test", Integer.class), notNullValue());
    }

    @Test
    public void testGetValueNullByBoxed() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getValue("test", Integer.class), nullValue());
    }

    @Test
    public void testGetValueByPrimitive() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("test", 0);
        assertThat(yapionObject.getValue("test", int.class), notNullValue());
    }

    @Test
    public void testGetValueNullByPrimitive() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getValue("test", int.class), nullValue());
    }

    @Test
    public void testGetValueOrDefault() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        assertThat(yapionObject.getValueOrDefault("", String.class, "Hello World").get(), is(""));
    }

    @Test
    public void testGetValueNullOrDefault() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getValueOrDefault("", String.class, "Hello World").get(), is("Hello World"));
    }

    @Test
    public void testGetValueOrDefaultByPrimitive() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("test", 0);
        assertThat(yapionObject.getValueOrDefault("test", int.class, 1).get(), is(0));
    }

    @Test
    public void testGetValueWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        yapionObject.getValue("", String.class, new Consumer<YAPIONValue<String>>() {
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
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.getValue("", String.class, new Consumer<YAPIONValue<String>>() {
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
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        assertThat(yapionObject.getValue("", "").get(), is(""));
    }

    @Test
    public void testGetValueOrDefaultByValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        assertThat(yapionObject.getValueOrDefault("", "Hello World").get(), is(""));
    }

    @Test
    public void testGetValueNullOrDefaultByValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        assertThat(yapionObject.getValueOrDefault("", "Hello World").get(), is("Hello World"));
    }

    @Test
    public void testGetPlainValue() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        String s = yapionObject.getPlainValue("");
        assertThat(s, is(""));
    }

    @Test
    public void testGetPlainValueOrDefault() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "");
        String s = yapionObject.getPlainValueOrDefault("", "Hello World");
        assertThat(s, is(""));
    }

    @Test
    public void testGetPlainValueNullOrDefault() {
        YAPIONObject yapionObject = new YAPIONObject();
        String s = yapionObject.getPlainValueOrDefault("", "Hello World");
        assertThat(s, is("Hello World"));
    }

    @Test
    public void testGetPlainValueWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add("", "Hello World");
        yapionObject.getPlainValue("", s -> {
            assertThat(s, is("Hello World"));
        }, () -> {
            throw new AssertionError();
        });
    }

    @Test(expected = SecurityException.class)
    public void testGetPlainValueNullWithConsumer() {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.getPlainValue("", s -> {
            throw new AssertionError();
        }, () -> {
            throw new SecurityException();
        });
    }

}
