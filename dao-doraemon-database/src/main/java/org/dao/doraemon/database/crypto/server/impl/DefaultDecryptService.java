package org.dao.doraemon.database.crypto.server.impl;

import java.security.GeneralSecurityException;
import org.dao.doraemon.database.crypto.server.DecryptService;
import org.dao.doraemon.database.crypto.util.AESUtil;

/**
 * @author wuzhenhong
 * @SInCE 1.0
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
