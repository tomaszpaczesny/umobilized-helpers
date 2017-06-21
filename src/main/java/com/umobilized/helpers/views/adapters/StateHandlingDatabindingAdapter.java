package com.umobilized.helpers.views.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Generic adapter for presenting simple list of data with support to display specific item
 * when data is loading and when there is no data to show.
 *
 * It assumes usage of databinding, do it has view holder holding databinding object.
 *
 * Created by tpaczesny on 2017-06-20.
 */
public class StateHandlingDatabindingAdapter<T> extends SimpleDatabindingAdapter<T> {

    private static final int ITEM_DATA = 0;
    private static final int ITEM_LOADING = 1;
    private static final int ITEM_EMPTY = 2;

    protected final int mLoadingItemResId;
    protected final int mEmptyItemResId;

    private boolean mIsLoading;

    /**
     * Constructor that allows to provide layout id and binding variable id for simple view creation and binding.
     * @param initialData
     * @param itemResId
     * @param viewModelBindingVariableId
     */
    public StateHandlingDatabindingAdapter(List<T> initialData, int itemResId, int viewModelBindingVariableId, int loadingItemResId, int emptyItemResId) {
        super(initialData, itemResId, viewModelBindingVariableId);
        mLoadingItemResId = loadingItemResId;
        mEmptyItemResId = emptyItemResId;

        mIsLoading = false;
    }

    private boolean usesLoadingItem() {
        return mLoadingItemResId > 0;
    }

    private boolean usesEmptyItem() {
        return mEmptyItemResId > 0;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (usesLoadingItem() && mIsLoading) {
            return 1;
        } else if (mData.size() == 0 && usesEmptyItem()) {
            return 1;
        } else {
            return mData.size();
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (usesLoadingItem() && mIsLoading) {
            return ITEM_LOADING;
        } else if (mData.size() == 0 && usesEmptyItem()) {
            return ITEM_EMPTY;
        } else {
            return ITEM_DATA;
        }
    }

    public void setData(List<T> data) {
        mIsLoading = false;
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setIsLoading(boolean loading) {
        if (loading != mIsLoading) {
            mIsLoading = loading;
            notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public DatabindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding;

        switch (viewType) {
            case ITEM_DATA:
                return super.onCreateViewHolder(parent, viewType);
            case ITEM_LOADING:
                binding = DataBindingUtil.inflate(inflater, mLoadingItemResId, parent, false);
                break;
            case ITEM_EMPTY:
                binding = DataBindingUtil.inflate(inflater, mEmptyItemResId, parent, false);
                break;
            default:
                throw new IllegalStateException("Unknown viewType");
        }

        return new DatabindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DatabindingViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_DATA) {
            super.onBindViewHolder(holder, position);
        }
    }

}
