package org.dao.doraemon.database.crypto;

import java.util.List;

/**
 * @author wuzhenhong
 * @date 2024/12/20 10:37
 */
public class ThreadLocalUtil {

    private static final ThreadLocal<List<FieldEncryptSnapshotInfo>>
    THREAD_LOCAL = new ThreadLocal<>();

    public static void set(List<FieldEncryptSnapshotInfo> list) {
        THREAD_LOCAL.set(list);
    }

    public static List<FieldEncryptSnapshotInfo> get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
         THREAD_LOCAL.remove();
    }
}
