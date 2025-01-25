package org.dao.doraemon.sensitive.handler;

/**
 * 手机电话号码脱敏逻辑处理器
 *
 * @author sucf
 * @since 1.0
 */
public class PhoneNumberHandler extends Handler {

    @Override
    public String masking(String target) {
        if (target == null || target.length() < 11) {
            return target;
        }
        return target.substring(0, 3) + "****" + target.substring(7);
    }
}
