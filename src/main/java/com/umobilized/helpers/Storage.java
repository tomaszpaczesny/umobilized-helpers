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

/** Helper class to manage shared preferences-based storage for serializing simple objects using JSON
 * or objects serialization
 *
 * Created by tpaczesny on 2016-11-24.
 */

public class Storage {

    private static final String PREFERENCES_FILE = "com.umobilized.helpers.storage";
    private static final String CACHE_SUBDIRECTORY = "storage";

    private Context mContext;
    private Gson mGson;

    public Storage(Context context) {
        mContext = context;

        mGson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializerDeserialzier())
                .create();
    }

    public SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public void storeObjectAsJson(String key, Object object) {
        getPrefs().edit().putString(key, serialize(object)).apply();
    }

    public Object getObjectFromJson(String key, Class expectedClass) {
        return deserialize(getPrefs().getString(key, null), expectedClass);
    }

    public Object getObjectFromJson(String key, Type expectedType) {
        return deserialize(getPrefs().getString(key, null), expectedType);
    }

    private Object deserialize(String string, Class expectedClass) {
        if (string != null)
            return mGson.fromJson(string,expectedClass);
        else
            return null;
    }

    private Object deserialize(String string, Type expectedType) {
        if (string != null)
            return mGson.fromJson(string,expectedType);
        else
            return null;
    }

    private String serialize(Object object) {
        return object != null ? mGson.toJson(object) : null;
    }


    public void storeObjectAsFile(String key, Serializable object) {
        File file = FileUtils.getCacheFile(mContext, CACHE_SUBDIRECTORY, key);

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

    public Serializable getObjectFromFile(String key) {
        File file = FileUtils.getCacheFile(mContext, CACHE_SUBDIRECTORY, key);

        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            return (Serializable) ois.readObject();
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