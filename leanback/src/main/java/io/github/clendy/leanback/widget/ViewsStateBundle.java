/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.util.LruCache;
import android.util.SparseArray;
import android.view.View;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class ViewsStateBundle {

    public static final int LIMIT_DEFAULT = 100;
    public static final int UNLIMITED = Integer.MAX_VALUE;

    private int mSavePolicy;
    private int mLimitNumber;

    private LruCache<String, SparseArray<Parcelable>> mChildStates;

    public ViewsStateBundle() {
        mSavePolicy = BaseGridView.SAVE_NO_CHILD;
        mLimitNumber = LIMIT_DEFAULT;
    }

    public void clear() {
        if (mChildStates != null) {
            mChildStates.evictAll();
        }
    }

    public void remove(int id) {
        if (mChildStates != null && mChildStates.size() != 0) {
            mChildStates.remove(getSaveStatesKey(id));
        }
    }

    /**
     * @return the saved views states
     */
    public final Bundle saveAsBundle() {
        if (mChildStates == null || mChildStates.size() == 0) {
            return null;
        }
        Map<String, SparseArray<Parcelable>> snapshot = mChildStates.snapshot();
        Bundle bundle = new Bundle();
        for (Iterator<Entry<String, SparseArray<Parcelable>>> i =
             snapshot.entrySet().iterator(); i.hasNext(); ) {
            Entry<String, SparseArray<Parcelable>> e = i.next();
            bundle.putSparseParcelableArray(e.getKey(), e.getValue());
        }
        return bundle;
    }

    public final void loadFromBundle(Bundle savedBundle) {
        if (mChildStates != null && savedBundle != null) {
            mChildStates.evictAll();
            for (Iterator<String> i = savedBundle.keySet().iterator(); i.hasNext(); ) {
                String key = i.next();
                mChildStates.put(key, savedBundle.getSparseParcelableArray(key));
            }
        }
    }

    public final int getSavePolicy() {
        return mSavePolicy;
    }

    public final int getLimitNumber() {
        return mLimitNumber;
    }

    /**
     * @see ViewsStateBundle#getSavePolicy()
     */
    public final void setSavePolicy(int savePolicy) {
        this.mSavePolicy = savePolicy;
        applyPolicyChanges();
    }

    /**
     * @see ViewsStateBundle#getLimitNumber()
     */
    public final void setLimitNumber(int limitNumber) {
        this.mLimitNumber = limitNumber;
        applyPolicyChanges();
    }

    protected void applyPolicyChanges() {
        if (mSavePolicy == BaseGridView.SAVE_LIMITED_CHILD) {
            if (mLimitNumber <= 0) {
                throw new IllegalArgumentException();
            }
            if (mChildStates == null || mChildStates.maxSize() != mLimitNumber) {
                mChildStates = new LruCache<String, SparseArray<Parcelable>>(mLimitNumber);
            }
        } else if (mSavePolicy == BaseGridView.SAVE_ALL_CHILD || mSavePolicy == BaseGridView.SAVE_ON_SCREEN_CHILD) {
            if (mChildStates == null || mChildStates.maxSize() != UNLIMITED) {
                mChildStates = new LruCache<String, SparseArray<Parcelable>>(UNLIMITED);
            }
        } else {
            mChildStates = null;
        }
    }

    /**
     * Load view from states, it's none operation if the there is no state associated with the id.
     *
     * @param view view where loads into
     * @param id   unique id for the view within this ViewsStateBundle
     */
    public final void loadView(View view, int id) {
        if (mChildStates != null) {
            String key = getSaveStatesKey(id);
            // Once loaded the state, do not keep the state of child. The child state will
            // be saved again either when child is offscreen or when the parent is saved.
            SparseArray<Parcelable> container = mChildStates.remove(key);
            if (container != null) {
                view.restoreHierarchyState(container);
            }
        }
    }

    /**
     * Save views regardless what's the current policy is.
     *
     * @param view view to save
     * @param id   unique id for the view within this ViewsStateBundle
     */
    protected final void saveViewUnchecked(View view, int id) {
        if (mChildStates != null) {
            String key = getSaveStatesKey(id);
            SparseArray<Parcelable> container = new SparseArray<Parcelable>();
            view.saveHierarchyState(container);
            mChildStates.put(key, container);
        }
    }

    public final Bundle saveOnScreenView(Bundle bundle, View view, int id) {
        if (mSavePolicy != BaseGridView.SAVE_NO_CHILD) {
            String key = getSaveStatesKey(id);
            SparseArray<Parcelable> container = new SparseArray<Parcelable>();
            view.saveHierarchyState(container);
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray(key, container);
        }
        return bundle;
    }

    /**
     * Save off screen views according to policy.
     *
     * @param view view to save
     * @param id   unique id for the view within this ViewsStateBundle
     */
    public final void saveOffscreenView(View view, int id) {
        switch (mSavePolicy) {
            case BaseGridView.SAVE_LIMITED_CHILD:
            case BaseGridView.SAVE_ALL_CHILD:
                saveViewUnchecked(view, id);
                break;
            case BaseGridView.SAVE_ON_SCREEN_CHILD:
                remove(id);
                break;
            default:
                break;
        }
    }

    static String getSaveStatesKey(int id) {
        return Integer.toString(id);
    }
}
