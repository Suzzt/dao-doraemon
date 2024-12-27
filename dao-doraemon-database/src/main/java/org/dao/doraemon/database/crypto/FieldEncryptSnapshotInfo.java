package org.dao.doraemon.database.crypto;

import java.lang.reflect.Field;

/**
 * @author wuzhenhong
 * @date 2024/12/20 10:38
 */
public class FieldEncryptSnapshotInfo {

    private Object containBean;
    private Field field;

    private Object origin;
    private Object encrypt;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getOrigin() {
        return origin;
    }

    public void setOrigin(Object origin) {
        this.origin = origin;
    }

    public Object getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Object encrypt) {
        this.encrypt = encrypt;
    }

    public Object getContainBean() {
        return containBean;
    }

    public void setContainBean(Object containBean) {
        this.containBean = containBean;
    }
}
