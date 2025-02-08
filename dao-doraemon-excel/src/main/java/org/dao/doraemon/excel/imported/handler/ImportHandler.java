package org.dao.doraemon.excel.imported.handler;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dao.doraemon.excel.model.ImportBatchResultModel;
import org.dao.doraemon.excel.model.ImportResultModel;
import org.dao.doraemon.excel.wrapper.DataWrapper;

import java.util.List;
import java.util.Map;

/**
 * 导入Excel处理能力接口
 *
 * @author sucf
 * @since 1.0
 */
public interface ImportHandler<T> {

    /**
     * 处理检验Excel表头是否符合
     *
     * @param headMap          表头对象
     * @param requestParameter 请求参数
     * @return 处理结果定义
     */
    ImportResultModel checkHead(Map<Integer, String> headMap, String requestParameter);

    /**
     * 处理你的业务
     *
     * @param dataWrapper      业务包装对象
     * @param requestParameter 请求参数
     * @return 处理结果定义
     */
    ImportResultModel process(DataWrapper<T> dataWrapper, String requestParameter);

    /**
     * 批量处理你的业务
     * 当你设置定义
     * {@link org.dao.doraemon.excel.properties.ExcelImportProperties}中的batchProcessRows>1时，会调用此方法
     *
     * @param data             批量数据
     * @param requestParameter 请求参数
     * @return 处理结果列表
     */
    List<ImportBatchResultModel> batchProcess(List<DataWrapper<T>> data, String requestParameter);

    /**
     * 定义导入失败的错误文件名
     *
     * @param parameter excel配置
     * @return 下载时的文件名
     */
    String defineFailFileName(String parameter);


    /**
     * 构建错误表头样式
     *
     * @param workbook   workbook
     * @param sheet      sheet
     * @param headRow    表头生成所在行
     * @param headColumn 表头生成所在列
     * @param headTitle  表头名称
     * @param parameter  请求参数
     */
    void generateErrorHeadStyle(Workbook workbook, Sheet sheet, int headRow, int headColumn, String headTitle, String parameter);
}
