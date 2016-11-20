package io.github.clendy.sample.ui.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.clendy.sample.presenter.base.BasePresenter;


/**
 * BaseFragmentActivity
 *
 * @author Clendy
 * @date 2016/11/16 016 13:53
 * @e-mail yc330483161@outlook.com
 */
public abstract class BaseFragmentActivity<T extends BasePresenter> extends FragmentActivity {

    protected List<T> mPresenters = new ArrayList<>();
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mBind = ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenters != null && mPresenters.size() > 0) {
            for (T presenter : mPresenters) {
                presenter.onDestroy();
            }
            mPresenters.clear();
            mPresenters = null;
        }
        mBind.unbind();
    }
}
