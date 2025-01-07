package org.dao.doraemon.database.crypto.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import org.dao.doraemon.database.crypto.server.DecryptService;
import org.dao.doraemon.database.crypto.server.EncryptService;
import org.dao.doraemon.database.crypto.server.impl.DefaultDecryptService;
import org.dao.doraemon.database.crypto.server.impl.DefaultEncryptService;
import org.springframework.util.ClassUtils;

/**
 * @author wuzhenhong
 * @SInCE 1.0
 */
public class DefaultCryptStrategy {

    private static final String CONFIG_NAME = "default_crypt.properties";
    private static final String DEFAULT_ENCRYPT_NAME = "defaultEncryptService";
    private static final String DEFAULT_DECRYPT_NAME = "defaultDecryptService";

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
            URL url = DefaultCryptStrategy.class.getClassLoader().getResource(File.separator + CONFIG_NAME);
            if (Objects.nonNull(url)) {
                Properties properties = new Properties();
                properties.load(url.openStream());
                String encryptClassName = properties.getProperty(DEFAULT_ENCRYPT_NAME);
                if (Objects.nonNull(encryptClassName) && !encryptClassName.trim().isEmpty()) {
                    try {
                        Class<?> defaultEntryClass = ClassUtils.forName(encryptClassName,
                            DefaultCryptStrategy.class.getClassLoader());
                        if (!EncryptService.class.isAssignableFrom(defaultEntryClass)) {
                            throw new RuntimeException(String.format("className -> %s 未实现%s", encryptClassName,
                                EncryptService.class.getName()));
                        }
                        defaultEncrypt = (Class<? extends EncryptService>) defaultEntryClass;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                String decryptClassName = properties.getProperty(DEFAULT_DECRYPT_NAME);
                if (Objects.nonNull(decryptClassName) && !decryptClassName.trim().isEmpty()) {
                    try {
                        Class<?> defaultDecryClass = ClassUtils.forName(decryptClassName,
                            DefaultCryptStrategy.class.getClassLoader());
                        if (!DecryptService.class.isAssignableFrom(defaultDecryClass)) {
                            throw new RuntimeException(String.format("className -> %s 未实现%s", decryptClassName,
                                DecryptService.class.getName()));
                        }
                        defaultDecrypt = (Class<? extends DecryptService>) defaultDecryClass;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
