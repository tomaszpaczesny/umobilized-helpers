package com.umobilized.helpers.views.adapters;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by tpaczesny on 2017-12-21.
 */

public class SimplePagerAdapter<T, PRESENTER> extends PagerAdapter {
    private final int mItemResId;
    private final int mModelVariableId;
    private final PRESENTER mPresenter;
    private final int mPresenterVariableId;
    private ArrayList<T> mData;

    public SimplePagerAdapter(int itemResId, int viewModelBindingVariableId, PRESENTER presenter, int presenterBindingVariableId) {
        mItemResId = itemResId;
        mModelVariableId = viewModelBindingVariableId;
        mPresenter = presenter;
        mPresenterVariableId = presenterBindingVariableId;
    }

    public SimplePagerAdapter(int itemResId, int viewModelBindingVariableId) {
        mItemResId = itemResId;
        mModelVariableId = viewModelBindingVariableId;
        mPresenter = null;
        mPresenterVariableId = -1;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ViewDataBinding)object).getRoot();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),
                mItemResId, container, true);

        binding.setVariable(mModelVariableId, mData != null ? mData.get(position) : null);
        if (mPresenter != null && mPresenterVariableId != -1) {
            binding.setVariable(mPresenterVariableId, mPresenter);
        }

        return binding;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(((ViewDataBinding)object).getRoot());
    }

    public void setData(ArrayList<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

}
