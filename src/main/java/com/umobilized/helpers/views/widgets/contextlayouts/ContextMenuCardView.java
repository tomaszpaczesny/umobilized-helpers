package com.umobilized.helpers.views.widgets.contextlayouts;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;
import android.util.AttributeSet;
import android.view.ContextMenu;

/**
 * CardView extenasion that can take arbitrary object and pass it over as ContextMenuInfo
 *
 * Created by tpaczesny on 2016-12-12.
 */

public class ContextMenuCardView extends CardView implements ContextMenu.ContextMenuInfo, ContextMenuInfoHolder{
    private Object mMenuInfo;

    @BindingAdapter("menuInfo")
    public static void setMenuInfoBinding(ContextMenuCardView layout, Object menuInfo) {
        layout.setMenuInfo(menuInfo);
    }

    public ContextMenuCardView(Context context) {
        super(context);
    }

    public ContextMenuCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContextMenuCardView(Context context, AttributeSet attrs, int defStyleAttr) {
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
