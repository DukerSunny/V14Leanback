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
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

/**
 * A vertically scrolling lists that supports paging loading
 *
 * @author Clendy
 * @date 2016/11/15 015 17:11
 * @e-mail yc330483161@outlook.com
 */
public class VerticalLoadMoreGridView extends VerticalGridView {

    private static final String TAG = VerticalLoadMoreGridView.class.getSimpleName();

    private OnLoadMoreListener mLoadMoreListener;

    private int mLoadState = OnLoadMoreListener.STATE_MORE_LOADED;

    public VerticalLoadMoreGridView(Context context) {
        this(context, null);
    }

    public VerticalLoadMoreGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalLoadMoreGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }

    public void loadMoreData() {
        if (isMoreLoaded()) {
            this.post(new Runnable() {
                @Override
                public void run() {
                    if (mLoadMoreListener != null) {
                        notifyMoreLoading();
                        mLoadMoreListener.loadMore();
                    }
                }
            });
        }
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public boolean isFocusOnLeftmostColumn() {
        if (mLayoutManager != null && getFocusedChild() != null) {
            int position = mLayoutManager.getPosition(getFocusedChild());
            return position % mLayoutManager.getNumRows() == 0;
        }
        return false;
    }

    public boolean isFocusOnRightmostColumn() {
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


    public boolean isFocusOnBottomRow() {
        if (mLayoutManager != null && getFocusedChild() != null) {
            int position = mLayoutManager.getPosition(getFocusedChild());
            int rowCount = mLayoutManager.getItemCount() / mLayoutManager.getNumRows();
            int rowNum = position / mLayoutManager.getNumRows();
            return rowNum == rowCount - 1;
        }

        return false;
    }

    public boolean isFocusOnBottomRow(View focus, int position) {
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
        if (direction == FOCUS_DOWN) {
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
