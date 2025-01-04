package org.dao.doraemon.database.crypto.server.impl;

import java.security.GeneralSecurityException;
import org.dao.doraemon.database.crypto.server.DecryptService;
import org.dao.doraemon.database.crypto.util.AESUtil;

/**
 * @author wuzhenhong
 * @date 2024/12/30 11:16
 */
public class DefaultDecryptService implements DecryptService {

    @Override
    public String decrypt(String cryptVal) {
        try {
            return AESUtil.decryptECB(AESUtil.key128, cryptVal);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
