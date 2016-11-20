package io.github.clendy.sample.presenter.base;

import java.util.ArrayList;
import java.util.List;

import io.github.clendy.sample.view.BaseView;
import rx.Subscription;

/**
 * BasePresenterImpl
 *
 * @author Clendy
 * @date 2016/11/16 016 15:55
 * @e-mail yc330483161@outlook.com
 */
public class BasePresenterImpl<M extends BaseView, V> implements BasePresenter {

    protected List<Subscription> mSubscriptionList;
    protected M mView;

    public BasePresenterImpl(M mView) {
        this.mView = mView;
        mSubscriptionList = new ArrayList<>();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        if (mSubscriptionList != null && mSubscriptionList.size() > 0) {
            for (Subscription subscription : mSubscriptionList) {
                if (subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
        }

        mView = null;
    }

    protected boolean canPresenting(){
        return mView != null && !mView.isOnFinishing();
    }
}
