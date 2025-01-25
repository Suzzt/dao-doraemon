package org.dao.doraemon.example.sensitive;

import org.dao.doraemon.core.ApiResult;
import org.dao.doraemon.sensitive.annotation.SensitiveMapping;
import org.dao.doraemon.sensitive.handler.PasswordHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 脱敏测试案例
 *
 * @author sucf
 * @since 1.0
 */
@RestController
@RequestMapping("sensitive")
public class SensitiveController {
    @RequestMapping("query_user")
    @SensitiveMapping(fieldName = "password", handler = PasswordHandler.class)
    public ApiResult<UserVO> queryUser1() {
        // mock data
        UserVO userVO = new UserVO();
        userVO.setEmail("wuu@163.com");
        userVO.setName("JunMo");
        userVO.setPassword("123456");
        userVO.setPhone("19822222222");
        userVO.setAddress("beijing");
        return ApiResult.success(userVO);
    }

    @RequestMapping("query_user_apple_field")
    public ApiResult<UserAppleFieldVO> queryUser2() {
        // mock data
        UserAppleFieldVO userVO = new UserAppleFieldVO();
        userVO.setEmail("wuu@163.com");
        userVO.setName("JunMo");
        userVO.setPassword("123456");
        userVO.setPhone("19822222222");
        userVO.setAddress("beijing");
        return ApiResult.success(userVO);
    }
}
