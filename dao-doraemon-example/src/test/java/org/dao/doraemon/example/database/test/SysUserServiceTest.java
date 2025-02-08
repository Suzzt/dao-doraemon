package org.dao.doraemon.example.database.test;

import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import java.util.List;
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
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SysUserServiceTest {

    @Autowired
    private ISysUserInfoService sysUserInfoService;

    @Test
    public void insertToTestEncrypt() {

        SysUserInfo sysUserInfo = new SysUserInfo();
        sysUserInfo.setUserName("你大爷");
        sysUserInfo.setAccount("nidaye");
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
    public void updateToTestEncrypt() {
        SysUserInfo update = new SysUserInfo();
        update.setId(1L);
        update.setPassword("999");
        sysUserInfoService.updateById(update);
    }

    @Test
    public void updateByPasswordToTestEncrypt() {
        sysUserInfoService.updateByPassword("101010","999");
    }

    @Test
    public void updateEntityByPasswordToTestEncrypt() {
        SysUserInfo update = new SysUserInfo();
        update.setAvatar("sdfsdfsd");
        update.setPassword("777");
        sysUserInfoService.updateEntityByPassword(update,"101010");
    }

    @Test
    public void deleteToTestEncrypt() {
        sysUserInfoService.deleteByPassword("777");
    }

    @Test
    public void queryToTestDeCrypt() {

        SysUserInfo sysUserInfo = sysUserInfoService.lambdaQuery().eq(SysUserInfo::getAccount, "junmo")
                .one();

        System.out.println(JSONUtil.toJsonStr(sysUserInfo));
    }

    @Test
    public void queryByPasswordToTestEncryptAndDecrypt() {
        SysUserInfo query = new SysUserInfo();
        query.setPassword("888");
        List<SysUserInfo> sysUserInfos = sysUserInfoService.queryByPassword(query);

        System.out.println(JSONUtil.toJsonStr(sysUserInfos));
    }

    @Test
    public void mapperToTestEncrypt() {

        sysUserInfoService.updatePasswordByAccount("666", "junmo");

        SysUserInfo sysUserInfo = sysUserInfoService.lambdaQuery().eq(SysUserInfo::getAccount, "junmo")
            .one();

        System.out.println(JSONUtil.toJsonStr(sysUserInfo));
    }


    @Test
    public void sqlSessionToTestEncrypt() {

        sysUserInfoService.updatePasswordByAccountSqlSession("888", "junmo");

        SysUserInfo sysUserInfo = sysUserInfoService.lambdaQuery().eq(SysUserInfo::getAccount, "junmo")
            .one();

        System.out.println(JSONUtil.toJsonStr(sysUserInfo));
    }

}
