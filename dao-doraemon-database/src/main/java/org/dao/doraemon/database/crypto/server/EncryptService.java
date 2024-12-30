package org.dao.doraemon.database.crypto.server;

/**
 * 加密服务
 *
 * @author wuzhenhong
 * @date 2024/12/30 8:28
 */
public interface EncryptService {

    /**
     * @param originVal 加密前的原始值
     * @return 加密后的值
     */
    String encrypt(String originVal);
}
