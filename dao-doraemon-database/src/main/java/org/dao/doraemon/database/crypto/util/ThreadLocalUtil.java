package org.dao.doraemon.database.crypto.util;

import java.util.List;
import org.dao.doraemon.database.crypto.bo.FieldEncryptSnapshot;

/**
 * @author wuzhenhong
 * @author wuzhenhong
 */
public class ThreadLocalUtil {

    private static final ThreadLocal<List<FieldEncryptSnapshot>>
        THREAD_LOCAL = new ThreadLocal<>();

    public static void set(List<FieldEncryptSnapshot> list) {
        THREAD_LOCAL.set(list);
    }

    public static List<FieldEncryptSnapshot> get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
