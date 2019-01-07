package com.umobilized.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.umobilized.helpers.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Helper class to manage shared preferences-based storage for serializing simple objects using JSON
 * or objects serialization
 *
 * Created by tpaczesny on 2016-11-24.
 */

public class Storage {

    private static final String DEFAULT_PREFERENCES_FILE = "com.umobilized.helpers.storage";
    private static final String CACHE_SUBDIRECTORY = "storage";

    private final Context mContext;
    private final Gson mGson;
    private final String mPrefFileName;

    public Storage(Context context) {
        mContext = context;
        mPrefFileName = DEFAULT_PREFERENCES_FILE;
        mGson = buildGson();
    }

    public Storage(Context context, String prefFileName) {
        mContext = context;
        mPrefFileName = prefFileName;
        mGson = buildGson();
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializerDeserialzier())
                .create();
    }

    public SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(mPrefFileName, Context.MODE_PRIVATE);
    }

    public void storeObjectAsJson(String key, Object object) {
        getPrefs().edit().putString(key, toJson(object)).apply();
    }

    public <T> T  getObjectFromJson(String key, Class<T> expectedClass) {
        return fromJson(getPrefs().getString(key, null), expectedClass);
    }

    public <T> T  getObjectFromJson(String key, Type expectedType) {
        return fromJson(getPrefs().getString(key, null), expectedType);
    }

    public <T> T fromJson(String string, Class<T> expectedClass) {
        if (string != null)
            return mGson.fromJson(string,expectedClass);
        else
            return null;
    }

    public <T> T fromJson(String string, Type expectedType) {
        if (string != null)
            return mGson.fromJson(string,expectedType);
        else
            return null;
    }

    public String toJson(Object object) {
        return object != null ? mGson.toJson(object) : null;
    }


    public void storeObjectAsFile(String key, Serializable object) {
        File file = FileUtils.getInternalCacheFile(mContext, CACHE_SUBDIRECTORY, key);

        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new FileOutputStream(file, false));
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeQuietly(oos);
        }
    }

    public <T extends Serializable> T  getObjectFromFile(String key) {
        File file = FileUtils.getInternalCacheFile(mContext, CACHE_SUBDIRECTORY, key);

        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            return (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            FileUtils.closeQuietly(ois);
        }
    }


    public class UriSerializerDeserialzier implements JsonSerializer<Uri>, JsonDeserializer<Uri> {
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.getAsString());
        }
    }

}