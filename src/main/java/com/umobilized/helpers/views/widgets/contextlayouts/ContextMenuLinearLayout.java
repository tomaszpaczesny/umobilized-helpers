package com.umobilized.helpers.views.widgets.contextlayouts;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.widget.LinearLayout;

/**
 * Linear Layout that can take arbitrary object and pass it over as ContextMenuInfo
 *
 * Created by tpaczesny on 2016-12-12.
 */

public class ContextMenuLinearLayout extends LinearLayout implements ContextMenu.ContextMenuInfo, ContextMenuInfoHolder{
    private Object mMenuInfo;

    @BindingAdapter("menuInfo")
    public static void setMenuInfoBinding(ContextMenuLinearLayout layout, Object menuInfo) {
        layout.setMenuInfo(menuInfo);
    }

    public ContextMenuLinearLayout(Context context) {
        super(context);
    }

    public ContextMenuLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return this;
    }

    @Override
    public Object getMenuInfo() {
        return mMenuInfo;
    }

    @Override
    public void setMenuInfo(Object info) {
        this.mMenuInfo = info;
    }

}
