package com.umobilized.helpers.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by tpaczesny on 2016-11-23.
 */

public class StatusBarHelper {

    /**
     * Method to set light colors as status background (where dark status bar text is needed).
     * If device is Android 6.0+ sets the requested color as status bar background.
     * If not, it tries to set fallback color.
     * @param view
     * @param color
     */
    public static void setLightStatusBarColor(View view, int color, int fallbackColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(view, color);
            setLightStatusBar(view);
        } else {
            setStatusBarColor(view, fallbackColor);
        }
    }

    /**
     * If device is Android 5.0+ sets the request color as status bar background
     * @param view
     * @param color
     */

    public static void setStatusBarColor(View view, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Context context = view.getContext();
            if (context instanceof Activity) {
                Window window = ((Activity)context).getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }

    /**
     * If device is Android 6.0+ switch status bar mode to light
     * @param view
     */
    public static void setLightStatusBar(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);

        }
    }



}
