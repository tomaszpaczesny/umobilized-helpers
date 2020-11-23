package com.umobilized.helpers.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.umobilized.helpers.app.HelperFragment;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public abstract class PresenterFragment<T extends BasePresenter> extends HelperFragment implements BaseViewHandler {

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mPresenter = initPresenter();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter.hasView())
            mPresenter.dropView();

        mPresenter.onViewDestroyed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mPresenter.hasView())
            mPresenter.takeView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.dropView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mPresenter.hasView())
            mPresenter.takeView(this);

        mPresenter.onViewCreated();
    }

    /**
     * Return presenter instance this fragment should use. Can be new instance or static instance.
     * @return
     */
    protected abstract T initPresenter() throws IllegalAccessException, java.lang.InstantiationException;
    
}
