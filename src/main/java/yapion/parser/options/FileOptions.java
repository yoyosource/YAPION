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
import lombok.experimental.Accessors;
import yapion.parser.CommentParsing;
import yapion.parser.InputStreamCharsets;

@Getter
public class FileOptions extends StreamOptions {
    private boolean gzipped = false;

    @Override
    public FileOptions commentParsing(@NonNull CommentParsing commentParsing) {
        super.commentParsing(commentParsing);
        return this;
    }

    @Override
    public FileOptions charset(InputStreamCharsets charset) {
        super.charset(charset);
        return this;
    }

    @Override
    public FileOptions stopOnStreamEnd(boolean stopOnStreamEnd) {
        super.stopOnStreamEnd(stopOnStreamEnd);
        return this;
    }

    public FileOptions gzipped(boolean gzipped) {
        this.gzipped = gzipped;
        return this;
    }
}
