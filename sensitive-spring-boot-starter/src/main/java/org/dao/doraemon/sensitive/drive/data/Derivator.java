package org.dao.doraemon.sensitive.drive.data;

/**
 * 明文脱敏值推导器
 *
 * @author sucf
 * @since 1.0
 */
public interface Derivator {

    /**
     * 将脱敏值正向推导
     *
     * @param target 脱敏前数据值
     * @return 逆向数据值, 作为reverse()的入参值
     */
    String positive(String target);


    /**
     * 将脱敏值反向推导
     *
     * @param target positive生成的逆向数据值
     * @return 明码值
     */
    String reverse(String target);
}
