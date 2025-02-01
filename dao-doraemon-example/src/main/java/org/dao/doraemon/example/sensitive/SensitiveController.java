package org.dao.doraemon.example.sensitive;

import org.dao.doraemon.core.ApiResult;
import org.dao.doraemon.sensitive.annotation.MultipleSensitive;
import org.dao.doraemon.sensitive.annotation.SensitiveMapping;
import org.dao.doraemon.sensitive.drive.SensitiveUtil;
import org.dao.doraemon.sensitive.handler.PasswordHandler;
import org.dao.doraemon.sensitive.handler.PhoneNumberHandler;
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

    @RequestMapping("query_user_multiple")
    @MultipleSensitive({
            @SensitiveMapping(fieldName = "password", handler = PasswordHandler.class),
            @SensitiveMapping(fieldName = "phone", handler = PhoneNumberHandler.class)
    })
    public ApiResult<UserMultipleVO> queryUserMultiple1() {
        // mock data
        UserMultipleVO userVO = new UserMultipleVO();
        userVO.setEmail("wuu@163.com");
        userVO.setName("JunMo");
        userVO.setPassword("123456");
        userVO.setPhone("19822222222");
        userVO.setAddress("beijing");
        return ApiResult.success(userVO);
    }

    @RequestMapping("query_user_multiple_apple_field")
    public ApiResult<UserAppleFieldMultipleVO> queryUserMultiple2() {
        // mock data
        UserVO userVO = new UserVO();
        userVO.setEmail("wuu@163.com");
        userVO.setName("JunMo");
        userVO.setPassword("123456");
        userVO.setPhone("19822222222");
        userVO.setAddress("beijing");
        UserAppleFieldMultipleVO userAppleFieldMultipleVO = new UserAppleFieldMultipleVO();
        userAppleFieldMultipleVO.setUserVO(userVO);
        return ApiResult.success(userAppleFieldMultipleVO);
    }

    @RequestMapping("data")
    public ApiResult<String> value(String data) {
        String plaintext = SensitiveUtil.plaintext(data);
        return ApiResult.success(plaintext);
    }
}
