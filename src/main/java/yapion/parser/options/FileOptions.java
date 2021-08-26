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
public class FileOptions extends StreamOptions {
    private boolean gzipped = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public FileOptions commentParsing(@NonNull CommentParsing commentParsing) {
        super.commentParsing(commentParsing);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileOptions lazy(boolean lazy) {
        super.lazy(lazy);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileOptions charset(InputStreamCharsets charset) {
        super.charset(charset);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileOptions stopOnStreamEnd(boolean stopOnStreamEnd) {
        super.stopOnStreamEnd(stopOnStreamEnd);
        return this;
    }

    /**
     * Set if the File you want to read should be read with a {@link java.util.zip.GZIPInputStream} or not.
     *
     * @param gzipped {@code true} for {@link java.util.zip.GZIPInputStream}, {@code false} otherwise, default is {@code false}
     * @return itself
     */
    public FileOptions gzipped(boolean gzipped) {
        this.gzipped = gzipped;
        return this;
    }
}
