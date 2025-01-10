package org.dao.doraemon.example.database.test;

import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dao.doraemon.example.database.App;
import org.dao.doraemon.example.database.dao.entity.SysUserInfo;
import org.dao.doraemon.example.database.service.ISysUserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wuzhenhong
 * @date 2025/1/9 19:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SysUserServiceTest {

    @Autowired
    private ISysUserInfoService sysUserInfoService;

    @Test
    public void insert() {

        SysUserInfo sysUserInfo = new SysUserInfo();
        sysUserInfo.setUserName("苏城锋");
        sysUserInfo.setAccount("junmo");
        sysUserInfo.setPassword("123456");
        sysUserInfo.setAvatar("http://666.com");
        sysUserInfo.setCreateAt(LocalDateTime.now());
        sysUserInfo.setVersion(0);
        sysUserInfo.setUpdateAt(LocalDateTime.now());
        sysUserInfo.setStatus(0);

        sysUserInfoService.save(sysUserInfo);

        System.out.println(JSONUtil.toJsonStr(sysUserInfo));
    }

    @Test
    public void query() {

        SysUserInfo sysUserInfo = sysUserInfoService.lambdaQuery().eq(SysUserInfo::getAccount, "junmo")
                .one();

        System.out.println(JSONUtil.toJsonStr(sysUserInfo));
    }

    @Test
    public void mapper() {

        sysUserInfoService.updatePasswordByAccount("666", "junmo");

        SysUserInfo sysUserInfo = sysUserInfoService.lambdaQuery().eq(SysUserInfo::getAccount, "junmo")
            .one();

        System.out.println(JSONUtil.toJsonStr(sysUserInfo));
    }


    @Test
    public void sqlSession() {

        sysUserInfoService.updatePasswordByAccountSqlSession("888", "junmo");

        SysUserInfo sysUserInfo = sysUserInfoService.lambdaQuery().eq(SysUserInfo::getAccount, "junmo")
            .one();

        System.out.println(JSONUtil.toJsonStr(sysUserInfo));
    }

}
