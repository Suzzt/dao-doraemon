package org.dao.doraemon.example.database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dao.doraemon.example.database.dao.entity.SysUserInfo;
import org.dao.doraemon.example.database.dao.mapper.SysUserInfoMapper;
import org.dao.doraemon.example.database.service.ISysUserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统-用户信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-13
 */
@Service
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, SysUserInfo> implements ISysUserInfoService {

}
