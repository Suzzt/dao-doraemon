package org.dao.doraemon.example.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 导入对象
 *
 * @author sucf
 * @create_time 2024/12/29 17:19
 */
@Data
public class UserEntity {
    @ExcelProperty("姓名")
    //@ExcelProperty(index = 0)
    private String name;

    @ExcelProperty("年龄")
    //@ExcelProperty(index = 1)
    private Integer age;

    @ExcelProperty("邮箱")
    //@ExcelProperty(index = 2)
    private String email;
}
