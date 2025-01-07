package org.dao.doraemon.database.crypto.server;

/**
 * 解密服务
 *
 * @author wuzhenhong
 * @since 1.0
 */
public interface DecryptService {

    /**
     * @param cryptVal 加密值
     * @return 解密值
     */
    String decrypt(String cryptVal);
}
