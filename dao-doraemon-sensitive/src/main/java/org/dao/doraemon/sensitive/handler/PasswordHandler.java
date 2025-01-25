package org.dao.doraemon.sensitive.handler;

/**
 * 密码脱敏逻辑处理器
 *
 * @author sucf
 * @since 1.0
 */
public class PasswordHandler extends Handler {

    @Override
    public String masking(String target) {
        if (target == null || target.isEmpty()) {
            return target;
        }
        return "******";
    }
}
