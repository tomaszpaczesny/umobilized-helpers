package com.umobilized.helpers.utils;

import java.lang.reflect.Field;

/**
 * Created by tpaczesny on 2016-12-06.
 */

public class ReflectionUtils {

    /**
     * Retrieves value of a private field of a given class in a given object or null if something went wrong.
     *
     * !!! Use with care and only if no other way !!!
     *
     * @param ofClass
     * @param inObject
     * @param fieldName
     * @return
     */

    public static Object getPrivateFieldValue(Class ofClass, Object inObject, String fieldName) {
        try {
            Field f[] = ofClass.getDeclaredFields();
            for (Field field : f) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    return field.get(inObject);
                }
            }
        }
        catch (SecurityException e) {
        }
        catch (IllegalArgumentException e) {
        }
        catch (IllegalAccessException e) {
        }
        return null;
    }

    /**
     * Sets value of a private field of a given class in a given object.
     *
     * !!! Use with care and only if no other way !!!
     *
     * @param ofClass
     * @param inObject
     * @param fieldName
     * @param value
     * @return
     */

    public static void setPrivateFieldValue(Class ofClass, Object inObject, String fieldName, Object value) {
        try {
            Field f[] = ofClass.getDeclaredFields();
            for (Field field : f) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    field.set(inObject, value);
                }
            }
        }
        catch (SecurityException e) {
        }
        catch (IllegalArgumentException e) {
        }
        catch (IllegalAccessException e) {
        }
    }

}
