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

package yapion.parser.options;

import lombok.Getter;
import lombok.NonNull;
import yapion.hierarchy.types.YAPIONType;
import yapion.parser.CommentParsing;
import yapion.parser.callbacks.CallbackType;
import yapion.parser.callbacks.ParseCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class ParseOptions {
    private @NonNull CommentParsing commentParsing = CommentParsing.IGNORE;
    private boolean lazy = false;
    private boolean forceOnlyJSON = false;
    private boolean forceOnlyYAPION = false;
    private Map<CallbackType<?>, ParseCallback<?>> parseCallbackMap = new HashMap<>();
    private Set<YAPIONType> disabledTypes = new HashSet<>();

    /**
     * Set how comments should be treated by the parser.
     *
     * @param commentParsing the comment parsing behaviour, default is {@link CommentParsing#IGNORE}
     * @return itself
     */
    public ParseOptions commentParsing(@NonNull CommentParsing commentParsing) {
        this.commentParsing = commentParsing;
        return this;
    }

    /**
     * Set how the parser should parse 'keys', either lazy, removing any whiteSpace at the end, or not lazy keeping them.
     *
     * <br><br>Example:
     * <br>Input: <code>{test (Hello World)}</code>
     * <br>Output: <code>{test (Hello World)}</code>
     * <br>Output lazy: <code>{test(Hello World)}</code>
     *
     * @param lazy {@code true} for lazy parsing, {@code false} otherwise, default is {@code false}
     * @return itself
     */
    public ParseOptions lazy(boolean lazy) {
        this.lazy = lazy;
        return this;
    }

    /**
     *
     * @param forceOnlyJSON
     * @return itself
     */
    public ParseOptions forceOnlyJSON(boolean forceOnlyJSON) {
        this.forceOnlyJSON = forceOnlyJSON;
        return this;
    }

    /**
     *
     * @param forceOnlyYAPION
     * @return itself
     */
    public ParseOptions forceOnlyYAPION(boolean forceOnlyYAPION) {
        this.forceOnlyYAPION = forceOnlyYAPION;
        return this;
    }

    /**
     *
     * @param type
     * @param callback
     * @param <T>
     * @return itself
     */
    public <T> ParseOptions parseCallback(CallbackType<T> type, ParseCallback<T> callback) {
        parseCallbackMap.put(type, callback);
        return this;
    }

    /**
     *
     * @param type
     * @return itself
     */
    public ParseOptions disabledType(YAPIONType type) {
        disabledTypes.add(type);
        return this;
    }
}
