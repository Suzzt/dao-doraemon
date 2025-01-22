package org.dao.doraemon.core.utils;

import com.google.gson.Gson;

/**
 * Gson Utils
 *
 * @author sucf
 * @since 2025/1/20 17:19
 */
public class GsonUtils {
    private final static Gson GSON = new Gson();

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }
}
