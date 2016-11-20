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

package io.github.clendy.leanback.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * HeaderDecoration
 *
 * @author Clendy
 * @date 2016/11/16 016 19:22
 * @e-mail yc330483161@outlook.com
 */
public class HeaderDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = GridLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = GridLayoutManager.VERTICAL;

    private Drawable mDivider;

    private Context mContext;
    private int mOrientation = VERTICAL_LIST;
    private int mHorizontalSpacing = 0;
    private int mVerticalSpacing = 0;
    private boolean mIncludeEdge = false;
    private boolean mHasHeader = false;



    public HeaderDecoration(int hSpacing, int vSpacing, boolean includeEdge, boolean hasHeader) {
        mHorizontalSpacing = hSpacing;
        mVerticalSpacing = vSpacing;
        mIncludeEdge = includeEdge;
        mHasHeader = hasHeader;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            int spanCount = layoutManager.getSpanCount();

            if (mHasHeader) {
                if (position == 0) {
                    outRect.set(0, 0, 0, 0);
                }
                if (position > 0) {
                    position--;
                    int column = position % spanCount;
                    getGridItemOffsets(outRect, position, column, spanCount);
                }
            } else {
                int column = position % spanCount;
                getGridItemOffsets(outRect, position, column, spanCount);
            }


        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
            int spanCount = layoutManager.getSpanCount();
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            int column = lp.getSpanIndex();
            getGridItemOffsets(outRect, position, column, spanCount);

        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            outRect.left = mHorizontalSpacing;
            outRect.right = mHorizontalSpacing;
            if (mIncludeEdge) {
                if (position == 0) {
                    outRect.top = mVerticalSpacing;
                }
                outRect.bottom = mVerticalSpacing;
            } else {
                if (position > 0) {
                    outRect.top = mVerticalSpacing;
                }
            }
        }
    }

    private void getGridItemOffsets(Rect outRect, int position, int column, int spanCount) {
        if (mIncludeEdge) {
            outRect.left = mHorizontalSpacing * (spanCount - column) / spanCount;
            outRect.right = mHorizontalSpacing * (column + 1) / spanCount;
            if (position < spanCount) {
                outRect.top = mVerticalSpacing;
            }
            outRect.bottom = mVerticalSpacing;
        } else {
            outRect.left = mHorizontalSpacing * column / spanCount;
            outRect.right = mHorizontalSpacing * (spanCount - 1 - column) / spanCount;

            outRect.bottom = mVerticalSpacing;
        }
    }
}
