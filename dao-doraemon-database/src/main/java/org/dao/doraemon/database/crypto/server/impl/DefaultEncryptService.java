package org.dao.doraemon.database.crypto.server.impl;

import java.security.GeneralSecurityException;
import org.dao.doraemon.database.crypto.server.EncryptService;
import org.dao.doraemon.database.crypto.util.AESUtil;

/**
 * @author wuzhenhong
 * @SInCE 1.0
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
