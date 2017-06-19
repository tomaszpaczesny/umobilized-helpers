package com.umobilized.helpers.mvvm;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.umobilized.helpers.app.HelperFragment;

/**
 * Stores ViewModel as a field and calls callback methods on it.
 *
 * Created by tpaczesny on 2017-05-11.
 */

public class ViewModelFragment<T extends ViewModel> extends HelperFragment {

    private T mViewModel;

    public void setViewModel(T viewModel) {
        mViewModel = viewModel;
    }

    public T getViewModel() {
        return mViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mViewModel != null) {
            mViewModel.onCreate();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            mViewModel.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel != null) {
            mViewModel.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewModel != null) {
            mViewModel.onPause();
        }
    }
}
