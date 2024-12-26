package org.dao.doraemon.excel.server;

import org.dao.doraemon.core.ApiResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 提供 http 服务接口
 *
 * @author sucf
 * @create_time 2024/12/26 20:01
 */
@RestController
@RequestMapping("excel")
public class ExcelHttpServer {

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public ApiResult<ExcelImportResult> importExcel(@RequestBody ImportRequest request) {
        return new ApiResult<>();
    }

    /**
     * 导入excel文件请求
     */
    private class ImportRequest {
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
}
