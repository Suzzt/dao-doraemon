package org.dao.doraemon.excel.server;

import org.dao.doraemon.core.ApiResult;
import org.dao.doraemon.excel.exception.ExcelMarkException;
import org.dao.doraemon.excel.imported.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 提供 http 服务接口
 *
 * @author sucf
 * @since 2024/12/26 20:01
 */
@RestController
@RequestMapping("excel")
public class ExcelHttpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelHttpServer.class);

    private final Dispatcher dispatcher;


    public ExcelHttpServer(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public ApiResult<ExcelImportResult> importExcel(@RequestParam("file") MultipartFile file, @RequestParam("code") String code, @RequestParam(value = "param", required = false) String param) {
        ApiResult<ExcelImportResult> apiResult = new ApiResult<>();
        if (file == null) {
            apiResult.setSuccess(false);
            apiResult.setMessage("The file cannot be empty!");
            return apiResult;
        }
        if (code == null) {
            apiResult.setSuccess(false);
            apiResult.setMessage("The code cannot be empty!");
            return apiResult;
        }
        try {
            ExcelImportResult result = dispatcher.execute(code, param, file.getInputStream());
            apiResult.setSuccess(true);
            apiResult.setData(result);
            return apiResult;
        } catch (ExcelMarkException e) {
            LOGGER.error("Excel file process error!", e);
            apiResult.setSuccess(false);
            apiResult.setMessage(e.getMessage());
            return apiResult;
        } catch (Exception e) {
            LOGGER.error("Excel file process error!", e);
            apiResult.setSuccess(false);
            apiResult.setMessage("Excel file process error!");
            return apiResult;
        }
    }

    @RequestMapping(value = "download/{var1}/{var2}", method = RequestMethod.GET)
    public void download(@PathVariable("var1") String var1,
                         @PathVariable("var2") String var2,
                         HttpServletResponse response) {
        InputStream inputStream = dispatcher.download(var1 + File.separator + var2);
        if (inputStream == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(var2, "UTF-8"));

            byte[] buffer = new byte[1024];
            int bytesRead;

            try (OutputStream out = response.getOutputStream()) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

        } catch (IOException e) {
            LOGGER.error("file download error", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error("close inputStream error", e);
            }
        }
    }
}
