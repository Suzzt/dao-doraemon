package org.dao.doraemon.excel.wrapper;

import lombok.Data;
import org.dao.doraemon.excel.imported.handler.ImportHandler;
import org.dao.doraemon.excel.properties.ExcelImportProperties;

/**
 * Excel 导入 Wrapper
 *
 * @author sucf
 * @since 1.0
 */
@Data
public class ExcelImportWrapper {
    private ExcelImportProperties excelImportProperties;
    private ImportHandler<?> importHandler;
}
