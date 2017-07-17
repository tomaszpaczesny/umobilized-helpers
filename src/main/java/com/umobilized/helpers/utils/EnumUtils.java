package com.umobilized.helpers.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tpaczesny on 2017-07-10.
 */

public class EnumUtils {
    /**
     * Retrieve value of @SerializedName annotation (GSON) from enum.
     * @param e
     * @return
     */
    public static String toSerializedName(Enum e) {
        try {
            return e.getClass().getField(e.name()).getAnnotation(SerializedName.class).value();
        } catch (NoSuchFieldException ex) {
            throw new IllegalStateException(e.getClass().getName() + " enum field does not have SerializedName defined.");
        }
    }

    /**
     * Return value of given enum type for which value of @SerializedName annotation (GSON) matches the provided value.
     * First match is returned of null if no match.
     * @param serializedName
     * @return
     */
    public static Enum fromSerializedName(Enum[] values, String serializedName) {
        if (serializedName == null)
            return null;

        try {
            for (Enum val : values) {
                String annotationValue = val.getClass().getField(val.name()).getAnnotation(SerializedName.class).value();
                if (serializedName.equals(annotationValue)) {
                    return val;
                }
            }
            return null; // no match

        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(values.getClass().getName() + "enum field does not have SerializedName defined.");
        }
    }
}
