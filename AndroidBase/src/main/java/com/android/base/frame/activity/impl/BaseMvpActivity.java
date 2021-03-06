package com.android.base.frame.activity.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.android.base.frame.view.ViewWithPresenter;
import com.android.base.frame.presenter.BasePresenter;
import com.android.base.frame.presenter.impl.PresenterLifecycleDelegate;
import com.android.base.frame.presenter.factory.PresenterFactory;
import com.android.base.frame.presenter.factory.ReflectionPresenterFactory;
import com.android.base.netstate.NetWorkUtil;

/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends SuperActivity  implements ViewWithPresenter<P> {


    private static final String PRESENTER_STATE_KEY = "presenter_state";
    private PresenterLifecycleDelegate<P> mPresenterDelegate = new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));

    @Override
    public PresenterFactory<P> getPresenterFactory() {
        return mPresenterDelegate.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        mPresenterDelegate.setPresenterFactory(presenterFactory);
    }

    @Override
    public P getPresenter() {
        return mPresenterDelegate.getPresenter();
    }

    @Override
    protected void initData() {
        getPresenter().start();
    }

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPresenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));
        }

        mPresenterDelegate.onCreate(this,this);

        initView(savedInstanceState);

        initData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, mPresenterDelegate.onSaveInstanceState());
    }


    @Override
    public void onBackPressedSupport() {
        if (mPresenterDelegate == null || !mPresenterDelegate.onBackPressed()) {
            super.onBackPressedSupport();
        }
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        mPresenterDelegate.onDestroy(!isChangingConfigurations());
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenterDelegate.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onNetWorkDisConnect() {
        mPresenterDelegate.onNetWorkDisConnect();
    }

    @Override
    public void onNetWorkConnect(NetWorkUtil.NetWorkType type) {
        mPresenterDelegate.onNetWorkConnect(type);
    }

}
