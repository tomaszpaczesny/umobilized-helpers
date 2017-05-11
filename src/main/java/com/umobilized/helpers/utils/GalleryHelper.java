package com.umobilized.helpers.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Helper methods related to Gallery operations
 *
 * Created by tpaczesny on 2016-12-12.
 */

public class GalleryHelper {

    private static SimpleDateFormat GALLERY_FILE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

    public static Cursor getGalleryImagesCursor(Activity activity) {
        return getGalleryImagesCursor(activity, 0);
    }

    public static Cursor getGalleryImagesCursor(Activity activity, int limit) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        String sortOrder = MediaStore.MediaColumns.DATE_ADDED + " desc";

        // if needed add limit clause
        if (limit > 0) {
            sortOrder += " limit " + limit;
        }

        Cursor cursor = null;
        try {
            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, sortOrder);
        } catch (SecurityException e) {
            // permissions missing, will return null
        }
        return cursor;
    }

    /**
     * Gets File object representing file in device public Gallery where media can be stored.
     * @param prefix
     * @param extension
     * @param appDirName Application directory name (directory in gallery)
     * @return
     */

    public static File getGalleryOutputFile(String prefix, String extension, String appDirName) {
        if (!FileUtils.isExternalStorageWritable())
            return null;

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirName);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = GALLERY_FILE_TIME_FORMAT.format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                prefix + timeStamp + "." + extension);
    }

}
