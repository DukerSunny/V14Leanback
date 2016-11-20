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

import android.view.View;


public final class ItemAlignmentFacet {

    /**
     * Value indicates that percent is not used.
     */
    public final static float ITEM_ALIGN_OFFSET_PERCENT_DISABLED = -1;

    /**
     * Definition of an alignment position under a view.
     */
    public static class ItemAlignmentDef {
        int mViewId = View.NO_ID;
        int mFocusViewId = View.NO_ID;
        int mOffset = 0;
        float mOffsetPercent = 50f;
        boolean mOffsetWithPadding = false;

        /**
         * Sets number of pixels to offset. Can be negative for alignment from the high edge, or
         * positive for alignment from the low edge.
         */
        public final void setItemAlignmentOffset(int offset) {
            mOffset = offset;
        }

        /**
         * Gets number of pixels to offset. Can be negative for alignment from the high edge, or
         * positive for alignment from the low edge.
         */
        public final int getItemAlignmentOffset() {
            return mOffset;
        }

        /**
         * Sets whether to include left/top padding for positive item offset, include
         * right/bottom padding for negative item offset.
         */
        public final void setItemAlignmentOffsetWithPadding(boolean withPadding) {
            mOffsetWithPadding = withPadding;
        }

        /**
         * When it is true: we include left/top padding for positive item offset, include
         * right/bottom padding for negative item offset.
         */
        public final boolean isItemAlignmentOffsetWithPadding() {
            return mOffsetWithPadding;
        }

        /**
         * Sets the offset percent for item alignment in addition to offset.  E.g., 40
         * means 40% of the width from the low edge. Use {@link #ITEM_ALIGN_OFFSET_PERCENT_DISABLED}
         * to disable.
         */
        public final void setItemAlignmentOffsetPercent(float percent) {
            if ((percent < 0 || percent > 100) &&
                    percent != ITEM_ALIGN_OFFSET_PERCENT_DISABLED) {
                throw new IllegalArgumentException();
            }
            mOffsetPercent = percent;
        }

        /**
         * Gets the offset percent for item alignment in addition to offset. E.g., 40
         * means 40% of the width from the low edge. Use {@link #ITEM_ALIGN_OFFSET_PERCENT_DISABLED}
         * to disable.
         */
        public final float getItemAlignmentOffsetPercent() {
            return mOffsetPercent;
        }

        /**
         * Sets Id of which child view to be aligned.  View.NO_ID refers to root view and should
         * be only used in first one.  Extra ItemAlignmentDefs should provide view id to match
         * currently focused view.
         */
        public final void setItemAlignmentViewId(int viewId) {
            mViewId = viewId;
        }

        /**
         * Gets Id of which child view to be aligned.  View.NO_ID refers to root view and should
         * be only used in first one.  Extra ItemAlignmentDefs should provide view id to match
         * currently focused view.
         */
        public final int getItemAlignmentViewId() {
            return mViewId;
        }

        /**
         * Sets Id of which child view take focus for alignment.  When not set, it will use
         * use same id of {@link #getItemAlignmentViewId()}
         */
        public final void setItemAlignmentFocusViewId(int viewId) {
            mFocusViewId = viewId;
        }

        /**
         * Returns Id of which child view take focus for alignment.  When not set, it will use
         * use same id of {@link #getItemAlignmentViewId()}
         */
        public final int getItemAlignmentFocusViewId() {
            return mFocusViewId != View.NO_ID ? mFocusViewId : mViewId;
        }
    }

    private ItemAlignmentDef[] mAlignmentDefs = new ItemAlignmentDef[]{new ItemAlignmentDef()};

    public boolean isMultiAlignment() {
        return mAlignmentDefs.length > 1;
    }

    /**
     * Sets definitions of alignment positions.
     */
    public void setAlignmentDefs(ItemAlignmentDef[] defs) {
        if (defs == null || defs.length < 1) {
            throw new IllegalArgumentException();
        }
        mAlignmentDefs = defs;
    }

    /**
     * Returns read only definitions of alignment positions.
     */
    public ItemAlignmentDef[] getAlignmentDefs() {
        return mAlignmentDefs;
    }

}
