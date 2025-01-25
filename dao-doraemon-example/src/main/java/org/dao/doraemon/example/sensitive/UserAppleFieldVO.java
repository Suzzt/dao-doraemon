package org.dao.doraemon.example.sensitive;

import lombok.Data;
import org.dao.doraemon.sensitive.annotation.SensitiveMapping;
import org.dao.doraemon.sensitive.handler.PasswordHandler;

/**
 * @author sucf
 * @since 1.0
 */
@Data
public class UserAppleFieldVO {
    private String name;
    @SensitiveMapping(fieldName = "password", handler = PasswordHandler.class)
    private String password;
    private String phone;
    private String email;
    private String address;
}
