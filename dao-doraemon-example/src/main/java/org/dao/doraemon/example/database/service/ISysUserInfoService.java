package org.dao.doraemon.example.database.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.dao.doraemon.example.database.dao.entity.SysUserInfo;

/**
 * <p>
 * 系统-用户信息 服务类
 * </p>
 *
 * @author wuzhenhong
 * @since 1.0
 */
public interface ISysUserInfoService extends IService<SysUserInfo> {

    int updatePasswordByAccount(String password, String account);
    int updatePasswordByAccountSqlSession(String password, String account);

    List<SysUserInfo> queryByPassword(SysUserInfo query);

    int deleteByPassword(String password);

    int updateByPassword(String newPassword, String oldPassword);

    int updateEntityByPassword(SysUserInfo update, String oldPassword);
}
