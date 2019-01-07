package com.umobilized.helpers.app;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.umobilized.helpers.PermissionsSet;


/**
 * Created by tpaczesny on 2019-01-07.
 */

public abstract class HelperDialogFragment extends DialogFragment {

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            ViewGroup.LayoutParams params = window.getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes((android.view.WindowManager.LayoutParams) params);
        }
    }

    public boolean runPermissionsDependantAction(PermissionsSet permissions, Runnable action) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof HelperActivity) {
            return ((HelperActivity) activity).runPermissionsDependantAction(permissions,action);
        } else {
            return false;
        }
    }

    public boolean hasPermissions(PermissionsSet permissions) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof HelperActivity) {
            return ((HelperActivity) activity).hasPermissions(permissions);
        } else {
            return false;
        }
    }

    public void addOnActivityResultClient(HelperActivity.OnActivityResultClient client) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof HelperActivity) {
            ((HelperActivity) activity).addOnActivityResultClient(client);
        }
    }

    public void removeOnActivityResultClient(HelperActivity.OnActivityResultClient client) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof HelperActivity) {
            ((HelperActivity) activity).removeOnActivityResultClient(client);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            this.selectedByViewPager();
        }
    }

    /**
     * Override if needed to detect even of being selected by view pager.
     */
    public void selectedByViewPager() {
    }

    /**
     * Hides soft keyboard
     */
    public void hideSoftKeyboard() {
        Context context = getContext();
        if (context != null && context instanceof HelperActivity) {
            ((HelperActivity) context).hideSoftKeyboard();
        }
    }

    public void showSoftKeyboard(View consumer) {
        Context context = getContext();
        if (context != null && context instanceof HelperActivity) {
            ((HelperActivity) context).showSoftKeyboard(consumer);
        }
    }

    public void finishEditing() {
        Context context = getContext();
        if (context != null && context instanceof HelperActivity) {
            ((HelperActivity) context).finishEditing();
        }
    }
}
