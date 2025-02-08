package org.dao.doraemon.example.database.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.dao.doraemon.database.crypto.annotated.Crypto;
import org.dao.doraemon.example.database.dao.entity.SysUserInfo;

/**
 * <p>
 * 系统-用户信息 Mapper 接口
 * </p>
 *
 * @author wuzhenhong
 * @since 1.0
 */
public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {

    int updatePasswordByAccount(@Crypto @Param("password") String password, @Param("account") String account);

    List<SysUserInfo> queryByPassword(SysUserInfo query);

    int deleteByPassword(@Crypto @Param("password") String password);

    int updateByPassword(@Crypto @Param("newPassword") String newPassword,
        @Crypto @Param("oldPassword") String oldPassword);

    int updateEntityByPassword(@Param("update") SysUserInfo update, @Crypto @Param("oldPassword") String oldPassword);
}
