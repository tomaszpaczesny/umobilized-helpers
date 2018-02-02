package com.umobilized.helpers.utils;

import android.os.Build;
import android.view.View;
import android.view.ViewParent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tpaczesny on 2017-02-10.
 */

public class ViewUtils {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in {setId #(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.generateViewId();
        } else {
            for (;;) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        }
    }

    public static <T> T findViewInParent(View inView, int viewId, int maxDepth) {
        for (ViewParent parent; maxDepth>0; maxDepth--) {
            parent = inView.getParent();
            if (parent != null && parent instanceof View) {
                View foundView = ((View) parent).findViewById(viewId);
                if (foundView != null) {
                    return (T) foundView;
                } else {
                    inView = (View) parent;
                }
            } else {
                return null;
            }
        }
        return null;
    }


    public static <T> T findParentOfType(View inView, Class<T> ofClass) {
        ViewParent parent = null;
        while ((parent = inView.getParent()) != null) {
            if (parent.getClass().isAssignableFrom(ofClass)) {
                return (T)parent;
            } else if (parent instanceof View){
                inView = (View) parent;
            } else {
                break;
            }
        }
        return null;
    }
}
