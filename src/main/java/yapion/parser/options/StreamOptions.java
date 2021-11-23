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
import yapion.parser.CommentParsing;
import yapion.parser.InputStreamCharsets;
import yapion.parser.callbacks.CallbackType;
import yapion.parser.callbacks.ParseCallback;

@Getter
public class StreamOptions extends ParseOptions {
    private @NonNull InputStreamCharsets charset = InputStreamCharsets.US_ASCII;
    private boolean stopOnStreamEnd = true;

    /**
     * {@inheritDoc}
     *
     * @param commentParsing the comment parsing behaviour, default is {@link CommentParsing#IGNORE}
     * @return itself
     */
    @Override
    public StreamOptions commentParsing(@NonNull CommentParsing commentParsing) {
        super.commentParsing(commentParsing);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param lazy {@code true} for lazy parsing, {@code false} otherwise, default is {@code false}
     * @return itself
     */
    @Override
    public StreamOptions lazy(boolean lazy) {
        super.lazy(lazy);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param forceOnlyJSON
     * @return itself
     */
    public StreamOptions forceOnlyJSON(boolean forceOnlyJSON) {
        super.forceOnlyJSON(forceOnlyJSON);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param type
     * @param callback
     * @param <T>
     * @return itself
     */
    @Override
    public <T> StreamOptions parseCallback(CallbackType<T> type, ParseCallback<T> callback) {
        super.parseCallback(type, callback);
        return this;
    }

    /**
     * Set with which encoding the Stream should be read.
     *
     * @param charset the encoding of the Stream, {@link InputStreamCharsets#US_ASCII} by default.
     * @return itself
     */
    public StreamOptions charset(InputStreamCharsets charset) {
        this.charset = charset;
        return this;
    }

    /**
     * Set whether an EOF should stop the parsing or not.
     *
     * @param stopOnStreamEnd {@code true} to stop with EOF, {@code false} otherwise, default is {@code true}
     * @return itself
     */
    public StreamOptions stopOnStreamEnd(boolean stopOnStreamEnd) {
        this.stopOnStreamEnd = stopOnStreamEnd;
        return this;
    }
}
