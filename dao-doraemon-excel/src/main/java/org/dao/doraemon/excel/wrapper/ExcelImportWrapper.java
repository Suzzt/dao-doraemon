package org.dao.doraemon.excel.wrapper;

import lombok.Data;
import org.dao.doraemon.excel.imported.handler.ImportHandler;
import org.dao.doraemon.excel.properties.ExcelImportProperties;

/**
 * Excel 导入 Wrapper
 *
 * @author sucf
 * @create_time 2024/12/29 20:40
 */
@Data
public class ExcelImportWrapper {
    private ExcelImportProperties excelImportProperties;
    private ImportHandler<?> importHandler;
}
