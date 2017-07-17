package com.umobilized.helpers.views.widgets.contextlayouts;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.widget.FrameLayout;

/**
 * Frame Layout that can take arbitrary object and pass it over as ContextMenuInfo
 *
 * Created by tpaczesny on 2016-12-12.
 */

public class ContextMenuFrameLayout extends FrameLayout implements ContextMenu.ContextMenuInfo, ContextMenuInfoHolder{
    private Object mMenuInfo;

    @BindingAdapter("menuInfo")
    public static void setMenuInfoBinding(ContextMenuFrameLayout layout, Object menuInfo) {
        layout.setMenuInfo(menuInfo);
    }

    public ContextMenuFrameLayout(Context context) {
        super(context);
    }

    public ContextMenuFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
