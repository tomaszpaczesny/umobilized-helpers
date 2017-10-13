package com.umobilized.helpers;

import android.Manifest;
import android.os.Build;

/**
 * Info about all possible permissions sets needed by the app
 *
 * Created by tpaczesny on 2016-12-21.
 */

public enum PermissionsSet {
    MEDIA_CAPTURE, CAMERA, AUDIO_CAPTURE, LOCATION, SAVE_TO_GALLERY, TAKE_PICTURE, CONTACTS, ACCESS_GALLERY;

    public String[] toPermissionsArray() {
        switch (this) {
            case MEDIA_CAPTURE:
                return new String[] {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            case CAMERA:
                return new String[] {Manifest.permission.CAMERA};
            case AUDIO_CAPTURE:
                return new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            case LOCATION:
                return new String[] {Manifest.permission.ACCESS_FINE_LOCATION};
            case SAVE_TO_GALLERY:
                return new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            case TAKE_PICTURE:
                return new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            case ACCESS_GALLERY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    return new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
                } else {
                    return new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                }
            case CONTACTS:
                return new String[] {Manifest.permission.READ_CONTACTS};
            default:
                return null;
        }
    }

    public int toRequestCode() {
        return this.ordinal();
    }

}
