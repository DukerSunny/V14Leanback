/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.clendy.leanback.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;

/**
 * Helper class to handle ItemAlignmentFacet in a grid view.
 */
class ItemAlignmentFacetHelper {

    private static Rect sRect = new Rect();

    /**
     * get alignment position relative to optical left/top of itemView.
     */
    static int getAlignmentPosition(View itemView, ItemAlignmentFacet.ItemAlignmentDef facet,
                                    int orientation) {
        GridLayoutManager.LayoutParams p = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        View view = itemView;
        if (facet.mViewId != 0) {
            view = itemView.findViewById(facet.mViewId);
            if (view == null) {
                view = itemView;
            }
        }
        int alignPos = facet.mOffset;
        if (orientation == HORIZONTAL) {
            if (facet.mOffset >= 0) {
                if (facet.mOffsetWithPadding) {
                    alignPos += view.getPaddingLeft();
                }
            } else {
                if (facet.mOffsetWithPadding) {
                    alignPos -= view.getPaddingRight();
                }
            }
            if (facet.mOffsetPercent != ItemAlignmentFacet.ITEM_ALIGN_OFFSET_PERCENT_DISABLED) {
                alignPos += ((view == itemView ? p.getOpticalWidth(view) : view.getWidth())
                        * facet.mOffsetPercent) / 100f;
            }
            if (itemView != view) {
                sRect.left = alignPos;
                ((ViewGroup) itemView).offsetDescendantRectToMyCoords(view, sRect);
                alignPos = sRect.left - p.getOpticalLeftInset();
            }
        } else {
            if (facet.mOffset >= 0) {
                if (facet.mOffsetWithPadding) {
                    alignPos += view.getPaddingTop();
                }
            } else {
                if (facet.mOffsetWithPadding) {
                    alignPos -= view.getPaddingBottom();
                }
            }
            if (facet.mOffsetPercent != ItemAlignmentFacet.ITEM_ALIGN_OFFSET_PERCENT_DISABLED) {
                alignPos += ((view == itemView ? p.getOpticalHeight(view) : view.getHeight())
                        * facet.mOffsetPercent) / 100f;
            }
            if (itemView != view) {
                sRect.top = alignPos;
                ((ViewGroup) itemView).offsetDescendantRectToMyCoords(view, sRect);
                alignPos = sRect.top - p.getOpticalTopInset();
            }
        }
        return alignPos;
    }

}
