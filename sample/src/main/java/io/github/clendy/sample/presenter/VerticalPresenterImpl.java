package io.github.clendy.sample.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.clendy.sample.model.Entity;
import io.github.clendy.sample.presenter.base.BasePresenterImpl;
import io.github.clendy.sample.view.IView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * VerticalPresenterImpl
 *
 * @author Clendy
 * @date 2016/11/16 016 16:34
 * @e-mail yc330483161@outlook.com
 */
public class VerticalPresenterImpl extends BasePresenterImpl<IView, Entity>
        implements VerticalPresenter{

    private int offset = 0;
    private static final int LIMIT = 20;
    private static final int COUNT = 116;


    public VerticalPresenterImpl(IView mView) {
        super(mView);
        doRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        doRequest();
    }

    public void doRequest() {
        if (offset < COUNT) {
            mSubscriptionList.add(Observable
                    .create(new Observable.OnSubscribe<List<Entity>>() {
                        @Override
                        public void call(Subscriber<? super List<Entity>> subscriber) {
                            List<Entity> entities = new ArrayList<>();
                            if (offset < 100) {
                                for (int i = 0; i < 20; i++) {
                                    int random = (int) (Math.random() * 200);
                                    entities.add(new Entity(random, "She is GaoYuanyuan", null));
                                }
                            } else {
                                for (int i = 0; i < 16; i++) {
                                    int random = (int) (Math.random() * 200);
                                    entities.add(new Entity(random, "She is GaoYuanyuan", null));
                                }
                            }
                            subscriber.onNext(entities);
                            subscriber.onCompleted();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            mView.showProgress();
                        }
                    })
                    .delay(3000, TimeUnit.MILLISECONDS, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Entity>>() {
                        @Override
                        public void onCompleted() {
                            mView.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<Entity> entities) {
                            if (entities != null && entities.size() > 0) {
                                mView.response(entities);
                                offset += 20;
                            }
                        }
                    }));
        }
    }
}
