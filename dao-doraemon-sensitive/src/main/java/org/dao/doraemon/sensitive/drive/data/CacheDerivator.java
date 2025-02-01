package org.dao.doraemon.sensitive.drive.data;

/**
 * 缓存
 *
 * @author sucf
 * @since 1.0
 */

import cn.hutool.core.lang.Snowflake;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Local Cache Derivator
 * Based On Google guava
 *
 * @author sucf
 * @since 1.0
 */
public class CacheDerivator implements Derivator {
    private final Cache<String, String> cache;

    public CacheDerivator(int maxSize, long expireAfterWriteDuration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireAfterWriteDuration, timeUnit)
                .build();
    }

    @Override
    public String positive(String target) {
        String id = new Snowflake(1, 1).nextIdStr();
        cache.put(id, target);
        return id;
    }

    @Override
    public String reverse(String target) {
        return cache.getIfPresent(target);
    }
}

