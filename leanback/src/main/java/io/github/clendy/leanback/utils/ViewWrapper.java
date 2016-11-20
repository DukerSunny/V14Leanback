package io.github.clendy.leanback.utils;

import android.view.View;

/**
 * The type View wrapper.
 *
 * @author Clendy
 * @time 2016 /11/21 00:03
 * @e-mail yc330483161 @outlook.com
 */
public class ViewWrapper {

    private View view;
    private int width;
    private int height;

    public ViewWrapper(View view) {
        this.view = view;
    }

    public int getWidth() {
        return view.getLayoutParams().width;
    }

    public void setWidth(int width) {
        this.width = width;
        view.getLayoutParams().width = width;
        view.requestLayout();
    }

    public int getHeight() {
        return view.getLayoutParams().height;
    }

    public void setHeight(int height) {
        this.height = height;
        view.getLayoutParams().height = height;
        view.requestLayout();
    }
}
