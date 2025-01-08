package org.dao.doraemon.database.crypto.util;

import java.util.Objects;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.dao.doraemon.database.crypto.annotated.Crypto;
import org.dao.doraemon.database.crypto.config.DefaultCryptStrategy;
import org.dao.doraemon.database.crypto.server.DecryptService;
import org.dao.doraemon.database.crypto.server.EncryptService;

/**
 * @author wuzhenhong
 * @since 1.0
 */
public final class MetaObjectCryptoUtil {

    private static final ObjectFactory OBJECT_FACTORY = new DefaultObjectFactory();
    private static final org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private static final SimpleHashMap<Class<?>, Object> CACHE = new SimpleHashMap<>(16);

    private MetaObjectCryptoUtil() {
        throw new RuntimeException("do not instance!");
    }

    public static  <T> T getByCache(Class<?> clazz) {
        Object value = CACHE.get(clazz);
        if (Objects.nonNull(value)) {
            return (T) value;
        }
        value = OBJECT_FACTORY.create(clazz);
        CACHE.put(clazz, value);
        return (T) value;
    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, OBJECT_FACTORY, OBJECT_WRAPPER_FACTORY,
            REFLECTOR_FACTORY);
    }

    public static String encryptNess(Crypto enOrDecryptAnnotation, String value) {
        if (Objects.isNull(value) || Objects.isNull(enOrDecryptAnnotation)) {
            return value;
        }
        if (Objects.isNull(enOrDecryptAnnotation)) {
            return value;
        }
        boolean encrypt = enOrDecryptAnnotation.encrypt();
        if (!encrypt) {
            return value;
        }
        Class<? extends EncryptService>[] encryptServerClass = enOrDecryptAnnotation.encryptClass();
        if (Objects.isNull(encryptServerClass) || encryptServerClass.length == 0) {
            if (Objects.isNull(DefaultCryptStrategy.getDefaultEncrypt())) {
                throw new RuntimeException("默认加密策略不能设置为空！");
            } else {
                encryptServerClass = new Class[]{DefaultCryptStrategy.getDefaultEncrypt()};
            }
        }
        String encryptedValue = value;
        for (Class<? extends EncryptService> encryptServiceClazz : encryptServerClass) {
            EncryptService encryptService = MetaObjectCryptoUtil.getByCache(encryptServiceClazz);
            encryptedValue = encryptService.encrypt(encryptedValue);
        }
        return encryptedValue;
    }

    public static String decryptNess(Crypto deCryptoAnnotation, String value) {
        if (Objects.isNull(value) || Objects.isNull(deCryptoAnnotation)) {
            return value;
        }
        if (Objects.isNull(deCryptoAnnotation)) {
            return value;
        }
        boolean decrypt = deCryptoAnnotation.decrypt();
        if (!decrypt) {
            return value;
        }
        Class<? extends DecryptService>[] decryptServerClass = deCryptoAnnotation.decryptClass();
        if (Objects.isNull(decryptServerClass) || decryptServerClass.length == 0) {
            if (Objects.isNull(DefaultCryptStrategy.getDefaultDecrypt())) {
                throw new RuntimeException("默认解密策略不能设置为空！");
            } else {
                decryptServerClass = new Class[]{DefaultCryptStrategy.getDefaultDecrypt()};
            }
        }
        String decryptedValue = value;
        for (Class<? extends DecryptService> decryptServiceClazz : decryptServerClass) {
            DecryptService decryptService = MetaObjectCryptoUtil.getByCache(decryptServiceClazz);
            decryptedValue = decryptService.decrypt(decryptedValue);
        }

        return decryptedValue;
    }
}
