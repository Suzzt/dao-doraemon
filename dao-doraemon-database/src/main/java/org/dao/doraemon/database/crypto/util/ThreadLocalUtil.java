package org.dao.doraemon.database.crypto.util;

import java.util.List;
import org.dao.doraemon.database.crypto.bo.FieldEncryptSnapshotBo;

/**
 * @author wuzhenhong
 * @date 2024/12/20 10:37
 */
public class ThreadLocalUtil {

    private static final ThreadLocal<List<FieldEncryptSnapshotBo>>
        THREAD_LOCAL = new ThreadLocal<>();

    public static void set(List<FieldEncryptSnapshotBo> list) {
        THREAD_LOCAL.set(list);
    }

    public static List<FieldEncryptSnapshotBo> get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
