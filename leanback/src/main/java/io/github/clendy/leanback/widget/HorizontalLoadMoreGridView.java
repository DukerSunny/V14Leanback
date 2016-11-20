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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;

import static io.github.clendy.leanback.widget.OnLoadMoreListener.STATE_ALL_LOADED;
import static io.github.clendy.leanback.widget.OnLoadMoreListener.STATE_MORE_LOADED;
import static io.github.clendy.leanback.widget.OnLoadMoreListener.STATE_MORE_LOADING;


/**
 * A horizontally scrolling lists that supports paging loading
 *
 * @author Clendy
 * @date 2016/11/15 015 17:10
 * @e-mail yc330483161@outlook.com
 */
public class HorizontalLoadMoreGridView extends HorizontalGridView {

    private static final String TAG = HorizontalLoadMoreGridView.class.getSimpleName();

    private OnLoadMoreListener mLoadMoreListener;

    private int mLoadState = STATE_MORE_LOADED;

    private class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mLoadState == STATE_MORE_LOADED
                    && SCROLL_STATE_IDLE == newState
                    && isLastItemLaidOut()
                    && mLoadMoreListener != null) {

                mLoadState = STATE_MORE_LOADING;
                mLoadMoreListener.loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

    public HorizontalLoadMoreGridView(Context context) {
        this(context, null);
    }

    public HorizontalLoadMoreGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalLoadMoreGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new RecyclerViewScrollListener());
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    private boolean isLastItemLaidOut() {
        if (getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) getLayoutManager();
            return layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount() - 1;
        }
        return false;
    }

    private boolean isFocusOnLastItem() {
        if (getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) getLayoutManager();
            return layoutManager.getPosition(getFocusedChild()) == layoutManager.getItemCount() - 1;

        }
        return false;
    }

    /**
     * notify more data loading is completed
     */
    public void notifyMoreLoaded() {
        mLoadState = STATE_MORE_LOADED;
    }

    /**
     * notify all data is completed
     */
    public void notifyAllLoaded() {
        mLoadState = STATE_ALL_LOADED;
    }

    /**
     * to determine if the load is being loaded
     */
    public boolean isMoreLoading() {
        return mLoadState == STATE_MORE_LOADING;
    }

    /**
     * To determine whether the data are loaded complete
     */
    public boolean isAllLoaded() {
        return mLoadState == STATE_ALL_LOADED;
    }

    /**
     * You must override this method if RecyclerView need to page loading and you need to handle
     * the problem of Focus jitter
     *
     * @param focused   focused The view that currently has focus
     * @param direction One of FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, and
     *                  FOCUS_RIGHT, or 0 for not applicable.
     * @return the result of focusSearch
     */
    @Override
    public View focusSearch(View focused, int direction) {
        // Step.1 search focus by onInterceptFocusSearch
        View result = getLayoutManager().onInterceptFocusSearch(focused, direction);
        if (result != null) {
            return result;
        }
        // Step.2 search focus by FocusFinder
        final FocusFinder ff = FocusFinder.getInstance();
        result = ff.findNextFocus(this, focused, direction);
        if (result != null) {
            return result;
        }
        // Step.3 search focus by onFocusSearchFailed
        if (getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) getLayoutManager();
            if (layoutManager.ensureRecyclerState()) {
                result = layoutManager.onFocusSearchFailed(focused, direction,
                        layoutManager.getRecycler(), layoutManager.getState());
                if (result != null) {
                    return result;
                }
            }
        }
        if (direction == FOCUS_RIGHT) {
            final int position = getLayoutManager().getPosition(focused);
            if (position < getLayoutManager().getItemCount() - 1) {
                focused.post(new Runnable() {
                    @Override
                    public void run() {
                        requestLayout();
                    }
                });
            }
        }

        return null;
    }
}
