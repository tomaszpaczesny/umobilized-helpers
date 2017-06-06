package com.umobilized.helpers.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by tpaczesny on 2017-03-20.
 */

public class ExternalAppUtils {

    /**
     * Opens given URL in external activity.
     * @param url
     *  @return true if activity started
     */
    public static boolean externalUrl(Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens email client with prefilled address and subject
     * @param context
     * @param emailTo
     * @param subject
     *  @return true if activity started
     */
    public static boolean externalEmail(Context context, String emailTo, String subject, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if (emailTo != null)
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailTo});
        if (subject != null)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (content != null)
            intent.putExtra(Intent.EXTRA_TEXT, content);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens SMS/message client with prefilled recipient phone number and message
     * @param context
     * @param phoneNumber
     * @param message
     *  @return true if activity started
     */
    public static boolean externalSMS(Context context, String phoneNumber, String message) {
        Uri uri = Uri.parse("sms:" + phoneNumber);

        Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.putExtra("sms_body", message);
        intent.putExtra("address", phoneNumber);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_SENDTO);
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
            if(defaultSmsPackageName != null) {
                intent.setPackage(defaultSmsPackageName);
            }
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setType("vnd.android-dir/mms-sms");
        }

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens selection of text direct sharing apps
     * @param activity
     * @param text
     * @return
     */
    public static boolean externalTextShare(Activity activity, String text, String chooserText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(Intent.createChooser(intent, chooserText), 1000);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens dialer
     * @param context
     * @param number
     * @return
     */
    public static boolean externalDialNumber(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Opens video by URI in external activity.
     * @param videoUri
     *  @return true if activity started
     */
    public static boolean externalVideo(Context context, Uri videoUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(videoUri, "video/*");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Takes picture and stores in the provided file.
     * @param activity
     * @param fileProvider authority so file can be access by other apps ( https://developer.android.com/training/camera/photobasics.html )
     * @param outputFile file to save to
     * @param requestCode
     * @return
     */
    public static boolean externalTakePicture(Activity activity, String fileProvider, File outputFile, int requestCode) {
        if (outputFile == null) {
            throw new IllegalArgumentException("Output file is null");
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            Uri photoURI = FileProvider.getUriForFile(activity, fileProvider, outputFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            activity.startActivityForResult(intent, requestCode);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Generic class opening any intent, checking first if it can be resolved.
     * @param activity
     * @param intent
     * @return
     */
    public static boolean externalIntent(Activity activity, Intent intent) {
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    public static boolean externalAppRating(Activity activity) {
        final String packageName = activity.getApplicationContext().getPackageName();
        final Uri uri = Uri.parse("market://details?id=" + packageName);
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
            return true;
        }
        else {
            return externalUrl(activity, "https://play.google.com/store/apps/details?id=" + packageName);
        }
    }

}
