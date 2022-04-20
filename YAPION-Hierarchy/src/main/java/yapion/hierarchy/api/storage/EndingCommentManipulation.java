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

package yapion.hierarchy.api.storage;

public interface EndingCommentManipulation<I> extends EndingComments {

    I itself();

    default I addEndingComment(String comment) {
        getEndingComments().add(comment);
        return itself();
    }

    default I addEndingComment(String comment, int index) {
        getEndingComments().add(index, comment);
        return itself();
    }

    default I removeEndingComment(int index) {
        getEndingComments().remove(index);
        return itself();
    }

    default I clearEndingComments() {
        getEndingComments().clear();
        return itself();
    }
}
