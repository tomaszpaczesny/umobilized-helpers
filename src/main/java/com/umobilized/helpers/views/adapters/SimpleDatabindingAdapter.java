package com.umobilized.helpers.views.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Abstract adapter for presenting simple list of data.
 * It assumes usage of databinding, do it has view holder holding databinding object.
 *
 * Created by tpaczesny on 2017-02-20.
 */
public abstract class SimpleDatabindingAdapter<T> extends RecyclerView.Adapter<DatabindingViewHolder> {

    protected final List<T> mData;

    public SimpleDatabindingAdapter(List<T> initialData) {
        mData = initialData;
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

    public T getItem(int position) {
        return mData.get(position);
    }


}
