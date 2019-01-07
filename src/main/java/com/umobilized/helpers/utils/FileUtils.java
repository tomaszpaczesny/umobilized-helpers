package com.umobilized.helpers.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static File getCacheFile(Context context, String subDirectoryName, String fileName, boolean preferExternal) {
        File cacheDir = getCacheDir(context, preferExternal);

        File subDirectory = new File(cacheDir, subDirectoryName);
        subDirectory.mkdir(); // creates if needed

        return new File(subDirectory, fileName);
    }

    public static File getInternalCacheFile(Context context, String subDirectoryName, String fileName) {
        return getCacheFile(context, subDirectoryName, fileName, false);
    }

    public static File[] getAllCacheFiles(Context context, String subDirectoryName, boolean preferExternal) {
        File cacheDir = getCacheDir(context, preferExternal);
        File subDirectory = new File(cacheDir, subDirectoryName);
        if (subDirectory.exists()) {
            return subDirectory.listFiles();
        } else {
            return new File[0];
        }
    }

    public static void emptyCacheDirectory(Context context, String subDirectoryName, boolean preferExternal) {
        File[] files = FileUtils.getAllCacheFiles(context, subDirectoryName, preferExternal);
        for (File file : files) {
            file.delete();
        }
    }

    /**
     * Based on:
     * https://developer.android.com/training/camera/photobasics.html
     *
     * @param context
     * @return
     * @throws IOException
     */
    public static File createImageFile(Context context, boolean inPublicDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir;
        if (inPublicDir) {
            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            storageDir = getCacheDir(context, true);
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static boolean copy(File src, File dst) {
        if (src == null || dst == null) return false;

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024*8];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return true;
        }
        catch (IOException e) {
            return false;
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    public static boolean copy(InputStream in, File dst) {
        if (in == null || dst == null) return false;

        OutputStream out = null;
        try {
            out = new FileOutputStream(dst);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024*8];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return true;
        }
        catch (IOException e) {
            return false;
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    private static File getCacheDir(Context context, boolean preferExternal) {
        File cacheDir;


        File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
        if (preferExternal && externalCacheDirs != null && externalCacheDirs.length > 0 && externalCacheDirs[0] != null) {
            cacheDir = externalCacheDirs[0];
        } else {
            cacheDir = context.getCacheDir();
        }

        return cacheDir;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1 || lastIndexOf == name.length()-1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf+1);
    }

    public static String getFileMimeType(File file, String fallbackMimeType) {
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtension(file));
        return type != null ? type : fallbackMimeType;
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
