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

import yapion.hierarchy.output.SystemOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.options.ParseOptions;

public class Test {

    public static void main(String[] args) {
        {
            YAPIONObject yapionObject = YAPIONParser.parse(
                    """
                    {
                      Hello(World)
                    }
                    """);
            System.out.println(yapionObject);
        }
        {
            YAPIONObject yapionObject = YAPIONParser.parse(
                    """
                    [
                      Hello World,
                    ]
                    """);
            System.out.println(yapionObject);
        }
        {
            YAPIONObject yapionObject = YAPIONParser.parse(
                    """
                    <
                      (Hello World):(Hello World)
                      /*Hello World*/
                      {}:(Test)
                      []:->0000000000000000
                      <>:{}
                    >
                    """,
                    new ParseOptions().commentParsing(CommentParsing.KEEP)
            );
            System.out.println(yapionObject);
        }
        {
            YAPIONObject yapionObject = YAPIONParser.parse(
                    """
                    <
                      (Hello World):(Hello World)
                      /*Hello World*/
                      {}:(Test)
                      []:->0000000000000000
                      <>:{}
                    >
                    """,
                    new ParseOptions().commentParsing(CommentParsing.KEEP)
            );
            System.out.println(yapionObject);
        }
        {
            try {
                YAPIONParser.parse(
                        """
                        {
                          Hello(\\u99kk)
                        }
                        """);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        {
            YAPIONParser.parse("{regions{as41{prototype(as)flagStorage{COLOR(WHITE)}skin(AirShip)}wg12{prototype(wg)flagStorage{COLOR(WHITE)TNT(DENY)}skin(WarGear)}wg11{prototype(wg)flagStorage{}skin(Pro WarGear)}mwg23{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}mwg22{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}ws12{prototype(ws_inner)flagStorage{COLOR(WHITE)}skin(WarShip)}mwg21{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}mwg43{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}ws11{prototype(ws)flagStorage{COLOR(WHITE)}skin(WarShip)}mwg42{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}mwg41{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}ws_rahmen{prototype(ws_rahmen)flagStorage{COLOR(WHITE)TNT(DENY)}skin(WarShip Rahmen)}wg32{prototype(wg)flagStorage{COLOR(WHITE)FREEZE(ACTIVE)}skin(WarGear)}wg31{prototype(wg)flagStorage{COLOR(WHITE)}skin(Prestige WarGear)}as21{prototype(as)flagStorage{COLOR(WHITE)}skin(AirShip)}as42{prototype(as)flagStorage{COLOR(WHITE)}skin(AirShip)}as22{prototype(as)flagStorage{COLOR(WHITE)}skin(AirShip)}wg22{prototype(wg)flagStorage{COLOR(WHITE)FREEZE(ACTIVE)}skin(WarGear)}mwg13{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}mwg12{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}mwg11{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}mwg33{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}mwg32{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}ws22{prototype(ws_inner)flagStorage{COLOR(WHITE)}skin(WarShip)}mwg31{prototype(mwg)flagStorage{COLOR(WHITE)}skin(Steam Cliffs)}mwg53{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}ws21{prototype(ws)flagStorage{COLOR(WHITE)}skin(WarShip)}mwg52{prototype(mwg)flagStorage{COLOR(WHITE)}skin(MiniWarGear)}mwg51{prototype(mwg)flagStorage{COLOR(WHITE)}skin(Nethers-Reichtum)}ws_rumpf{prototype(ws_rumpf)flagStorage{COLOR(WHITE)TNT(DENY)}skin(WarShip Rumpf)}wg21{prototype(wg)flagStorage{COLOR(WHITE)}skin(Nethers-Reichtum)}as32{prototype(as)flagStorage{COLOR(WHITE)}skin(AirShip)}as31{prototype(as)flagStorage{COLOR(WHITE)}skin(AirShip)}as12{prototype(as)flagStorage{COLOR(WHITE)}skin(AirShip)}as11{prototype(as)flagStorage{COLOR(WHITE)}skin(AirShip)}global{flagStorage{TNT(DENY)CHANGED()}skin(null)}}warps{}}").toYAPION(new SystemOutput(true));
        }
    }
}
