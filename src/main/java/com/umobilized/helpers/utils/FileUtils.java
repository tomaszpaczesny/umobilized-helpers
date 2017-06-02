package com.umobilized.helpers.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tpaczesny on 2016-11-29.
 */

public class FileUtils {

    public static boolean writeBytes(byte[] bytes, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(fos);
        }
    }

    public static boolean writeBitmap(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(format, quality, out);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(out);
        }
    }

    public static byte[] readBytes(File file) {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));

            int size = (int) file.length();
            byte[] bytes = new byte[size];
            bis.read(bytes, 0, bytes.length);

            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeQuietly(bis);
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getCacheFile(Context context, String subDirectoryName, String fileName) {
        File cacheDir = getCacheDir(context);

        File subDirectory = new File(cacheDir, subDirectoryName);
        subDirectory.mkdir(); // creates if needed

        return new File(subDirectory, fileName);
    }

    public static File[] getAllCacheFiles(Context context, String subDirectoryName) {
        File cacheDir = getCacheDir(context);
        File subDirectory = new File(cacheDir, subDirectoryName);
        if (subDirectory.exists()) {
            return subDirectory.listFiles();
        } else {
            return new File[0];
        }
    }

    public static void emptyDirectory(Context context, String subDirectoryName) {
        File[] files = FileUtils.getAllCacheFiles(context, subDirectoryName);
        for (File file : files) {
            file.delete();
        }
    }

    private static File getCacheDir(Context context) {
        File cacheDir;


        File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
        if (externalCacheDirs != null && externalCacheDirs.length > 0 && externalCacheDirs[0] != null) {
            cacheDir = externalCacheDirs[0];
        } else {
            cacheDir = context.getCacheDir();
        }

        return cacheDir;
    }

    public static void closeQuietly(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeQuietly(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
