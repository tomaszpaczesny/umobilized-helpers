package com.umobilized.helpers;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Simple in-memory arguments store to allow easier passing of big objects between
 * activities/fragments without a need to serialize/deserialzie objects
 *
 * Created by tpaczesny on 2017-11-29.
 */

public class ObjectStore {

    private static long sNextHandleId = 0;
    private static HashMap<Long, Object> mArgumentsStore = new HashMap<>();
    private static HashMap<Long, WeakReference<Object>> mWeakArgumentsStore = new HashMap<>();

    /**
     * Store argument and return handle that can be used to retrieve it.
     * @param arg
     * @return
     */
    public static long store(Object arg) {
        ++sNextHandleId;
        mArgumentsStore.put(sNextHandleId, arg);
        return sNextHandleId;
    }

    /**
     * Retrieve argument using handle obtained by calling store()
     * @param handle
     * @return
     */
    public static <T> T get(long handle) {
        T object = (T) mArgumentsStore.get(handle);
        if (object != null) {
            // move once-read object to weak store, so can be accessed as long as originating data source exists
            mWeakArgumentsStore.put(handle, new WeakReference<>(object));
            mArgumentsStore.remove(handle);

            // cleanup weakArgumentsStore if needed
            checkWeakStoreCleanup();
        } else {


            WeakReference<Object> ref = mWeakArgumentsStore.get(handle);
            if (ref != null) {
                return (T) ref.get();
            }
        }

        return object;
    }

    private static void checkWeakStoreCleanup() {
        // cleanup garbage collected references if store size reaches 30 entries
        if (mWeakArgumentsStore.size() > 30) {
            ArrayList<Long> toRemove = new ArrayList<>();
            for (Long h : mWeakArgumentsStore.keySet()) {
                if (mWeakArgumentsStore.get(h).get() == null) {
                    toRemove.add(h);
                }
            }
            for (Long h : toRemove) {
                mWeakArgumentsStore.remove(h);
            }
        }
    }
}
