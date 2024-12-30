package org.dao.doraemon.database.crypto.server;

/**
 * 解密服务
 *
 * @author wuzhenhong
 * @date 2024/12/30 8:28
 */
public interface DecryptService {

    /**
     * @param cryptVal 加密值
     * @return 解密值
     */
    String decrypt(String cryptVal);
}
