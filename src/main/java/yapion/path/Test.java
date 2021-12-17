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

package yapion.path;

import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;
import yapion.path.elements.AnyDeeperElement;
import yapion.path.elements.Element;
import yapion.path.elements.ElementFilter;
import yapion.path.elements.Range;
import yapion.path.filters.ContainsFilter;

public class Test {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONParser.parse("""
                {
                    artists[
                        {
                            title(Hugo)
                            albums[
                                {
                                    title(Hello World)
                                    duration(100)
                                    tracks[
                                        {
                                            title(Hello World 1)
                                            duration(20)
                                        },
                                        {
                                            title(Hello World 2)
                                            duration(40)
                                        }
                                        {
                                            title(Hello World 3)
                                            duration(30)
                                        },
                                        {
                                            title(Hello World 4)
                                            duration(10)
                                        }
                                    ]
                                },
                                {
                                    title(Other than that)
                                    duration(95)
                                    tracks[
                                        {
                                            title(Other than that 1)
                                            duration(20)
                                        },
                                        {
                                            title(Other than that 2)
                                            duration(40)
                                        },
                                        {
                                            title(Other than that 3)
                                            duration(35)
                                        },
                                        {
                                            title(Other than that 4)
                                            duration(0)
                                        },
                                        {
                                            title(Other than that 5)
                                            duration(0)
                                        }
                                    ]
                                },
                                {
                                    title(Something else)
                                    duration(135)
                                    tracks[
                                        {
                                            title(Something else 1)
                                            duration(40)
                                        },
                                        {
                                            title(Something else 2)
                                            duration(70)
                                        },
                                        {
                                            title(Something else 3)
                                            duration(25)
                                        }
                                    ]
                                },
                                {
                                    title(...)
                                    duration(170)
                                    tracks[
                                        {
                                            title(... 1)
                                            duration(60)
                                        },
                                        {
                                            title(... 2)
                                            duration(50)
                                        },
                                        {
                                            title(... 3)
                                            duration(60)
                                        }
                                    ]
                                }
                            ]
                            singles[]
                        },
                        {
                            title(World)
                            albums[
                                {
                                    title(This is another Album)
                                    duration(100)
                                    tracks[
                                        {
                                            title(This is another Album 1)
                                            duration(40)
                                        },
                                        {
                                            title(This is another Album 2)
                                            duration(60)
                                        }
                                    ]
                                }
                            ]
                            singles[
                                {
                                    title(Another Test)
                                    duration(40)
                                },
                                {
                                    title(What about your life)
                                    duration(60)
                                },
                                {
                                    title(Another bites)
                                    duration(60)
                                }
                            ]
                        },
                        {
                            title(Example)
                            albums[]
                            singles[]
                        }
                    ]
                }
                """);
        System.out.println(yapionObject);

        YAPIONPath yapionPath = new YAPIONPath(
                new Element("artists"),
                new AnyDeeperElement(),
                new ElementFilter(
                        new ContainsFilter(
                                new Element("title")
                        ).and(new ContainsFilter(
                                new Element("duration"),
                                new ElementFilter(
                                        Range.range(20, 80)
                                )
                        ))
                )
        );
        System.out.println(yapionPath.apply(yapionObject));
        System.out.println(yapionObject.select("artists.**{title&duration{?[20...80]}}"));
        System.out.println(yapionObject.select("artists.*.[albums,singles].**{title&duration{?[20...80]}}"));
        // System.out.println(yapionObject.select("artists.[1...]"));
        // System.out.println(yapionObject.select("artists.[0,1].albums.*.title"));
    }
}
