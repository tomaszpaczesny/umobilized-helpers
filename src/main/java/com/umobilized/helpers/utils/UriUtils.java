package com.umobilized.helpers.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by tpaczesny on 2017-04-07.
 */

public class UriUtils {

    public static String getLocalPath(Uri uri, Context context)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIdx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIdx);
        cursor.close();
        return path;
    }

    public static Uri getResourceUri(Context context, int resourceId) {
        Resources resources = context.getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + resources.getResourcePackageName(resourceId)
                + File.separator + resources.getResourceTypeName(resourceId)
                + File.separator + resources.getResourceEntryName(resourceId) );
        return imageUri;
    }

    public static Uri safeParse(String uriString) {
        if (uriString == null) {
            return null;
        }
        else {
            return Uri.parse(uriString);
        }
    }

    public static Uri safeParseFilepath(String filePath) {
        if (filePath == null) {
            return null;
        }
        else {
            return Uri.fromFile(new File(filePath));
        }
    }

    public static String uriToFilePath(Uri uri) {
        if (uri == null)
            return null;

        String filePrefix = ContentResolver.SCHEME_FILE + ":";
        String stringUri = uri.toString();
        if (stringUri.startsWith(filePrefix)) {
            return stringUri.substring(filePrefix.length());
        } else {
            return null;
        }
    }
}
