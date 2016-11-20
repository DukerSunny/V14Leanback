package io.github.clendy.sample.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.List;

import butterknife.BindView;
import io.github.clendy.leanback.widget.BaseGridView;
import io.github.clendy.leanback.widget.HorizontalLoadMoreGridView;
import io.github.clendy.leanback.widget.OnChildLaidOutListener;
import io.github.clendy.leanback.widget.OnChildSelectedListener;
import io.github.clendy.leanback.widget.OnLoadMoreListener;
import io.github.clendy.sample.R;
import io.github.clendy.sample.adapter.LoadMoreAdapter;
import io.github.clendy.sample.adapter.OnItemClickListener;
import io.github.clendy.sample.model.Entity;
import io.github.clendy.sample.presenter.HorizontalPresenter;
import io.github.clendy.sample.presenter.HorizontalPresenterImpl;
import io.github.clendy.sample.ui.base.BaseFragmentActivity;
import io.github.clendy.sample.view.IView;

/**
 * HorizontalActivity
 *
 * @author Clendy
 * @time 2016/11/20 16:13
 * @e-mail yc330483161@outlook.com
 */
public class HorizontalActivity extends BaseFragmentActivity<HorizontalPresenter> implements IView,
        View.OnFocusChangeListener, View.OnClickListener {

    private static final String TAG = VerticalActivity.class.getSimpleName();

    @BindView(R.id.Button1)
    Button mButton1;
    @BindView(R.id.Button2)
    Button mButton2;
    @BindView(R.id.Button3)
    Button mButton3;
    @BindView(R.id.Button4)
    Button mButton4;
    @BindView(R.id.Button5)
    Button mButton5;
    @BindView(R.id.Button6)
    Button mButton6;
    @BindView(R.id.Button7)
    Button mButton7;
    @BindView(R.id.recyclerView)
    HorizontalLoadMoreGridView mRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBarCircularIndeterminate mProgressBar;

    private View mOldFocusView;
    private SparseArray<Button> mBtnArray;
    private LoadMoreAdapter mAdapter;
    private HorizontalPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);

        initView();
        initRequest();
    }

    @Override
    public void response(final List<Entity> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }


        if (mAdapter.getItems().size() == 0) {
            mAdapter.setItems(entities);
        } else {
            mAdapter.addItems(entities);
        }

        if (entities.size() >= 20) {
            mRecyclerView.notifyMoreLoaded();
        } else {
            mRecyclerView.notifyAllLoaded();
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public boolean isOnFinishing() {
        return isFinishing();
    }

    private void initView() {
        initRecyclerView();

        mButton7.setOnClickListener(this);

        mBtnArray = new SparseArray<>(7);
        mBtnArray.append(0, mButton1);
        mBtnArray.append(1, mButton2);
        mBtnArray.append(2, mButton3);
        mBtnArray.append(3, mButton4);
        mBtnArray.append(4, mButton5);
        mBtnArray.append(5, mButton6);
        mBtnArray.append(6, mButton7);
        for (int i = 0; i < mBtnArray.size(); i++) {
            mBtnArray.get(i).setOnFocusChangeListener(this);
        }
    }

    private void initRecyclerView() {

        mAdapter = new LoadMoreAdapter(this);
        mAdapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object entity) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mPresenter.onResume();
            }

            @Override
            public void showMsgLoading() {
                toast("Loading more data...");
            }

            @Override
            public void showMsgAllLoaded() {
                toast("All data loaded!");
            }

        });

        mRecyclerView.setOnChildSelectedListener(new OnChildSelectedListener() {
            @Override
            public void onChildSelected(ViewGroup parent, View view, int position, long id) {
                Log.d(TAG, "onChildSelected position:" + position);
                if (mRecyclerView.isFocusOnRightmostColumn(view, position)) {
                    mRecyclerView.loadMoreData();
                }
            }
        });
        mRecyclerView.setOnChildLaidOutListener(new OnChildLaidOutListener() {
            @Override
            public void onChildLaidOut(ViewGroup parent, View view, int position, long id) {
            }
        });

        mRecyclerView.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_DPAD_LEFT:

                            return false;
                        case KeyEvent.KEYCODE_DPAD_UP:
                            if (mRecyclerView.isFocusOnTopRow()) {
                                if (mOldFocusView != null) {
                                    mOldFocusView.requestFocus();
                                } else {
                                    mBtnArray.get(0).requestFocus();
                                }
                                return true;
                            }
                            return false;
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            return false;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            return false;
                        default:
                            return false;
                    }

                }
                return false;
            }
        });
    }

    private void initRequest() {
        mPresenter = new HorizontalPresenterImpl(this);
        mPresenters.add(mPresenter);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.Button1:
                    mOldFocusView = mButton1;
                    break;
                case R.id.Button2:
                    mOldFocusView = mButton2;
                    break;
                case R.id.Button3:
                    mOldFocusView = mButton3;
                    break;
                case R.id.Button4:
                    mOldFocusView = mButton4;
                    break;
                case R.id.Button5:
                    mOldFocusView = mButton5;
                    break;
                case R.id.Button6:
                    mOldFocusView = mButton6;
                    break;
                case R.id.Button7:
                    mOldFocusView = mButton7;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mOldFocusView != null && !mOldFocusView.hasFocus()) {
                    mOldFocusView.requestFocus();
                    return true;
                }
                return super.onKeyDown(keyCode, event);
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Button7:
                mAdapter.getItems().clear();
                mAdapter.notifyDataSetChanged();
                mPresenter = new HorizontalPresenterImpl(this);
                break;
            default:
                break;
        }
    }
}
