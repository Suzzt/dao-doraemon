package org.dao.doraemon.core.utils;

import com.google.gson.Gson;

/**
 * Gson Utils
 *
 * @author sucf
 * @since 1.0
 */
public class GsonUtils {
    private final static Gson GSON = new Gson();
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }
}
