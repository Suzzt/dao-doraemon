package org.dao.doraemon.excel.parameter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * http 请求导入参数
 *
 * @author sucf
 * @since 1.0
 */
@Setter
@Getter
public class ImportRequest {
    /**
     * 业务处理code,用定位到是哪种处理器来处理.
     */
    private String code;

    /**
     * 请求业务的参数
     * 这个用来定义自己业务所需的参数
     */
    private Map<String, Object> param;

    /**
     * excel文件
     */
    private MultipartFile file;

}
