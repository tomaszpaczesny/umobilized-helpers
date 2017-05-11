package com.umobilized.helpers.mvp;

/**
 * Created by tpaczesny on 2016-09-02.
 */
public class BasePresenter<T extends BaseViewHandler> {

    protected T mView;

    protected BasePresenter() {
    }

    public void takeView(T view) {
        mView = view;
    }

    public void dropView() {
        mView = null;
    }

    public void onViewCreated() {
    }

    public void onViewDestroyed() {
    }

    public final boolean hasView() {
        return mView != null;
    }


}
