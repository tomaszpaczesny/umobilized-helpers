package com.umobilized.helpers.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umobilized.helpers.app.HelperActivity;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public abstract class PresenterActivity<T extends BasePresenter> extends HelperActivity implements BaseViewHandler {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        mPresenter.takeView(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mPresenter.onViewCreated();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter.hasView())
            mPresenter.dropView();

        mPresenter.onViewDestroyed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.dropView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mPresenter.hasView())
            mPresenter.takeView(this);
    }

    /**
     * Return presenter instance this activity should use. Can be new instance or static instance.
     * @return
     */
    protected abstract T initPresenter();

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
