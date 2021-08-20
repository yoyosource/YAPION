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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CommentParsing {

    /**
     * Any {@link YAPIONParser} defaults to this behaviour.
     *
     * Any comment starting tag '/*' will be ignored and interpreted as normal characters.
     */
    IGNORE(false, false),

    /**
     * Any comment starting tag '/*' will be parsed and everything until the ending will be ignored and skipped.
     * This will remove any comment in the source and can't output them afterwards.
     */
    SKIP(true, false),

    /**
     * Any comment starting tag '/*' will be parsed and everything until the ending will be grabbed and added to the
     * next element parsed or closing tag parsed. The last behaviour only applies to {@link yapion.hierarchy.types.YAPIONType#ARRAY},
     * {@link yapion.hierarchy.types.YAPIONType#MAP} and {@link yapion.hierarchy.types.YAPIONType#OBJECT}. This is to
     * enable ending comments in any {@link yapion.hierarchy.api.groups.YAPIONDataType}.
     */
    KEEP(true, true);

    private boolean parse;
    private boolean add;
}
