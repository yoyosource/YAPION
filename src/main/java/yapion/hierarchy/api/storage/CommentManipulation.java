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

public interface CommentManipulation<T> extends Comments {

    T itself();

    default T addComment(String comment) {
        getComments().add(comment);
        return itself();
    }

    default T addComment(String comment, int index) {
        getComments().add(index, comment);
        return itself();
    }

    default T removeComment(int index) {
        getComments().remove(index);
        return itself();
    }

    default T clearComments() {
        getComments().clear();
        return itself();
    }
}
