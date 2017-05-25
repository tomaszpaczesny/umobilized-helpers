package com.umobilized.helpers.utils;

import java.util.HashMap;

/**
 * Created by tpaczesny on 2017-05-25.
 */

public class ObjectUtils {

    public static HashMap<String,String> asMap(String... strings) {
        HashMap<String, String> map = new HashMap<>();
        if (strings.length % 2 != 0) {
            throw new IllegalArgumentException("strings length not odd");
        }
        for (int i=0; i < strings.length; i+=2) {
            map.put(strings[i], strings[i+1]);
        }
        return map;
    }
}
