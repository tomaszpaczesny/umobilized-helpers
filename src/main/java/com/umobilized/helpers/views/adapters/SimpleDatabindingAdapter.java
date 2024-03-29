package com.umobilized.helpers.views.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic adapter for presenting simple list of data.
 * It assumes usage of databinding, do it has view holder holding databinding object.
 *
 * Created by tpaczesny on 2017-02-20.
 */
public class SimpleDatabindingAdapter<T> extends RecyclerView.Adapter<DatabindingViewHolder> {

    protected final List<T> mData;
    protected final int mItemResId;
    protected final int mViewModelVarId;
    private Pair<Integer,Object>[] mGlobalObjects;

    /**
     * Constructor providing initial data only. Uset his constructor only when extending this class.
     * Remember to provide own implementation of onCreateViewHolder and onBindViewHolder
     * @param initialData
     */
    public SimpleDatabindingAdapter(List<T> initialData) {
        mData = initialData != null ? initialData : new ArrayList<>();
        mItemResId = -1;
        mViewModelVarId = -1;
        mGlobalObjects = null;
    }

    /**
     * Constructor that allows to provide layout id and binding variable id for simple view creation and binding.
     * @param initialData
     * @param itemResId
     * @param viewModelBindingVariableId
     */
    public SimpleDatabindingAdapter(List<T> initialData, int itemResId, int viewModelBindingVariableId, Pair<Integer,Object>... globalObjects) {
        mData = initialData != null ? initialData : new ArrayList<>();
        mItemResId = itemResId;
        mViewModelVarId = viewModelBindingVariableId;
        mGlobalObjects = globalObjects;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void setData(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setGlobalObjects(Pair<Integer,Object>... globalObjects) {
        mGlobalObjects = globalObjects;
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public DatabindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mItemResId == -1) {
            throw new IllegalStateException("onCreteViewHolder must be overriden or itemResId provided");
        }
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, mItemResId, parent, false);

        return new DatabindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DatabindingViewHolder holder, int position) {
        if (mViewModelVarId == -1) {
            throw new IllegalStateException("onBindViewHolder must be overriden or viewModelBindingVariableId provided");
        }
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(mViewModelVarId, getItem(position));

        // bind global objects
        if (mGlobalObjects != null) {
            for (Pair<Integer, Object> pair : mGlobalObjects) {
                binding.setVariable(pair.first, pair.second);
            }
        }

        binding.executePendingBindings();
    }

}
