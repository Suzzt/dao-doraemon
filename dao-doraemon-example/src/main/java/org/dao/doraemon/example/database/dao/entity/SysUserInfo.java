package org.dao.doraemon.example.database.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dao.doraemon.database.crypto.annotated.Crypto;

/**
 * <p>
 * 系统-用户信息
 * </p>
 *
 * @author wuzhenhong
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_info")
public class SysUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String userName;

    private String account;

    @Crypto(encrypt = true, decrypt = true)
    private String password;

    private String avatar;

    private Integer version;

    private Integer status;


}
