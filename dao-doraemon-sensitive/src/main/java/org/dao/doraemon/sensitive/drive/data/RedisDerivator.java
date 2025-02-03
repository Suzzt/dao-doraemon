package org.dao.doraemon.sensitive.drive.data;

import cn.hutool.core.lang.Snowflake;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis Derivator

 * @author sucf
 * @since 1.0
 */
public class RedisDerivator implements Derivator {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisDerivator(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String positive(String target) {
        String id = new Snowflake(1, 1).nextIdStr();
        stringRedisTemplate.opsForValue().set(id, target);
        return id;
    }

    @Override
    public String reverse(String target) {
        return stringRedisTemplate.opsForValue().get(target);
    }
}
