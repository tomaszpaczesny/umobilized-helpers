package com.umobilized.helpers.app;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.umobilized.helpers.PermissionsSet;


/**
 * Created by tpaczesny on 2016-11-23.
 */

public abstract class HelperFragment extends Fragment {

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
