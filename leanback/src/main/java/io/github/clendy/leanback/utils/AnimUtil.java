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

package io.github.clendy.leanback.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * AnimUtil
 *
 * @author Clendy
 * @date 2016/11/16 016 17:09
 * @e-mail yc330483161@outlook.com
 */
public class AnimUtil {

    public static synchronized void animateScaleUp(View v, float targetScale) {
        float currentScaleX = v.getScaleX();
        float currentScaleY = v.getScaleY();

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, currentScaleX, targetScale);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, currentScaleY, targetScale);
        ValueAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(v, scaleXHolder, scaleYHolder);
        scaleAnimator.start();
    }

    public static synchronized void animateScaleDown(View v, float targetScale) {
        float currentScaleX = v.getScaleX();
        float currentScaleY = v.getScaleY();

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, currentScaleX, targetScale);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, currentScaleY, targetScale);

        ValueAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(v, scaleXHolder, scaleYHolder);
        scaleAnimator.start();
    }

    public static void scaleAnim(@NonNull View v, float scaleX, float scaleY, long duration) {
        v.animate().scaleX(scaleX).scaleY(scaleY).setDuration(duration).start();

    }

    public static void scaleAnim(@NonNull View v, float scaleX, float scaleY, long duration,
                                 Animator.AnimatorListener listener) {
        v.animate().scaleX(scaleX).scaleY(scaleY).setDuration(duration).setListener(listener).start();

    }

}
