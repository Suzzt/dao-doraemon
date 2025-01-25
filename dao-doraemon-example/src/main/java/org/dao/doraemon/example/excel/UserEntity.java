package org.dao.doraemon.example.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 导入对象
 *
 * @author sucf
 * @since 1.0
 */
@Data
public class UserEntity {
    @ExcelProperty("姓名")
    // @ExcelProperty(index = 0)
    private String name;

    @ExcelProperty({"年龄"})
    //@ExcelProperty(index = 1)
    private Integer age;

    @ExcelProperty("邮箱")
    // @ExcelProperty(index = 2)
    private String email;

    /**
     * 这个是用增加标识的, 注意他的格式, 一定是$结尾的， 然后加上你的字段名, 否则不生效
     */
    private String name$;
    private String age$;
}
