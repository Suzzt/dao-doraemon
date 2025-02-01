package org.dao.doraemon.sensitive.drive;


import org.dao.doraemon.sensitive.drive.data.Derivator;

/**
 * @author sucf
 * @since 1.0
 */
public class SensitiveUtil {

    private static Derivator derivator;

    public SensitiveUtil(Derivator derivator) {
        SensitiveUtil.derivator = derivator;
    }

    /**
     * 获取脱敏前的原值
     *
     * @param identifyValue 标识值
     * @return 未脱敏前的值
     */
    public static String plaintext(String identifyValue) {
        return derivator.reverse(identifyValue);
    }
}
