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
    @RequestMapping("apple_method")
    @SensitiveMapping(fieldName = "password", handler = PasswordHandler.class)
    public ApiResult<AppleMethodVO> appleMethod() {
        AppleMethodVO appleMethodVO = new AppleMethodVO();
        appleMethodVO.setEmail("wuu@163.com");
        appleMethodVO.setName("JunMo");
        appleMethodVO.setPassword("123456");
        appleMethodVO.setPhone("19822222222");
        appleMethodVO.setAddress("beijing");
        return ApiResult.success(appleMethodVO);
    }

    @RequestMapping("apple_field")
    public ApiResult<AppleFieldVO> appleField() {
        AppleFieldVO appleFieldVO = new AppleFieldVO();
        appleFieldVO.setEmail("wuu@163.com");
        appleFieldVO.setName("JunMo");
        appleFieldVO.setPassword("123456");
        appleFieldVO.setPhone("19822222222");
        appleFieldVO.setAddress("beijing");
        return ApiResult.success(appleFieldVO);
    }

    @RequestMapping("apple_class")
    public ApiResult<AppleClassVO> appleClass() {
        AppleClassVO appleClassVO = new AppleClassVO();
        appleClassVO.setEmail("wuu@163.com");
        appleClassVO.setName("JunMo");
        appleClassVO.setPassword("123456");
        appleClassVO.setPhone("19822222222");
        appleClassVO.setAddress("beijing");
        return ApiResult.success(appleClassVO);
    }

    @RequestMapping("multiple_apple_method")
    @MultipleSensitive({
            @SensitiveMapping(fieldName = "password", handler = PasswordHandler.class),
            @SensitiveMapping(fieldName = "phone", handler = PhoneNumberHandler.class)
    })
    public ApiResult<AppleMethodVO> multipleAppleMethod() {
        AppleMethodVO appleMethodVO = new AppleMethodVO();
        appleMethodVO.setEmail("wuu@163.com");
        appleMethodVO.setName("JunMo");
        appleMethodVO.setPassword("123456");
        appleMethodVO.setPhone("19822222222");
        appleMethodVO.setAddress("beijing");
        return ApiResult.success(appleMethodVO);
    }

    @RequestMapping("multiple_apple_field")
    public ApiResult<FieldMultipleVO> multipleAppleField() {
        AppleVO appleVO = new AppleVO();
        appleVO.setEmail("wuu@163.com");
        appleVO.setName("JunMo");
        appleVO.setPassword("123456");
        appleVO.setPhone("19822222222");
        appleVO.setAddress("beijing");
        FieldMultipleVO fieldMultipleVO = new FieldMultipleVO();
        fieldMultipleVO.setAppleVO(appleVO);
        return ApiResult.success(fieldMultipleVO);
    }

    @RequestMapping("multiple_apple_class")
    public ApiResult<ClassMultipleVO> multipleAppleClass() {
        AppleVO appleVO = new AppleVO();
        appleVO.setEmail("wuu@163.com");
        appleVO.setName("JunMo");
        appleVO.setPassword("123456");
        appleVO.setPhone("19822222222");
        appleVO.setAddress("beijing");
        ClassMultipleVO classMultipleVO = new ClassMultipleVO();
        classMultipleVO.setAppleVO(appleVO);
        return ApiResult.success(classMultipleVO);
    }

    @RequestMapping("data")
    public ApiResult<String> value(String data) {
        String plaintext = SensitiveUtil.plaintext(data);
        return ApiResult.success(plaintext);
    }
}