package com.umobilized.helpers.views.adapters;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by tpaczesny on 2017-05-11.
 */

public class DatabindingViewHolder extends RecyclerView.ViewHolder {
    /**
     * Abstract binding as different view types are used.
     * Cast to actual binding as needed.
     */
    private ViewDataBinding mBinding;

    public DatabindingViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        setBinding(binding);
    }

    public void setBinding(ViewDataBinding binding) {
        mBinding = binding;
    }

    public ViewDataBinding getBinding() {
        return mBinding;
    }
}
