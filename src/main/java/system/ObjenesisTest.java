/**
 * Copyright 2019,2020 yoyosource
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

package system;

import org.objenesis.ObjenesisBase;
import org.objenesis.strategy.StdInstantiatorStrategy;

public class ObjenesisTest {

    private int i;
    private final String s = "Hello World";

    public ObjenesisTest(int i, String s) {

    }

    private final class Tets {

    }

    public static void main(String[] args) throws Exception {
        // Objenesis objenesis = new ObjenesisStd(false);
        ObjenesisBase objenesisBase = new ObjenesisBase(new StdInstantiatorStrategy(), false);
        ObjenesisTest objenesisTest = objenesisBase.newInstance(ObjenesisTest.class);
        System.out.println(objenesisTest);
    }

    @Override
    public String toString() {
        return "ASMTest{" +
                "i=" + i +
                ", s='" + s + '\'' +
                '}';
    }

}