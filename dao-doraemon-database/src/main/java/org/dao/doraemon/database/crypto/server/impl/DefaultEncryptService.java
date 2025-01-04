package org.dao.doraemon.database.crypto.server.impl;

import java.security.GeneralSecurityException;
import org.dao.doraemon.database.crypto.server.EncryptService;
import org.dao.doraemon.database.crypto.util.AESUtil;

/**
 * @author wuzhenhong
 * @date 2024/12/30 11:16
 */
public class DefaultEncryptService implements EncryptService {

    @Override
    public String encrypt(String originVal) {
        try {
            return AESUtil.encryptECB(AESUtil.key128, originVal);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
