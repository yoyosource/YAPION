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

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import net.sf.cglib.core.ClassGenerator;
import net.sf.cglib.core.DefaultGeneratorStrategy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.transform.TransformingClassGenerator;
import net.sf.cglib.transform.impl.AddPropertyTransformer;
import org.objectweb.asm.Type;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisBase;
import org.objenesis.ObjenesisStd;
import org.objenesis.strategy.SerializingInstantiatorStrategy;
import org.objenesis.strategy.SingleInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.lang.invoke.MethodHandles;

public class ObjenesisTest {

    private int i;
    private String s = "Hello World";

    public ObjenesisTest(int i, String s) {

    }

    private final class Tets {

    }

    public static void main(String[] args) throws Exception {
        /*Objenesis objenesis = new ObjenesisStd(false);
        ObjenesisBase objenesisBase = new ObjenesisBase(new StdInstantiatorStrategy(), false);
        ObjenesisTest objenesisTest = objenesisBase.newInstance(ObjenesisTest.class);
        System.out.println(objenesisTest);*/

        ClassPool defaultClassPool = ClassPool.getDefault();
        CtClass superClass = defaultClassPool.get(ObjenesisTest.class.getTypeName());
        CtClass cc = defaultClassPool.makeClass(ObjenesisTest.class.getTypeName() + "Extended", superClass);
        cc.addConstructor(new CtConstructor(new CtClass[0], cc));
        System.out.println(cc);
        System.out.println(cc.toClass(ObjenesisTest.class));
        System.out.println((ObjenesisTest)cc.toClass(ObjenesisTest.class).getDeclaredConstructor().newInstance());
        //System.out.println((ObjenesisTest)cc.toClass().getDeclaredConstructor().newInstance());

        /*ObjenesisBase objenesisBase = new ObjenesisBase(new StdInstantiatorStrategy(), false);
        Tets objenesisTest = objenesisBase.newInstance(Tets.class);
        System.out.println(objenesisTest);*/
    }

    @Override
    public String toString() {
        return "ASMTest{" +
                "i=" + i +
                ", s='" + s + '\'' +
                '}';
    }

}