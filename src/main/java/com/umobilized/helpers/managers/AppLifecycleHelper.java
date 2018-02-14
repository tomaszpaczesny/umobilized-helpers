package com.umobilized.helpers.managers;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.LinkedHashSet;

/**
 * Created by tpaczesny on 17/12/2017.
 */

public class AppLifecycleHelper implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "AppLifecycleHelper";

    private static AppLifecycleHelper sInstance;

    public static AppLifecycleHelper getInstance() {
        if (sInstance == null)
            sInstance = new AppLifecycleHelper();
        return sInstance;
    }

    private int mActivityCnt;
    private Activity mForegroundActivity;
    private LinkedHashSet<AppVisibilityListener> mAppVisibilityListeners;

    private AppLifecycleHelper() {
        mActivityCnt = 0;
        mAppVisibilityListeners = new LinkedHashSet<>();
    }

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    public void addAppVisibilityListener(AppVisibilityListener listener) {
        mAppVisibilityListeners.add(listener);
    }

    public void removeAddVisibilityListener(AppVisibilityListener listener) {
        mAppVisibilityListeners.remove(listener);
    }

    public boolean isForeground() {
        return mActivityCnt > 0;
    }

    public Activity getForegroundActivity() {
        return mForegroundActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        mActivityCnt++;
        if (mActivityCnt == 1) {
            notifyAppVisibilityListeners(true);
        }
        mForegroundActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        mActivityCnt--;
        if (mActivityCnt == 0) {
            notifyAppVisibilityListeners(false);
            mForegroundActivity = null;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    private void notifyAppVisibilityListeners(boolean foreground) {
        Log.i(TAG, "App visibility changed to: " + (foreground ? "foreground" : "background"));
        for (AppVisibilityListener listener : mAppVisibilityListeners) {
            listener.appVisibilityChanged(foreground);
        }
    }

    public interface AppVisibilityListener {
        void appVisibilityChanged(boolean foreground);
    }
}
