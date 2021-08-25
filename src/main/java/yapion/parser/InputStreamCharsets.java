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

package yapion.parser;

public enum InputStreamCharsets {
    /**
     * Just the default 0x00 - 0x7F
     */
    US_ASCII,

    /**
     * Also known as ISO_8859_1
     * Just the default 0x00 - 0xFF
     */
    LATIN_1,

    /**
     * <table>
     *   <tr>
     *     <th>#</th>
     *     <th>Bits</th>
     *     <th>BitPattern</th>
     *   </tr>
     *   <tr>
     *       <td>1</td>
     *       <td>7</td>
     *       <td>0xxxxxxx</td>
     *   </tr>
     *   <tr>
     *       <td>2</td>
     *       <td>11</td>
     *       <td>110xxxxx 10xxxxxx</td>
     *   </tr>
     *   <tr>
     *       <td>3</td>
     *       <td>16</td>
     *       <td>1110xxxx 10xxxxxx 10xxxxxx</td>
     *   </tr>
     *   <tr>
     *       <td>4</td>
     *       <td>21</td>
     *       <td>11110xxx 10xxxxxx 10xxxxxx 10xxxxxx</td>
     *   </tr>
     * </table>
     */
    UTF_8
}
