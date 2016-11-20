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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import io.github.clendy.leanback.R;

import static io.github.clendy.leanback.widget.OnLoadMoreListener.STATE_MORE_LOADED;


/**
 * A horizontally scrolling lists that supports paging loading
 *
 * @author Clendy
 * @date 2016/11/15 015 17:10
 * @e-mail yc330483161@outlook.com
 */
public class HorizontalLoadMoreGridView extends HorizontalGridView {

    private static final String TAG = HorizontalLoadMoreGridView.class.getSimpleName();

    private Context mContext;
    private boolean canLoadMore = false;
    private boolean addLoadingView = false;
    private int allLoadedToastCount = 0;
    private int mLoadState = STATE_MORE_LOADED;

    private OnLoadMoreListener mLoadMoreListener;


    public HorizontalLoadMoreGridView(Context context) {
        this(context, null);
    }

    public HorizontalLoadMoreGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalLoadMoreGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbLoadMoreGridView);
        try {
            canLoadMore = a.getBoolean(R.styleable.lbLoadMoreGridView_canLoadMore, false);
            addLoadingView = a.getBoolean(R.styleable.lbLoadMoreGridView_addLoadingView, false);
        } finally {
            a.recycle();
        }
    }

    /**
     * if you want invoke this method to load more data, you must to implement
     * the interface {@link OnLoadMoreListener}
     */
    public void loadMoreData() {
        if (canLoadMore && isMoreLoaded()) {
            this.post(new Runnable() {
                @Override
                public void run() {
                    if (mLoadMoreListener != null) {
                        notifyMoreLoading();
                        mLoadMoreListener.loadMore();
                        mLoadMoreListener.showMsgLoading();
                    }
                }
            });
        } else if (canLoadMore && isAllLoaded() && allLoadedToastCount++ <= 0) {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.showMsgAllLoaded();
            }
        }
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    /**
     * determine whether the focus is located on the top row
     *
     * @return true if the focus on the top row
     */
    public boolean isFocusOnTopRow() {
        if (mLayoutManager != null && getFocusedChild() != null) {
            int position = mLayoutManager.getPosition(getFocusedChild());
            return position % mLayoutManager.getNumRows() == 0;
        }
        hasFocus();
        return false;
    }

    /**
     * determine whether the focus is located on the bottom row
     *
     * @return true if the focus on the bottom row
     */
    public boolean isFocusOnBottomRow() {
        if (mLayoutManager != null && getFocusedChild() != null) {
            int position = mLayoutManager.getPosition(getFocusedChild());
            if (position % mLayoutManager.getNumRows() == mLayoutManager.getNumRows() - 1) {
                return true;
            } else if (position == mLayoutManager.getItemCount() - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * determine whether the focus is located on the rightmost column
     *
     * @return true if the focus on the bottom row
     */
    public boolean isFocusOnRightmostColumn() {
        if (mLayoutManager != null && getFocusedChild() != null) {
            int position = mLayoutManager.getPosition(getFocusedChild());
            int rowCount = mLayoutManager.getItemCount() / mLayoutManager.getNumRows();
            int rowNum = position / mLayoutManager.getNumRows();
            return rowNum == rowCount - 1;
        }

        return false;
    }

    /**
     * determine whether the focus is located on the rightmost column
     *
     * @param focus    the current focus view
     * @param position The adapter position of the item which is rendered by this View.
     * @return true if the focus on the rightmost column
     */
    public boolean isFocusOnRightmostColumn(View focus, int position) {
        if (mLayoutManager != null && focus != null) {
            int rowCount = mLayoutManager.getItemCount() / mLayoutManager.getNumRows();
            int rowNum = position / mLayoutManager.getNumRows();
            return rowNum == rowCount - 1;
        }
        return false;
    }


    /**
     * notify more data is loading
     */
    public void notifyMoreLoading() {
        mLoadState = OnLoadMoreListener.STATE_MORE_LOADING;
    }

    /**
     * notify more data loading is completed
     */
    public void notifyMoreLoaded() {
        mLoadState = OnLoadMoreListener.STATE_MORE_LOADED;
    }

    /**
     * notify all data loaded
     */
    public void notifyAllLoaded() {
        mLoadState = OnLoadMoreListener.STATE_ALL_LOADED;
    }

    /**
     * to determine if the load is being loaded
     */
    public boolean isMoreLoading() {
        return mLoadState == OnLoadMoreListener.STATE_MORE_LOADING;
    }

    public boolean isMoreLoaded() {
        return mLoadState == OnLoadMoreListener.STATE_MORE_LOADED;
    }

    /**
     * To determine whether the data are loaded complete
     */
    public boolean isAllLoaded() {
        return mLoadState == OnLoadMoreListener.STATE_ALL_LOADED;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mOnKeyInterceptListener != null && mOnKeyInterceptListener.onInterceptKeyEvent(event)) {
            return true;
        }
        if (super.dispatchKeyEvent(event)) {
            return true;
        }
        if (mOnUnhandledKeyListener != null && mOnUnhandledKeyListener.onUnhandledKey(event)) {
            return true;
        }
        return false;
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
