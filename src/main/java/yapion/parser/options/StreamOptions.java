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

@Getter
public class StreamOptions extends ParseOptions {
    private @NonNull InputStreamCharsets charset = InputStreamCharsets.US_ASCII;
    private boolean stopOnStreamEnd = true;

    @Override
    public StreamOptions commentParsing(@NonNull CommentParsing commentParsing) {
        super.commentParsing(commentParsing);
        return this;
    }

    @Override
    public StreamOptions lazy(boolean lazy) {
        super.lazy(lazy);
        return this;
    }

    public StreamOptions charset(InputStreamCharsets charset) {
        this.charset = charset;
        return this;
    }

    public StreamOptions stopOnStreamEnd(boolean stopOnStreamEnd) {
        this.stopOnStreamEnd = stopOnStreamEnd;
        return this;
    }
}
