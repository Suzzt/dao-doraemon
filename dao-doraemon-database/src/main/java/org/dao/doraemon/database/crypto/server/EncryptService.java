package org.dao.doraemon.database.crypto.server;

/**
 * 加密服务
 *
 * @author wuzhenhong
 * @since 1.0
 */
public interface EncryptService {

    /**
     * @param originVal 加密前的原始值
     * @return 加密后的值
     */
    String encrypt(String originVal);
}
