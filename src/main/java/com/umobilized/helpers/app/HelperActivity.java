package com.umobilized.helpers.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.umobilized.helpers.PermissionsSet;

import java.util.ArrayList;

/**
 * Base class for all application activities providing some useful utils.
 *
 * Created by tpaczesny on 2016-11-23.
 */

public abstract class HelperActivity extends AppCompatActivity {

    private static int sStartedActivitiesCount = 0;

    private SparseArray<Runnable> mPendingActions;
    private ArrayList<OnActivityResultClient> mActivityResultClients;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPendingActions = new SparseArray<>();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPendingActions = null;
        if (mActivityResultClients != null) {
            mActivityResultClients.clear();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sStartedActivitiesCount == 0) {
            onAppOpened();
        }
        sStartedActivitiesCount++;
    }

    @Override
    protected void onStop() {
        super.onStop();
        sStartedActivitiesCount--;
        if (sStartedActivitiesCount == 0) {
            onAppClosed();
        }
    }

    /**
     * Called when first activity of HelperActivity type is started.
     */
    protected void onAppOpened() {

    }

    /**
     * Called when last activity of HelperActivity type is stopped.
     */
    protected void onAppClosed() {

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // post that delayed so this is processed after Activities onResume
        mHandler.post(() -> {
            if (mActivityResultClients != null) {
                for (OnActivityResultClient client : mActivityResultClients) {
                    client.onActivityResult(requestCode, resultCode, data);
                }
            }
        });

    }

    /**
     * Registers callback that will be executed when activty is opened with "onActivityResult"
     * method. Registered clients are cleared on activity onDestroy(), so need to be registered
     * in onCreate().
     * @param client
     */
    public void addOnActivityResultClient(OnActivityResultClient client) {
        if (mActivityResultClients == null) {
            mActivityResultClients = new ArrayList<>();
        }

        mActivityResultClients.add(client);
    }


    /**
     * Requests permission for all missing permission from the provided permissions list.
     *
     * @param permissions
     * @return true if some permission were requested, false if all requested permissions are granted
     */

    public boolean requestMissingPermissions(PermissionsSet permissions) {
        ArrayList<String> missingPermList = new ArrayList<>();
        String[] permissionsArray = permissions.toPermissionsArray();
        if (permissionsArray == null) {
            throw new IllegalArgumentException("Unknown permissions set");
        }
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermList.add(permission);
            }
        }

        if (missingPermList.size() == 0) {
            return false;
        } else {
            String[] missingPerm = new String[missingPermList.size()];
            for (int i=0; i<missingPerm.length; i++) {
                missingPerm[i] = missingPermList.get(i);
            }
            ActivityCompat.requestPermissions(this, missingPerm, permissions.toRequestCode());
            return true;
        }
    }

    /**
     * Request action to be run when permissions are granted. May run immediately if all permissions are available.
     * @param permissions
     * @param action
     * @return true if action was executed in place; false it it scheduled to be executed after permissions are granted
     */
    public boolean runPermissionsDependantAction(PermissionsSet permissions, Runnable action) {
        if (!requestMissingPermissions(permissions)) {
            // if we have all permissions, run immediately
            action.run();
            return true;
        } else {
            mPendingActions.put(permissions.toRequestCode(), action);
            return false;
        }
    }

    public boolean hasPermissions(PermissionsSet permissions) {
        String[] permissionsArray = permissions.toPermissionsArray();
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Runnable pendingAction = mPendingActions.get(requestCode);
        if (pendingAction != null) {
            // we need all permissions granted to run pendingAction
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            // all granted, run action
            mPendingActions.remove(requestCode);
            mHandler.post(pendingAction);
        }
    }

    /**
     * Hides soft keyboard
     */
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * Shows soft keyboard
     */
    public void showSoftKeyboard(View consumer) {
        if (consumer != null) {
            consumer.requestFocus();

            // FIXME: bit of a hack; showSoftInput must be called once consumer isActive;
            Handler handler = new Handler(getMainLooper());
            handler.postDelayed(() -> {
                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.showSoftInput(consumer, InputMethodManager.SHOW_IMPLICIT);
            }, 100);

        } else {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        }
    }



    /**
     * Clears current focus and hides soft keyboard if needed.
     */
    public void finishEditing() {
        View view = getWindow().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
        }
        hideSoftKeyboard();
    }

    public interface OnActivityResultClient {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}
