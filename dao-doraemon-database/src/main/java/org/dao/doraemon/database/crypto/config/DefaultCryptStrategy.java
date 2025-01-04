package org.dao.doraemon.database.crypto.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import org.dao.doraemon.database.crypto.server.DecryptService;
import org.dao.doraemon.database.crypto.server.EncryptService;
import org.dao.doraemon.database.crypto.server.impl.DefaultDecryptService;
import org.dao.doraemon.database.crypto.server.impl.DefaultEncryptService;

/**
 * @author wuzhenhong
 * @date 2024/12/30 13:57
 */
public class DefaultCryptStrategy {

    private static final String CONFIG_NAME = "default_crypt.properties";

    private static Class<? extends EncryptService> defaultEncrypt = DefaultEncryptService.class;
    private static Class<? extends DecryptService> defaultDecrypt = DefaultDecryptService.class;

    public static Class<? extends EncryptService> getDefaultEncrypt() {
        return defaultEncrypt;
    }

    public static Class<? extends DecryptService> getDefaultDecrypt() {
        return defaultDecrypt;
    }

    public static void loadDefaultCrypto() {
        try {
            Enumeration<URL> enumeration = DefaultCryptStrategy.class.getClassLoader().getResources(File.separator + CONFIG_NAME);
            while(enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                Properties properties = new Properties();
                properties.load(url.openStream());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
