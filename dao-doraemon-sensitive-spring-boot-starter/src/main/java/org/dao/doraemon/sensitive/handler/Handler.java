package org.dao.doraemon.sensitive.handler;

import org.dao.doraemon.sensitive.drive.data.Derivator;
import org.dao.doraemon.sensitive.model.SensitiveEntry;


/**
 * 脱敏逻辑处理器
 *
 * @author sucf
 * @since 1.0
 */
public abstract class Handler {

    /**
     * 脱敏逻辑
     * 用户自己实现自己的脱敏逻辑
     *
     * @param target 脱敏前的目标值
     * @return 脱敏后的目标值
     */
    public abstract String masking(String target);

    /**
     * 主流程链路上加载的脱敏载体对象
     *
     * @param target    脱敏前的目标值
     * @param derivator 推导器
     * @return 脱敏载体对象
     */
    public SensitiveEntry generate(String target, Derivator derivator) {
        return new SensitiveEntry(derivator.positive(target), masking(target));
    }
}
