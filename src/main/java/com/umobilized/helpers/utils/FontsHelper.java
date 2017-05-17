package com.umobilized.helpers.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by tpaczesny on 2017-05-17.
 */

public class FontsHelper {

    private static HashMap<String, Typeface> mFontsCache = new HashMap<>(); // name : typeface object

    public static Typeface getFont(String name) {
        return mFontsCache.get(name);
    }

    public static void loadFont(Context context, String assetsPath, String name) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), assetsPath);
        if (typeface == null) {
            throw new IllegalArgumentException("Failed loading typeface from: " + assetsPath);
        }
        mFontsCache.put(name, typeface);
    }

}
