package org.dao.doraemon.example.sensitive;

import lombok.Data;

/**
 * @author sucf
 * @since 1.0
 */
@Data
public class UserVO {
    private String name;
    private String password;
    private String phone;
    private String email;
    private String address;
}
