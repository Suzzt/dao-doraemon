package org.dao.doraemon.database.crypto.server;

/**
 * 加密服务
 * @author wuzhenhong
 * @date 2024/12/27 9:32
 */
public interface CryptoServer {

    /**
     *
     * @param originVal 加密前的原始值
     * @return 加密后的值
     */
    String crypto(String originVal);
}
