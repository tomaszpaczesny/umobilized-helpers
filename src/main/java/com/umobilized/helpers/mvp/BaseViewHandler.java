package com.umobilized.helpers.mvp;

import android.app.Activity;
import android.content.Context;

import com.umobilized.helpers.PermissionsSet;
import com.umobilized.helpers.app.HelperActivity;

/**
 * Created by tpaczesny on 2017-03-20.
 */

public interface BaseViewHandler {
    Activity getActivity();
    Context getContext();
    String getString(int resId);

    /**
     * Request action to be run when permissions are granted. May run immediately if all permissions are available.
     * @param permissions
     * @param action
     * @return true if action was executed in place; false it it scheduled to be executed after permissions are granted
     */
    boolean runPermissionsDependantAction(PermissionsSet permissions, Runnable action);

    /**
     * Checks if given permission set is fully granted.
     * @param permissions
     * @return
     */

    boolean hasPermissions(PermissionsSet permissions);


    /**
     * Registers callback that will be executed when activty is opened with "onActivityResult"
     * method. Registered clients are cleared on activity onDestroy(), so need to be registered
     * in onCreate().
     * @param client
     */
    void addOnActivityResultClient(HelperActivity.OnActivityResultClient client);

    /**
     * Hides soft keyboard
     */
    void hideSoftKeyboard();

    /**
     * Clears current focus and hides soft keyboard if needed.
     */
    void finishEditing();
}
