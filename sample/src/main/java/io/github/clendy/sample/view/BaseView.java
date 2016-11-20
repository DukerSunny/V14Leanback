package io.github.clendy.sample.view;

/**
 * BaseView
 *
 * @author Clendy
 * @date 2016/11/16 016 13:58
 * @e-mail yc330483161@outlook.com
 */
public interface BaseView {

    void toast(String msg);

    void showProgress();

    void hideProgress();

    void showError();

    void showEmpty();

    boolean isOnFinishing();
}
