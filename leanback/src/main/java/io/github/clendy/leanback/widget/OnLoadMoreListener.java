/*
 * Copyright 2016 Clendy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.clendy.leanback.widget;

/**
 * A RecyclerView load more listener
 *
 * @author Clendy
 * @date 2016/11/15 015 20:29
 * @e-mail yc330483161@outlook.com
 */
public interface OnLoadMoreListener {

    int STATE_MORE_LOADING = 1;
    int STATE_MORE_LOADED = 2;
    int STATE_ALL_LOADED = 3;

    void loadMore();
}
