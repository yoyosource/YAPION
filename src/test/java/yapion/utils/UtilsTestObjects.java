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

package yapion.utils;

public class UtilsTestObjects {

    public static class UtilReflectionsTest {

        private final int i = 0;

    }

    public static class UtilReflectionsTestExtends extends UtilModifierTest {

        private final int i = 0;

    }

    public static class UtilReflectionsTestExtendsTwo extends UtilModifierTest {

        private final int j = 0;

    }

    public static class UtilModifierTest {

        private static final int i = 0;
        private final transient int j = 0;
        private final int k = 0;

    }

}
